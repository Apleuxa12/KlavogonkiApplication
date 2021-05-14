package hse.org.ddmukhin.klavogonkiapplication.remote.data

data class ColoredText(val text: Text?, val colors: ArrayList<Color>, val shouldClear: Boolean) {

    val isFinished: Boolean
        get(){
            if(colors.isEmpty())
                return false

            for(color in colors){
                if(color == Color.NEUTRAL || color == Color.WRONG)
                    return false
            }
            return true
        }
}