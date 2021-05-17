package hse.org.ddmukhin.klavogonkiapplication.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Message

class SerializationUtils {

    companion object{
        fun <T> serializeMessage(message: T): String{
            return Gson().toJson(message)
        }

        inline fun <reified T> deserializeMessage(message: String): T{
            return Gson().fromJson<T>(message, object: TypeToken<T>(){}.type)
        }
    }

}