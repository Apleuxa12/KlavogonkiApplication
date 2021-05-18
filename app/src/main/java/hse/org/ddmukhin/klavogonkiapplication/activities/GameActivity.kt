package hse.org.ddmukhin.klavogonkiapplication.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.text.HtmlCompat
import dagger.Component
import hse.org.ddmukhin.klavogonkiapplication.R
import hse.org.ddmukhin.klavogonkiapplication.databinding.ActivityGameBinding
import hse.org.ddmukhin.klavogonkiapplication.remote.threads.UserThread
import hse.org.ddmukhin.klavogonkiapplication.presenters.GamePresenter
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Color
import hse.org.ddmukhin.klavogonkiapplication.remote.data.ColoredText
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Results
import hse.org.ddmukhin.klavogonkiapplication.utils.set
import hse.org.ddmukhin.klavogonkiapplication.views.GameView
import kotlinx.coroutines.*
import moxy.MvpActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.doAsync
import java.net.Socket
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class GameActivity : MvpActivity(), GameView {

    @InjectPresenter
    lateinit var presenter: GamePresenter

    private lateinit var binding: ActivityGameBinding

    private lateinit var host: String

    private var port = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        host = getString(R.string.host)
        port = resources.getInteger(R.integer.port)

        binding.exitButton.setOnClickListener {
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        val username = intent.extras?.get("username")

        presenter.startGame(username.toString(), host, port)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun showLoading() {
        runOnUiThread {
            binding.loading.visibility = View.VISIBLE
            binding.theme.visibility = View.GONE
            binding.text.visibility = View.GONE
            binding.input.visibility = View.GONE
            binding.error.visibility = View.GONE
            binding.exitButton.visibility = View.GONE
            binding.results.visibility = View.GONE
        }
    }

    override fun showGame() {
        runOnUiThread {
            binding.loading.visibility = View.GONE
            binding.theme.visibility = View.VISIBLE
            binding.text.visibility = View.VISIBLE
            binding.input.visibility = View.VISIBLE
            binding.error.visibility = View.GONE
            binding.exitButton.visibility = View.GONE
            binding.results.visibility = View.GONE
        }
    }

    override fun showError(errorMsg: String) {
        runOnUiThread {
            binding.loading.visibility = View.GONE
            binding.theme.visibility = View.GONE
            binding.text.visibility = View.GONE
            binding.input.visibility = View.GONE
            binding.error.visibility = View.VISIBLE
            binding.error.text = errorMsg
            binding.exitButton.visibility = View.VISIBLE
            binding.results.visibility = View.GONE
        }
    }

    override fun showColoredText(coloredText: ColoredText) {
        runOnUiThread {
            binding.loading.visibility = View.GONE
            binding.theme.visibility = View.VISIBLE
            binding.text.visibility = View.VISIBLE
            binding.input.visibility = View.VISIBLE
            binding.error.visibility = View.GONE
            binding.exitButton.visibility = View.GONE
            binding.results.visibility = View.GONE
            binding.theme.text = coloredText.text?.theme ?: ""
            val text = coloredText.text?.value ?: ""
            val colors = coloredText.colors
            val span = SpannableString(text)
            colors.forEachIndexed{ i, color ->
                span[i] = color
            }
            binding.text.text = span
        }
    }

    override fun showResults(results: Results) {
        runOnUiThread {
            binding.loading.visibility = View.GONE
            binding.theme.visibility = View.GONE
            binding.text.visibility = View.GONE
            binding.input.visibility = View.GONE
            binding.error.visibility = View.GONE
            binding.exitButton.visibility = View.VISIBLE
            binding.results.visibility = View.VISIBLE
//            binding.results.text = "Результаты: время - ${results.time.toInt()}, скорость - ${results.speed.toInt()}"
            binding.results.text = String.format(
                getString(R.string.resultPlaceholder),
                convertTime(results.time), convertSpeed(results.speed)
            )
        }
    }

    override fun addInputTextChangedListener(textWatcher: TextWatcher) {
        binding.input.addTextChangedListener(textWatcher)
    }

    override fun clearInput() {
        runOnUiThread {
            binding.input.text.clear()
        }
    }

    private fun convertTime(milliSeconds: Long) : String{
        return String.format("%02d м, %02d с",
            TimeUnit.MILLISECONDS.toMinutes(milliSeconds),
        TimeUnit.MILLISECONDS.toSeconds(milliSeconds))
    }

    private fun convertSpeed(speed: Double): String{
        return String.format("%02f слов в минуту", speed)
    }
}
