package hse.org.ddmukhin.klavogonkiapplication.utils

import android.os.Build

class SystemUtils {

    companion object{

        fun isGenymotion(): Boolean{
            return Build.MANUFACTURER.contains("Genymotion")
        }

    }

}