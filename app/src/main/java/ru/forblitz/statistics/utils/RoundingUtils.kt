package ru.forblitz.statistics.utils

import java.math.RoundingMode

class RoundingUtils {
    companion object {

        fun Number.round(newScale: Int = 2): Double {
            return this.toDouble().toBigDecimal().setScale(newScale, RoundingMode.HALF_EVEN).toDouble()
        }

    }
}