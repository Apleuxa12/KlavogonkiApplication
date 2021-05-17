package hse.org.ddmukhin.klavogonkiapplication.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.SpannableString
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import dagger.Component
import hse.org.ddmukhin.klavogonkiapplication.R
import hse.org.ddmukhin.klavogonkiapplication.databinding.ActivityGameBinding
import hse.org.ddmukhin.klavogonkiapplication.remote.threads.UserThread
import hse.org.ddmukhin.klavogonkiapplication.presenters.GamePresenter
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Color
import hse.org.ddmukhin.klavogonkiapplication.remote.data.ColoredText
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Results
import hse.org.ddmukhin.klavogonkiapplication.views.GameView
import kotlinx.coroutines.*
import moxy.MvpActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.doAsync
import java.net.Socket
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
        binding.loading.visibility = View.VISIBLE
        binding.theme.visibility = View.GONE
        binding.text.visibility = View.GONE
        binding.input.visibility = View.GONE
        binding.error.visibility = View.GONE
        binding.exitButton.visibility = View.GONE
        binding.results.visibility = View.GONE
    }

    override fun showGame() {
        binding.loading.visibility = View.GONE
        binding.theme.visibility = View.VISIBLE
        binding.text.visibility = View.VISIBLE
        binding.input.visibility = View.VISIBLE
        binding.error.visibility = View.GONE
        binding.exitButton.visibility = View.GONE
        binding.results.visibility = View.GONE
    }

    override fun showError(errorMsg: String) {
        binding.loading.visibility = View.GONE
        binding.theme.visibility = View.GONE
        binding.text.visibility = View.GONE
        binding.input.visibility = View.GONE
        binding.error.visibility = View.VISIBLE
        binding.error.text = errorMsg
        binding.exitButton.visibility = View.VISIBLE
        binding.results.visibility = View.GONE
    }

    override fun showColoredText(coloredText: ColoredText) {
        binding.loading.visibility = View.GONE
        binding.theme.visibility = View.VISIBLE
        binding.text.visibility = View.VISIBLE
        binding.input.visibility = View.VISIBLE
        binding.error.visibility = View.GONE
        binding.exitButton.visibility = View.GONE
        binding.results.visibility = View.GONE

        binding.theme.text = coloredText.text?.theme ?: ""
        val text = coloredText.text?.value ?: ""
        var coloredString = ""
        val colors = coloredText.colors
        text.forEachIndexed{ i, letter ->
            coloredString += getColoredSpanned(letter.toString(), colors[i])
        }
        binding.text.text = coloredString
    }

    override fun showResults(results: Results) {
        binding.loading.visibility = View.GONE
        binding.theme.visibility = View.GONE
        binding.text.visibility = View.GONE
        binding.input.visibility = View.GONE
        binding.error.visibility = View.GONE
        binding.exitButton.visibility = View.GONE
        binding.results.visibility = View.VISIBLE
    }

    override fun addInputTextChangedListener(textWatcher: TextWatcher) {
        binding.input.addTextChangedListener(textWatcher)
    }

    override fun clearInput() {
        binding.input.text.clear()
    }

    private fun getColoredSpanned(text: String, color: Color): String{
        return "<font color=${color.stringColor}>$text</font>"
    }
}
