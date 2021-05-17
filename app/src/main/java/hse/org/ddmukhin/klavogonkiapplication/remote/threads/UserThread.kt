package hse.org.ddmukhin.klavogonkiapplication.remote.threads

import android.util.Log
import hse.org.ddmukhin.klavogonkiapplication.presenters.GamePresenter
import hse.org.ddmukhin.klavogonkiapplication.remote.data.ColoredText
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Text
import hse.org.ddmukhin.klavogonkiapplication.utils.SerializationUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class UserThread(
    private val socket: Socket,
    private val userName: String,
    private val gamePresenter: GamePresenter
) : Thread() {

    private val writer: PrintWriter
    private val reader: BufferedReader

    private lateinit var text: Text

    private var inputText = ""

    init {
        gamePresenter.addInputTextChangedListener { s -> inputText = s }

        reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        writer = PrintWriter(socket.getOutputStream(), true)
    }

    override fun run() {
        try {
//      Send userName to server
            Log.d("UserThread", "$userName connected")
            sendMessage(userName)
//      Get Text from server
            text = readMessage()
            var message = ColoredText(text, ArrayList(), false)
            do {
//          Send-read messages to/from server
                if (inputText.isEmpty())
                    continue
                Log.d("INPUT", inputText)
                sendMessage(inputText)
                message = readMessage()
                gamePresenter.textChanged(message)
            } while (!message.isFinished)
            gamePresenter.showResults(readMessage())
        } catch (e: IOException) {
            gamePresenter.showError("Ошибка соединения")
        } finally {
            close()
        }
    }

    private fun close() {
        reader.close()
        writer.close()
        socket.close()
    }

    @Throws(IOException::class)
    private inline fun <reified T> readMessage(): T {
        val msg = SerializationUtils.deserializeMessage<T>(reader.readLine())
        Log.d("READ", msg.toString())
        return msg
    }

    @Throws(IOException::class)
    private fun <T> sendMessage(message: T) {
        val msg = SerializationUtils.serializeMessage(message)
        Log.d("WRITE", msg)
        writer.println(msg)
    }
}