package ru.forblitz.statistics.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import ru.forblitz.statistics.R
import ru.forblitz.statistics.data.Constants.achievementIconNamePrefix
import ru.forblitz.statistics.data.Constants.achievementsIcons
import ru.forblitz.statistics.dto.AchievementInfo
import ru.forblitz.statistics.utils.Utils
import ru.forblitz.statistics.utils.Utils.getDrawableResourceByName
import ru.forblitz.statistics.utils.Utils.isResourceExists

class AchievementsRowAdapter(
    private val achievements: List<Pair<AchievementInfo, Int>>,
    private val itemSize: Int
) : RecyclerView.Adapter<AchievementsRowAdapter.AchievementsRowViewHolder>() {

    class AchievementsRowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val achievementIcon = itemView.findViewById(R.id.achievement_icon) as AppCompatImageButton
        val achievementCount = itemView.findViewById(R.id.achievement_count) as AppCompatTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementsRowViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_achievement, parent, false)
        return AchievementsRowViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AchievementsRowViewHolder, position: Int) {
        val achievementPair = achievements[position]
        with (holder.achievementIcon) {
            layoutParams = RecyclerView.LayoutParams(
                itemSize,
                itemSize
            )
            val resourceName = "$achievementIconNamePrefix${achievementPair.first.achievementId.lowercase()}"
            if (isResourceExists(context, resourceName, Utils.ResourceType.drawable)) {
                setImageDrawable(getDrawableResourceByName(context, resourceName))
            } else if (achievementsIcons.containsKey(achievementPair.first.achievementId)) {
                setImageDrawable(getDrawableResourceByName(context, achievementsIcons[achievementPair.first.achievementId]!!))
            }

            setOnClickListener {
                onClick(this@with, achievementPair)
            }
        }
        holder.achievementCount.text = achievementPair.second.toString()

    }
    
    private fun onClick(view: View, achievementPair: Pair<AchievementInfo, Int>) {
        Toast.makeText(view.context, achievementPair.first.name, Toast.LENGTH_LONG).show()
    }

    override fun getItemCount() = achievements.size

}