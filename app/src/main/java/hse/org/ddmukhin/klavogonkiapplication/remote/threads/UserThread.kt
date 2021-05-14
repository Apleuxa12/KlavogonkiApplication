package hse.org.ddmukhin.klavogonkiapplication.remote.threads

import android.util.Log
import hse.org.ddmukhin.klavogonkiapplication.presenters.GamePresenter
import hse.org.ddmukhin.klavogonkiapplication.remote.data.ColoredText
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Results
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Text
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Message
import hse.org.ddmukhin.klavogonkiapplication.utils.SerializationUtils
import kotlinx.coroutines.CoroutineScope
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class UserThread(
    private val socket: Socket,
    private val userName: String,
    private val gamePresenter: GamePresenter
) : Thread() {

    private lateinit var writer: PrintWriter
    private lateinit var reader: BufferedReader

    private var text: Text? = null

    private var inputText = ""

    init {
        gamePresenter.addInputTextChangedListener { s -> inputText = s }
    }

    override fun start() {
        super.start()
        Log.d("UserThread", "Creation")
        reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        writer = PrintWriter(socket.getOutputStream(), true)
    }

    override fun run() {
//      Send userName to server
        sendMessage(Message(userName))
//      Get Text from server
        text = readMessage<Text>().value
        var message = Message(ColoredText(text, ArrayList(), false))
        do {
//          Send-read messages to/from server
            if(inputText.isEmpty())
                continue
            sendMessage(Message(inputText))
            message = readMessage()
            gamePresenter.textChanged(message.value)
        } while (!message.value.isFinished)
        gamePresenter.showResults(readMessage<Results>().value)

        close()
    }

    private fun close() {
        reader.close()
        writer.close()
        socket.close()
    }

    private fun <T> readMessage(): Message<T> =
        SerializationUtils.deserializeMessage(reader.readLine())

    private fun <T> sendMessage(message: Message<T>) = writer.println(message.toString())
}