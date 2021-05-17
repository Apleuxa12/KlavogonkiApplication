package hse.org.ddmukhin.klavogonkiapplication.presenters

import android.os.AsyncTask
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import hse.org.ddmukhin.klavogonkiapplication.remote.data.ColoredText
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Results
import hse.org.ddmukhin.klavogonkiapplication.remote.threads.UserThread
import hse.org.ddmukhin.klavogonkiapplication.views.GameView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import moxy.DefaultViewState
import moxy.MvpPresenter
import moxy.MvpView
import java.io.IOException
import java.net.ConnectException
import java.net.Socket
import javax.inject.Inject

class GamePresenter @Inject constructor() : MvpPresenter<GameView>() {

    private class ThreadAsyncTask(
        private val viewState: GameView,
        private val userName: String,
        private val host: String,
        private val port: Int,
        private val connectionMaxTries: Int
    ) :
        AsyncTask<GamePresenter, Unit, Boolean>() {

        private var connectionTries = 0

        override fun onPreExecute() {
            viewState.showLoading()
        }

        override fun doInBackground(vararg params: GamePresenter?): Boolean {
            var socket: Socket? = null
            while (true) {
                try {
                    Log.d("Socket", "Trying to connect...")
                    if (socket != null || connectionTries >= connectionMaxTries)
                        break
                    socket = Socket(host, port)
                } catch (e: IOException) {
                    connectionTries++
                    Log.e("Socket", "${e.localizedMessage!!}, $connectionTries tries")
                    Thread.sleep(1000)
                }
            }
            if(socket != null) {
                UserThread(socket, userName, params[0]!!).start()
            }
            return socket != null
        }

        override fun onPostExecute(result: Boolean?) {
            if(result!!) viewState.showGame() else viewState.showError("Не удалось подключиться к серверу")
        }
    }

    fun startGame(userName: String, host: String, port: Int) {
        ThreadAsyncTask(viewState, userName, host, port, 10).execute(this)
    }

    fun showError(errorMsg: String){
        viewState.showError(errorMsg)
    }

    fun textChanged(coloredText: ColoredText) {
        viewState.showColoredText(coloredText)
        if (coloredText.shouldClear) viewState.clearInput()
    }

    fun showResults(results: Results) {
        viewState.showResults(results)
    }

    fun addInputTextChangedListener(f: (String) -> Unit) {
        viewState.addInputTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = f(s.toString())

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}