package hse.org.ddmukhin.klavogonkiapplication.remote.data

import hse.org.ddmukhin.klavogonkiapplication.utils.SerializationUtils

open class Message<T>(val value: T) {

    override fun toString(): String {
        return SerializationUtils.serializeMessage(this)
    }
}