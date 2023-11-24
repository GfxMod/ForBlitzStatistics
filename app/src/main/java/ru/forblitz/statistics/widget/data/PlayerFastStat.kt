package ru.forblitz.statistics.widget.data

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import ru.forblitz.statistics.ForBlitzStatisticsApplication
import ru.forblitz.statistics.R
import ru.forblitz.statistics.data.Constants
import ru.forblitz.statistics.data.Constants.PreferencesSwitchesTags
import ru.forblitz.statistics.dto.StatisticsData
import ru.forblitz.statistics.utils.InterfaceUtils
import ru.forblitz.statistics.utils.ParseUtils
import ru.forblitz.statistics.utils.RoundingUtils.Companion.round

class PlayerFastStat : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val averageDamageRounding: Boolean
        get() = (context.applicationContext as ForBlitzStatisticsApplication).preferencesService.get(PreferencesSwitchesTags.averageDamageRounding)

    fun setData(nickname: String, statisticsData: StatisticsData) {
        val battles = findViewWithTag<TextView>("battles")
        val winRate = findViewWithTag<TextView>("winRate")
        val averageDamage = findViewWithTag<TextView>("averageDamage")
        val efficiency = findViewWithTag<TextView>("efficiency")
        val survived = findViewWithTag<TextView>("survived")
        val hitsFromShots = findViewWithTag<TextView>("hitsFromShots")

        val battlesValue = ParseUtils.splitByThousands(statisticsData.battles.toString())
        var winningPercentageValue = "${statisticsData.winningPercentage!!.round()}%"
        var averageDamageValue = ParseUtils.splitByThousands(
            if (!averageDamageRounding) {
                statisticsData.averageDamage!!.toInt().toString()
            } else {
                statisticsData.averageDamage!!.round().toString()
            }
        )
        var efficiencyValue = statisticsData.efficiency!!.round().toString()
        var survivedPercentageValue = statisticsData.survivedPercentage!!.round().toString()
        var hitsOutOfShotsValue = statisticsData.hitsOutOfShots!!.round().toString()

        var winningPercentageColor = InterfaceUtils.getValueColor(
            this.context,
            statisticsData.winningPercentage!!.toDouble(),
            Constants.Steps.winRate
        )
        var averageDamageColor = InterfaceUtils.getValueColor(
            this.context,
            statisticsData.averageDamage!!.toDouble(),
            Constants.Steps.averageDamage
        )
        var efficiencyColor = InterfaceUtils.getValueColor(
            this.context,
            statisticsData.efficiency!!.toDouble(),
            Constants.Steps.efficiency
        )

        if (statisticsData.winningPercentage!!.isNaN()) {
            winningPercentageValue = context.getString(R.string.empty)
            winningPercentageColor = this.context.getColor(R.color.white)
        }
        if (statisticsData.averageDamage!!.isNaN()) {
            averageDamageValue = context.getString(R.string.empty)
            averageDamageColor = this.context.getColor(R.color.white)
        }
        if (statisticsData.efficiency!!.isNaN()) {
            efficiencyValue = context.getString(R.string.empty)
            efficiencyColor = this.context.getColor(R.color.white)
        }
        if (statisticsData.survivedPercentage!!.isNaN()) {
            survivedPercentageValue = context.getString(R.string.empty)
        }
        if (statisticsData.hitsOutOfShots!!.isNaN()) {
            hitsOutOfShotsValue = context.getString(R.string.empty)
        }

        battles.text = battlesValue
        winRate.text = winningPercentageValue
        averageDamage.text = averageDamageValue
        efficiency.text = efficiencyValue
        survived.text = survivedPercentageValue
        hitsFromShots.text = hitsOutOfShotsValue
        battles.setTextColor(
            InterfaceUtils.getValueColor(
                this.context,
                statisticsData.battles.toDouble(),
                Constants.Steps.battles
            )
        )
        winRate.setTextColor(winningPercentageColor)
        averageDamage.setTextColor(averageDamageColor)
        efficiency.setTextColor(efficiencyColor)
        findViewWithTag<TextView>("nickname").text = nickname
    }

    fun setSessionData(statisticsData: StatisticsData) {
        var battlesDiff: String = statisticsData.battles.toString()
        var winRateDiff: String = statisticsData.winningPercentage!!.round().toString()
        var averageDamageDiff: String = if (!averageDamageRounding) {
            statisticsData.averageDamage!!.toInt().toString()
        } else {
            statisticsData.averageDamage!!.round().toString()
        }
        var efficiencyDiff: String = statisticsData.efficiency!!.round().toString()
        var survivedDiff: String = statisticsData.survivedPercentage!!.round().toString()
        var hitsFromShotsDiff: String = statisticsData.hitsOutOfShots!!.round().toString()

        battlesDiff = "+$battlesDiff"

        if (statisticsData.winningPercentage!! > 0) {
            winRateDiff = "+$winRateDiff"
        }
        winRateDiff = "$winRateDiff%"

        if (statisticsData.averageDamage!! > 0) {
            averageDamageDiff = "+$averageDamageDiff"
        }

        if (statisticsData.efficiency!! > 0) {
            efficiencyDiff = "+$efficiencyDiff"
        }

        if (statisticsData.survivedPercentage!! > 0) {
            survivedDiff = "+$survivedDiff"
        }

        survivedDiff = "$survivedDiff%"
        if (statisticsData.hitsOutOfShots!! > 0) {
            hitsFromShotsDiff = "+$hitsFromShotsDiff"
        }

        hitsFromShotsDiff = "$hitsFromShotsDiff%"

        (findViewWithTag<View>("battlesDiff") as DifferenceIndicatorView).setValue(
            battlesDiff,
            false
        )
        (findViewWithTag<View>("winRateDiff") as DifferenceIndicatorView).setValue(
            winRateDiff,
            true
        )
        (findViewWithTag<View>("averageDamageDiff") as DifferenceIndicatorView).setValue(
            averageDamageDiff,
            false
        )
        (findViewWithTag<View>("efficiencyDiff") as DifferenceIndicatorView).setValue(
            efficiencyDiff,
            false
        )
        (findViewWithTag<View>("survivedDiff") as DifferenceIndicatorView).setValue(
            survivedDiff,
            true
        )
        (findViewWithTag<View>("hitsFromShotsDiff") as DifferenceIndicatorView).setValue(
            hitsFromShotsDiff,
            true
        )
    }
}