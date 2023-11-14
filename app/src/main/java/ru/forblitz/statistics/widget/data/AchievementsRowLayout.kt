package ru.forblitz.statistics.widget.data

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Space
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.children
import ru.forblitz.statistics.data.Constants
import ru.forblitz.statistics.dto.AchievementInfo
import ru.forblitz.statistics.utils.Utils

class AchievementsRowLayout : LinearLayout {

    var itemSize: Int = 0
        set(value) {
            if (value >= 0) {
                field = value
            } else {
                throw IllegalArgumentException("itemSize must be >= 0")
            }
        }

    private var achievements: List<Pair<AchievementInfo, Int>>? = null

    var onAchievementClick: ((Pair<AchievementInfo, Int>) -> Unit)? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setAchievements(achievements: List<Pair<AchievementInfo, Int>>?) {
        this.achievements = achievements
    }

    fun refreshAchievements() {
        children.forEach { children ->
            if (children !is Space) {
                (children as ViewGroup).also {
                    if (indexOfChild(it) / 2 >= (if (achievements != null) { achievements!!.size } else { 0 })) {
                        it.visibility = GONE
                    } else {
                        it.visibility = VISIBLE

                        it.layoutParams = LayoutParams(
                            itemSize,
                            itemSize
                        )

                        val achievementPair = achievements?.get(indexOfChild(it) / 2)!!
                        it.findViewWithTag<AppCompatTextView>("count")?.text = achievementPair.second.toString()

                        it.findViewWithTag<AppCompatImageButton>("icon")?.apply {

                            val resourceName = "${Constants.achievementIconNamePrefix}${achievementPair.first.achievementId.lowercase()}"
                            if (Utils.isResourceExists(context, resourceName, Utils.ResourceType.drawable)) {
                                setImageDrawable(Utils.getDrawableResourceByName(context, resourceName))
                            } else if (Constants.achievementsIcons.containsKey(achievementPair.first.achievementId)) {
                                setImageDrawable(
                                    Utils.getDrawableResourceByName(
                                        context,
                                        Constants.achievementsIcons[achievementPair.first.achievementId]!!
                                    )
                                )
                            }

                            setOnClickListener {
                                onAchievementClick?.invoke(achievementPair)
                            }

                        }
                        it.contentDescription = achievementPair.first.name
                    }
                }
            }
        }
    }

}