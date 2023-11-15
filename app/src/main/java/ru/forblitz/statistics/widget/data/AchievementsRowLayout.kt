package ru.forblitz.statistics.widget.data

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.TransitionDrawable
import android.text.Html
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Space
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.children
import ru.forblitz.statistics.R
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

    var paddingSize: Int = 0
        set(value) {
            if (value >= 0) {
                field = value
            } else {
                throw IllegalArgumentException("paddingSize must be >= 0")
            }
        }

    private val expandedHeight: Int
        get() = itemSize * 3 + paddingSize

    private var achievements: List<Pair<AchievementInfo, Int>>? = null

    var onAchievementClick: ((AchievementsRowLayout, ViewGroup, Pair<AchievementInfo, Int>) -> Unit)? = null

    var expandedChild: Int = -1
        private set

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setAchievements(achievements: List<Pair<AchievementInfo, Int>>?) {
        this.achievements = achievements
    }

    fun refreshAchievements() {
        with(findViewWithTag<LinearLayout>("row")) {
            children.forEach { children ->
                if (children !is Space) {
                    (children as ViewGroup).also { viewGroup ->
                        if (indexOfChild(viewGroup) / 2 >= (if (achievements != null) { achievements!!.size } else { 0 })) {
                            viewGroup.visibility = GONE
                        } else {
                            viewGroup.visibility = VISIBLE
                            viewGroup.background =
                                AppCompatResources.getDrawable(
                                    context,
                                    R.drawable.background_layout_nested
                                )


                            viewGroup.layoutParams = LayoutParams(
                                itemSize,
                                itemSize
                            )

                            val achievementPair = achievements?.get(indexOfChild(viewGroup) / 2)!!

                            with(viewGroup.findViewWithTag<AppCompatTextView>("count")) {
                                if (achievementPair.first.section != "step") {
                                    visibility = VISIBLE
                                    text = achievementPair.second.toString()
                                } else {
                                    visibility = GONE
                                }
                            }
                            viewGroup.findViewWithTag<AppCompatImageButton>("icon")?.apply imageButton@ {

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
                                    onAchievementClick?.invoke(this@AchievementsRowLayout, viewGroup, achievementPair)
                                }

                            }
                            viewGroup.contentDescription = achievementPair.first.name
                        }
                    }
                }
            }
        }
    }

    fun expand(viewGroup: ViewGroup, achievementInfo: AchievementInfo) {
        findViewWithTag<AppCompatTextView>("description").also {
            it.text = Html.fromHtml(
                "<h3>${achievementInfo.name.substringBefore("(")}</h3><br>${achievementInfo.description}",
                Html.FROM_HTML_MODE_COMPACT
            )

            with(ValueAnimator.ofInt(it.layoutParams.height, expandedHeight - paddingSize - itemSize)) {
                addUpdateListener { valueAnimator ->
                    val value = valueAnimator.animatedValue as Int
                    it.layoutParams.height = value
                    it.requestLayout()
                }
                duration =
                    context.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
                start()
            }
        }

        with(ValueAnimator.ofInt(layoutParams.height, expandedHeight)) {
            addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Int
                this@AchievementsRowLayout.layoutParams.height = value
                this@AchievementsRowLayout.requestLayout()
            }
            duration =
                context.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
            start()
        }

        viewGroup.background = TransitionDrawable(arrayOf(
            AppCompatResources.getDrawable(context, R.drawable.background_layout_nested),
            AppCompatResources.getDrawable(context, R.drawable.background_layout_nested_selected)
        )).apply {
            isCrossFadeEnabled = true
            startTransition(
                context.resources.getInteger(android.R.integer.config_shortAnimTime)
            )
        }

        expandedChild = achievements!!.indexOf(
            achievements!!.find { it.first == achievementInfo }!!
        ) * 2
    }

    fun collapse(viewGroup: ViewGroup) {
        findViewWithTag<AppCompatTextView>("description").also {
            with(ValueAnimator.ofInt(it.layoutParams.height, 0)) {
                addUpdateListener { valueAnimator ->
                    val value = valueAnimator.animatedValue as Int
                    it.layoutParams.height = value
                    it.requestLayout()
                }
                duration =
                    context.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
                start()
            }
        }

        with(ValueAnimator.ofInt(layoutParams.height, itemSize)) {
            addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Int
                this@AchievementsRowLayout.layoutParams.height = value
                this@AchievementsRowLayout.requestLayout()
            }
            duration =
                context.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
            start()
        }

        viewGroup.background = TransitionDrawable(arrayOf(
            AppCompatResources.getDrawable(context, R.drawable.background_layout_nested_selected),
            AppCompatResources.getDrawable(context, R.drawable.background_layout_nested)
        )).apply {
            isCrossFadeEnabled = true
            startTransition(
                context.resources.getInteger(android.R.integer.config_shortAnimTime)
            )
        }

        expandedChild = -1
    }

    fun resetAnimations() {
        if (layoutParams.height != itemSize) {
            layoutParams.height = itemSize
            requestLayout()
        }

        expandedChild = -1
    }

}