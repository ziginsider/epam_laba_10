package io.github.ziginsider.epam_laba_10.utils

import android.graphics.Color
import java.util.*

/**
 * Extension for Color. Scales bitmap to new width and height
 */
fun randomColor(): Int {
    val rnd = Random()
    return Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
}
