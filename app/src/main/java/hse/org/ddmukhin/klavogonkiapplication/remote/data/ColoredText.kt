package hse.org.ddmukhin.klavogonkiapplication.remote.data

data class ColoredText(val text: Text?, val colors: Array<Color>) {

    val isFinished: Boolean
        get(){
            for(color in colors){
                if(color == Color.NEUTRAL || color == Color.WRONG)
                    return false
            }
            return true
        }
}