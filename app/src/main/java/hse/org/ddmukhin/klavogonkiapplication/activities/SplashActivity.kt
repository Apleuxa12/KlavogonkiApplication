package hse.org.ddmukhin.klavogonkiapplication.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import hse.org.ddmukhin.klavogonkiapplication.R
import moxy.MvpActivity

class SplashActivity : MvpActivity() {

    private var mDelayHandler: Handler? = null

    private val nextActivity = MainMenuActivity::class.java

    private val mRunnable = Runnable {
        if(!isFinishing){
            startActivity(Intent(applicationContext, nextActivity))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_layout)

        mDelayHandler = Handler()

        mDelayHandler!!.postDelayed(mRunnable, resources.getInteger(R.integer.splash_duration) * 1000L)
    }

    override fun onDestroy() {
        if(mDelayHandler != null)
            mDelayHandler!!.removeCallbacks(mRunnable)

        super.onDestroy()
    }
}