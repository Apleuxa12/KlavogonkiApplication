package hse.org.ddmukhin.klavogonkiapplication.views

import android.text.TextWatcher
import hse.org.ddmukhin.klavogonkiapplication.remote.data.ColoredText
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Results
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Text
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

interface GameView : MvpView {

    @StateStrategyType(AddToEndStrategy::class)
    fun showLoading()

    @StateStrategyType(AddToEndStrategy::class)
    fun showGame()

    @StateStrategyType(AddToEndStrategy::class)
    fun showError(errorMsg: String)

    @StateStrategyType(AddToEndStrategy::class)
    fun showColoredText(coloredText: ColoredText)

    @StateStrategyType(AddToEndStrategy::class)
    fun showResults(results: Results)

    @StateStrategyType(AddToEndStrategy::class)
    fun addInputTextChangedListener(textWatcher: TextWatcher)

    @StateStrategyType(AddToEndStrategy::class)
    fun clearInput()
}