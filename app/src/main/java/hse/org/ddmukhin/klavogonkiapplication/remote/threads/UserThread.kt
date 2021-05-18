package hse.org.ddmukhin.klavogonkiapplication.remote.threads

import android.util.Log
import hse.org.ddmukhin.klavogonkiapplication.presenters.GamePresenter
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Color
import hse.org.ddmukhin.klavogonkiapplication.remote.data.ColoredText
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Results
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Text
import hse.org.ddmukhin.klavogonkiapplication.utils.SerializationUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.NullPointerException
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
            val textLine = reader.readLine() ?: throw NullPointerException("Потеряно соединение с сервером")
            text = readMessage(textLine)
            var message = ColoredText(text, calculateInitialColors(text), false)
            gamePresenter.textChanged(message)
            do {
//          Send-read messages to/from server
                if (inputText.isEmpty())
                    continue
//                Log.d("INPUT", inputText)
                sendMessage(inputText)
                val inLine = reader.readLine() ?: throw NullPointerException("Потеряно соединение с сервером")
                message = readMessage(inLine)
                gamePresenter.textChanged(message)
            } while (!message.isFinished)
            val resultLine = reader.readLine() ?: ""
            if(resultLine.isNotEmpty())
                gamePresenter.showResults(readMessage(resultLine))
            else
                gamePresenter.showResults(Results(0, 0.0))
        } catch (e: IOException) {
            gamePresenter.showError("Ошибка соединения")
        }catch(e: NullPointerException){
            gamePresenter.showError(e.message!!)
        } finally {
            close()
        }
    }

    private fun close() {
        reader.close()
        writer.close()
        socket.close()
    }

    private fun calculateInitialColors(text: Text) : ArrayList<Color>{
        val colors = ArrayList<Color>()

        for(letter in text.value){
            colors.add(
                when(letter){
                    ' ' -> Color.SPACE
                    else -> Color.NEUTRAL
                }
            )
        }
        return colors
    }

    @Throws(IOException::class)
    private inline fun <reified T> readMessage(input: String): T {
        val msg = SerializationUtils.deserializeMessage<T>(input)
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