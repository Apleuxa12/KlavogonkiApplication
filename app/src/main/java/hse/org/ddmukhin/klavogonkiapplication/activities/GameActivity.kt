package hse.org.ddmukhin.klavogonkiapplication.activities

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import hse.org.ddmukhin.klavogonkiapplication.R
import hse.org.ddmukhin.klavogonkiapplication.databinding.ActivityGameBinding
import hse.org.ddmukhin.klavogonkiapplication.remote.threads.UserThread
import hse.org.ddmukhin.klavogonkiapplication.presenters.GamePresenter
import hse.org.ddmukhin.klavogonkiapplication.remote.data.ColoredText
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Results
import hse.org.ddmukhin.klavogonkiapplication.views.GameView
import moxy.MvpActivity
import moxy.presenter.InjectPresenter
import java.net.Socket

class GameActivity : MvpActivity(), GameView {

    @InjectPresenter
    lateinit var presenter: GamePresenter

    private lateinit var inputField: EditText

    private val host  = getString(R.string.host)

    private val port = resources.getInteger(R.integer.port)

    private val userThread = UserThread(Socket(host, port), "userName", presenter)

    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
//      init UI

        userThread.start()
    }

    override fun showColoredText(coloredText: ColoredText) {
//      show colored text
    }

    override fun finishGame() {
//      hide input, show results
    }

    override fun addResults(results: Results) {
//      add results to results list
    }

    override fun addInputTextChangedListener(textWatcher: TextWatcher) {
        inputField.addTextChangedListener(textWatcher)
    }
}
