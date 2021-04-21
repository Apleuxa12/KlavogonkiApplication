package hse.org.ddmukhin.klavogonkiapplication.views

import android.text.TextWatcher
import hse.org.ddmukhin.klavogonkiapplication.remote.data.ColoredText
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Results
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

interface GameView : MvpView {

    @StateStrategyType(AddToEndStrategy::class)
    fun showColoredText(coloredText: ColoredText)

    @StateStrategyType(AddToEndStrategy::class)
    fun finishGame()

    @StateStrategyType(AddToEndStrategy::class)
    fun addResults(results: Results)

    @StateStrategyType(AddToEndStrategy::class)
    fun addInputTextChangedListener(textWatcher: TextWatcher)
}