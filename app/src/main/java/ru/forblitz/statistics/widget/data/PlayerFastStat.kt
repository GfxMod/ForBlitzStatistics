package ru.forblitz.statistics.widget.data

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import ru.forblitz.statistics.R
import ru.forblitz.statistics.data.Constants
import ru.forblitz.statistics.dto.StatisticsDataModern
import ru.forblitz.statistics.utils.InterfaceUtils
import ru.forblitz.statistics.utils.ParseUtils

class PlayerFastStat : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setData(nickname: String, statisticsDataModern: StatisticsDataModern) {
        val battles = findViewWithTag<TextView>("battles")
        val winRate = findViewWithTag<TextView>("winRate")
        val averageDamage = findViewWithTag<TextView>("averageDamage")
        val efficiency = findViewWithTag<TextView>("efficiency")
        val survived = findViewWithTag<TextView>("survived")
        val hitsFromShots = findViewWithTag<TextView>("hitsFromShots")
        val battlesValue = statisticsDataModern.battles.toString()
        var winRateValue = statisticsDataModern.winningPercentage.toString()
        var averageDamageValue = statisticsDataModern.averageDamage.toString()
        var efficiencyValue = statisticsDataModern.efficiency.toString()
        var survivedValue = statisticsDataModern.survivedPercentage.toString()
        var hitsFromShotsValue = statisticsDataModern.hitsOutOfShots.toString()
        var winRateColor = InterfaceUtils.getValueColor(
            this.context,
            statisticsDataModern.winningPercentage!!.toDouble(),
            Constants.Steps.winRate
        )
        var averageDamageColor = InterfaceUtils.getValueColor(
            this.context,
            statisticsDataModern.averageDamage!!.toDouble(),
            Constants.Steps.averageDamage
        )
        var efficiencyColor = InterfaceUtils.getValueColor(
            this.context,
            statisticsDataModern.efficiency!!.toDouble(),
            Constants.Steps.efficiency
        )
        if (winRateValue == "NaN") {
            winRateValue = context.getString(R.string.empty)
            winRateColor = this.context.getColor(R.color.white)
        }
        if (averageDamageValue == "NaN") {
            averageDamageValue = context.getString(R.string.empty)
            averageDamageColor = this.context.getColor(R.color.white)
        }
        if (efficiencyValue == "NaN") {
            efficiencyValue = context.getString(R.string.empty)
            efficiencyColor = this.context.getColor(R.color.white)
        }
        if (survivedValue == "NaN") {
            survivedValue = context.getString(R.string.empty)
        }
        if (hitsFromShotsValue == "NaN") {
            hitsFromShotsValue = context.getString(R.string.empty)
        }
        battles.text = ParseUtils.splitByThousands(battlesValue)
        winRate.text = winRateValue
        averageDamage.text = ParseUtils.splitByThousands(averageDamageValue)
        efficiency.text = efficiencyValue
        survived.text = survivedValue
        hitsFromShots.text = hitsFromShotsValue
        battles.setTextColor(
            InterfaceUtils.getValueColor(
                this.context,
                statisticsDataModern.battles.toDouble(),
                Constants.Steps.battles
            )
        )
        winRate.setTextColor(winRateColor)
        averageDamage.setTextColor(averageDamageColor)
        efficiency.setTextColor(efficiencyColor)
        findViewWithTag<TextView>("nickname").text = nickname
    }

    fun setSessionData(statisticsDataModern: StatisticsDataModern) {
        var battlesDiff: String = statisticsDataModern.battles.toString()
        var winRateDiff: String = statisticsDataModern.winningPercentage.toString()
        var averageDamageDiff: String = statisticsDataModern.averageDamage.toString()
        var efficiencyDiff: String = statisticsDataModern.efficiency.toString()
        var survivedDiff: String = statisticsDataModern.survivedPercentage.toString()
        var hitsFromShotsDiff: String = statisticsDataModern.hitsOutOfShots.toString()
        battlesDiff = "+$battlesDiff"
        if (winRateDiff.toDouble() > 0) {
            winRateDiff = "+$winRateDiff"
        }
        winRateDiff = "$winRateDiff%"
        if (averageDamageDiff.toDouble() > 0) {
            averageDamageDiff = "+$averageDamageDiff"
        }
        if (efficiencyDiff.toDouble() > 0) {
            efficiencyDiff = "+$efficiencyDiff"
        }
        if (survivedDiff.toDouble() > 0) {
            survivedDiff = "+$survivedDiff"
        }
        survivedDiff = "$survivedDiff%"
        if (hitsFromShotsDiff.toDouble() > 0) {
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