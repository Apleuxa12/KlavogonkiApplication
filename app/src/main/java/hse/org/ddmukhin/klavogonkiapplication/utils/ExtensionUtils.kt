package hse.org.ddmukhin.klavogonkiapplication.utils

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import hse.org.ddmukhin.klavogonkiapplication.remote.data.Color

operator fun SpannableString.set(i: Int, color: Color) {
    if (i < this.length)
        this.setSpan(
            ForegroundColorSpan(color.intColor),
            i,
            i + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    else
        this.setSpan(
            ForegroundColorSpan(color.intColor),
            this.length,
            this.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
}