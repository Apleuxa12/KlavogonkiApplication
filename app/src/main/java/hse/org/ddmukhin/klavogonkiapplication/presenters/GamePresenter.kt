package hse.org.ddmukhin.klavogonkiapplication.presenters

import android.text.Editable
import android.text.TextWatcher
import hse.org.ddmukhin.klavogonkiapplication.remote.data.ColoredText
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Results
import hse.org.ddmukhin.klavogonkiapplication.views.GameView
import moxy.MvpPresenter
import javax.inject.Inject

class GamePresenter @Inject constructor() : MvpPresenter<GameView>() {

    fun textChanged(coloredText: ColoredText) {
        viewState.showColoredText(coloredText)
    }

    fun gameFinished(results: Results) {
        viewState.addResults(results)
        viewState.finishGame()
    }

    fun addInputTextChangedListener(f: (String) -> Unit) {
        viewState.addInputTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = f(s.toString())

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}