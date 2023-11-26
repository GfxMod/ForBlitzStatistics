package ru.forblitz.statistics.utils

import android.content.Context
import androidx.core.content.ContextCompat
import ru.forblitz.statistics.R

object VehicleUtils {

    @JvmStatic
    fun getBattlesColor(context: Context?, battles: Int): Int {
        val battlesColor: Int = if (battles < 100) {
            ContextCompat.getColor(context!!, R.color.white)
        } else if (battles < 250) {
            ContextCompat.getColor(context!!, R.color.green)
        } else if (battles < 500) {
            ContextCompat.getColor(context!!, R.color.blue)
        } else {
            ContextCompat.getColor(context!!, R.color.violet)
        }
        return battlesColor
    }

    @JvmStatic
    fun getAverageDamageColor(context: Context?, averageDamage: Double, tier: Int): Int {
        val averageDamageColor: Int = if (tier == 10) {
            if (averageDamage < 2000) {
                ContextCompat.getColor(context!!, R.color.white)
            } else if (averageDamage < 2700) {
                ContextCompat.getColor(context!!, R.color.green)
            } else if (averageDamage < 3300) {
                ContextCompat.getColor(context!!, R.color.blue)
            } else {
                ContextCompat.getColor(context!!, R.color.violet)
            }
        } else if (tier == 9) {
            if (averageDamage < 1700) {
                ContextCompat.getColor(context!!, R.color.white)
            } else if (averageDamage < 2200) {
                ContextCompat.getColor(context!!, R.color.green)
            } else if (averageDamage < 2600) {
                ContextCompat.getColor(context!!, R.color.blue)
            } else {
                ContextCompat.getColor(context!!, R.color.violet)
            }
        } else if (tier == 8) {
            if (averageDamage < 1300) {
                ContextCompat.getColor(context!!, R.color.white)
            } else if (averageDamage < 1800) {
                ContextCompat.getColor(context!!, R.color.green)
            } else if (averageDamage < 2200) {
                ContextCompat.getColor(context!!, R.color.blue)
            } else {
                ContextCompat.getColor(context!!, R.color.violet)
            }
        } else {
            ContextCompat.getColor(context!!, R.color.white)
        }
        return averageDamageColor
    }

    @JvmStatic
    fun getWinningPercentageColor(context: Context?, winningPercentage: Double): Int {
        val winningPercentageColor: Int = if (winningPercentage < 50) {
            ContextCompat.getColor(context!!, R.color.white)
        } else if (winningPercentage < 60) {
            ContextCompat.getColor(context!!, R.color.green)
        } else if (winningPercentage < 70) {
            ContextCompat.getColor(context!!, R.color.blue)
        } else {
            ContextCompat.getColor(context!!, R.color.violet)
        }
        return winningPercentageColor
    }

    @JvmStatic
    fun getEfficiencyColor(context: Context?, efficiency: Double): Int {
        val efficiencyColor: Int = if (efficiency < 1) {
            ContextCompat.getColor(context!!, R.color.white)
        } else if (efficiency < 1.5) {
            ContextCompat.getColor(context!!, R.color.green)
        } else if (efficiency < 2) {
            ContextCompat.getColor(context!!, R.color.blue)
        } else {
            ContextCompat.getColor(context!!, R.color.violet)
        }
        return efficiencyColor
    }

}