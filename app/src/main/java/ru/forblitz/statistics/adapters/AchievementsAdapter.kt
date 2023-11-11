package ru.forblitz.statistics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import ru.forblitz.statistics.R
import ru.forblitz.statistics.dto.AchievementInfo
import ru.forblitz.statistics.widget.data.AchievementsRowLayout


class AchievementsAdapter(
    context: Context,
    private val achievementsRows: List<List<Pair<AchievementInfo, Int>>>,
    private val rowHeight: Int,
) :
    ArrayAdapter<List<Pair<AchievementInfo, Int>>>(
        context,
        R.layout.item_achievements_row,
        achievementsRows
    )
{

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return convertView
            ?:
            LayoutInflater
                .from(context)
                .inflate(R.layout.item_achievements_row, parent, false)
                .apply {
                    (this as AchievementsRowLayout)
                        .apply {
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                rowHeight
                            )
                            itemSize = rowHeight
                            setAchievements(achievementsRows[position])
                            refreshAchievements()
                        }
            }
    }

}