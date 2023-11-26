package ru.forblitz.statistics.widget.data

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
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

    @SuppressLint("SetTextI18n")
    fun setData(nickname: String, statisticsData: StatisticsData) {
        with(findViewWithTag<TextView>("battles")) {
            text = ParseUtils.splitByThousands(statisticsData.battles.toString())
            setTextColor(
                InterfaceUtils.getValueColor(
                    this.context,
                    statisticsData.battles.toDouble(),
                    Constants.Steps.battles
                )
            )
        }
        with(findViewWithTag<TextView>("winRate")) {
            if (statisticsData.winningPercentage!!.isNaN()) {
                text = context.getString(R.string.empty)
                setTextColor(context.getColor(R.color.white))
            } else {
                text = "${statisticsData.winningPercentage!!.round()}%"
                setTextColor(InterfaceUtils.getValueColor(
                    context,
                    statisticsData.winningPercentage!!.toDouble(),
                    Constants.Steps.winRate
                ))
            }
        }
        with(findViewWithTag<TextView>("averageDamage")) {
            if (statisticsData.averageDamage!!.isNaN()) {
                text = context.getString(R.string.empty)
                setTextColor(context.getColor(R.color.white))
            } else {
                text = ParseUtils.splitByThousands(
                    if (!averageDamageRounding) {
                        statisticsData.averageDamage!!.toInt().toString()
                    } else {
                        statisticsData.averageDamage!!.round().toString()
                    }
                )
                setTextColor(InterfaceUtils.getValueColor(
                    this.context,
                    statisticsData.averageDamage!!.toDouble(),
                    Constants.Steps.averageDamage
                ))
            }
        }
        with(findViewWithTag<TextView>("efficiency")) {
            if (statisticsData.efficiency!!.isNaN()) {
                text = context.getString(R.string.empty)
                setTextColor(context.getColor(R.color.white))
            } else {
                text = statisticsData.efficiency!!.round().toString()
                setTextColor(InterfaceUtils.getValueColor(
                    this.context,
                    statisticsData.efficiency!!.toDouble(),
                    Constants.Steps.efficiency
                ))
            }
        }
        with(findViewWithTag<TextView>("survived")) {
            text = if (statisticsData.survivedPercentage!!.isNaN()) {
                context.getString(R.string.empty)
            } else {
                statisticsData.survivedPercentage!!.round().toString()
            }
        }
        with(findViewWithTag<TextView>("hitsFromShots")) {
            text = if (statisticsData.hitsOutOfShots!!.isNaN()) {
                context.getString(R.string.empty)
            } else {
                statisticsData.hitsOutOfShots!!.round().toString()
            }
        }
        findViewWithTag<TextView>("nickname").text = nickname
    }

    fun setSessionData(statisticsData: StatisticsData) {
        (findViewWithTag<View>("battlesDiff") as DifferenceIndicatorView).setValue(
            statisticsData.battles,
            false
        )
        (findViewWithTag<View>("winRateDiff") as DifferenceIndicatorView).setValue(
            statisticsData.winningPercentage!!,
            true
        )
        (findViewWithTag<View>("averageDamageDiff") as DifferenceIndicatorView).setValue(
            if (!averageDamageRounding) {
                statisticsData.averageDamage!!.toInt().toFloat()
            } else {
                statisticsData.averageDamage!!
            },
            false
        )
        (findViewWithTag<View>("efficiencyDiff") as DifferenceIndicatorView).setValue(
            statisticsData.efficiency!!,
            false
        )
        (findViewWithTag<View>("survivedDiff") as DifferenceIndicatorView).setValue(
            statisticsData.survivedPercentage!!,
            true
        )
        (findViewWithTag<View>("hitsFromShotsDiff") as DifferenceIndicatorView).setValue(
            statisticsData.hitsOutOfShots!!,
            true
        )
    }
}