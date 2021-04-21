package hse.org.ddmukhin.klavogonkiapplication.remote.threads

import hse.org.ddmukhin.klavogonkiapplication.presenters.GamePresenter
import hse.org.ddmukhin.klavogonkiapplication.remote.data.ColoredText
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Results
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Text
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Message
import hse.org.ddmukhin.klavogonkiapplication.utils.SerializationUtils
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

    private lateinit var text: Text

    private var inputText = ""

    init {
        gamePresenter.addInputTextChangedListener { s -> inputText = s }
    }

    override fun run() {
        reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        writer = PrintWriter(socket.getOutputStream(), true)

//      Send userName to server
        sendMessage(Message(userName))
//      Get Text from server
        text = readMessage<Text>().value
        var clientMessage: Message<String>
        var message: Message<ColoredText>

        do {
//          Send-read messages to/from server
            clientMessage = Message(inputText)
            sendMessage(clientMessage)
            message = readMessage()
            gamePresenter.textChanged(message.value)
            if (message.value.isFinished) {
                gamePresenter.gameFinished(readMessage<Results>().value)
                break
            }
        } while (true)
    }

    private fun <T> readMessage(): Message<T> =
        SerializationUtils.deserializeMessage(reader.readLine())

    private fun <T> sendMessage(message: Message<T>) = writer.println(message.toString())
}