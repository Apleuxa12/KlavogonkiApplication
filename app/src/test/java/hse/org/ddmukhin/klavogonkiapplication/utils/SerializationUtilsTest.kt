package hse.org.ddmukhin.klavogonkiapplication.utils

import android.util.Log
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Message
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Text
import hse.org.ddmukhin.klavogonkiapplication.remote.threads.UserThread
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.Socket

internal class SerializationUtilsTest{

    @Test
    fun testSerialize(){
        assertEquals(SerializationUtils.serializeMessage(Message("VALUE")), "{\"value\":\"VALUE\"}")
    }

    @Test
    fun testDeserialize(){
        assertEquals(SerializationUtils.deserializeMessage<Message<String>>("{\"value\":\"VALUE\"}").value
            , "VALUE")
    }

    @Test
    fun readLine(){
        val text = readMessage<Text>()
        assertEquals(text.theme, "MockTheme")
    }

    private inline fun <reified T> readMessage(): T {
        val msg = SerializationUtils.
        deserializeMessage<T>("{\"value\":{\"theme\":\"MockTheme\",\"value\":\"Mock mock mock mock mock mock mock\"}}")
//        Log.d("Message read", msg.toString())
        return msg
    }
}