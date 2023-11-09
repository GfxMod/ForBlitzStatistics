package ru.forblitz.statistics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.forblitz.statistics.R
import ru.forblitz.statistics.dto.AchievementInfo


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
        return (convertView
            ?: LayoutInflater
                .from(context)
                .inflate(R.layout.item_achievements_row, parent, false))
            .apply {
                (this as RecyclerView)
                    .apply {

                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            rowHeight
                        )

                        if (convertView == null) {
                            addItemDecoration(
                                DividerItemDecoration(
                                    context,
                                    RecyclerView.HORIZONTAL
                                ).apply {
                                    ContextCompat.getDrawable(context, R.drawable.achievements_row_divider)
                                        ?.let {
                                            setDrawable(it)
                                        }
                                }
                            )
                        }

                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(
                            context,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        adapter = AchievementsRowAdapter(
                            achievementsRows[position],
                            rowHeight
                        )

                    }
            }

    }

}