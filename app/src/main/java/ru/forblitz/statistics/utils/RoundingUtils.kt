package ru.forblitz.statistics.utils

import java.math.RoundingMode

class RoundingUtils {
    companion object {

        const val NEW_SCALE = 2

        fun Float.round(newScale: Int = NEW_SCALE): Float {
            return this.toBigDecimal().setScale(newScale, RoundingMode.HALF_EVEN).toFloat()
        }

    }
}