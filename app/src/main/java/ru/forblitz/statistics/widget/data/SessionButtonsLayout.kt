package ru.forblitz.statistics.widget.data

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import ru.forblitz.statistics.R
import ru.forblitz.statistics.utils.InterfaceUtils
import ru.forblitz.statistics.widget.common.ExtendedButton

class SessionButtonsLayout : LinearLayout {

    val statisticsButton: ExtendedButton
        get() = findViewWithTag("statistics_session_switch")
    private val listButton: ExtendedButton
        get() = findViewWithTag("statistics_sessions_list")

    val actions: Actions = Actions()

    private var inactiveStatisticsButtonAction: (() -> Unit) = {
        InterfaceUtils.createAlertDialog(
            context,
            context.getString(R.string.sessions_alert_title),
            context.getString(R.string.sessions_statistics_alert_text),
        ).show()
    }
    private var inactiveListButtonAction: (() -> Unit) = {
        InterfaceUtils.createAlertDialog(
            context,
            context.getString(R.string.sessions_alert_title),
            context.getString(R.string.sessions_list_alert_text),
        ).show()
    }

    private val activeButtonStyle: (ExtendedButton) -> Unit = {
        it.background = AppCompatResources.getDrawable(
            context,
            R.drawable.background_sessions_true
        )
        it.setTextColor(context.getColor(R.color.white))
        it.setPadding(
            context.resources.getDimensionPixelSize(R.dimen.padding_very_big),
            context.resources.getDimensionPixelSize(R.dimen.padding_very_big),
            context.resources.getDimensionPixelSize(R.dimen.padding_very_big),
            context.resources.getDimensionPixelSize(R.dimen.padding_very_big)
        )
    }
    private val inactiveButtonStyle: (ExtendedButton) -> Unit = {
        it.background = AppCompatResources.getDrawable(
            context,
            R.drawable.background_layout_nested_selected
        )
        it.setTextColor(context.getColor(R.color.transparent_white))
        it.setPadding(
            context.resources.getDimensionPixelSize(R.dimen.padding_very_big),
            context.resources.getDimensionPixelSize(R.dimen.padding_very_big),
            context.resources.getDimensionPixelSize(R.dimen.padding_very_big),
            context.resources.getDimensionPixelSize(R.dimen.padding_very_big)
        )
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setButtonsVisibility(variant: ButtonsVisibility) {
        if (variant == ButtonsVisibility.NOTHING) {
            statisticsButton.apply(inactiveButtonStyle)
                .setOnClickListener { inactiveStatisticsButtonAction.invoke() }
            listButton.apply(inactiveButtonStyle)
                .setOnClickListener { inactiveListButtonAction.invoke() }
        }
        if (variant == ButtonsVisibility.ONLY_FLIP) {
            statisticsButton.apply(activeButtonStyle)
                .setOnClickListener { actions.statisticsButtonAction?.invoke(statisticsButton) }
            listButton.apply(inactiveButtonStyle)
                .setOnClickListener { inactiveListButtonAction.invoke() }
        }
        if (variant == ButtonsVisibility.ALL) {
            statisticsButton.apply(activeButtonStyle)
                .setOnClickListener { actions.statisticsButtonAction?.invoke(statisticsButton) }
            listButton.apply(activeButtonStyle)
                .setOnClickListener { actions.listButtonAction?.invoke(listButton) }
        }
    }

    enum class ButtonsVisibility {
        NOTHING,
        ONLY_FLIP,
        ALL
    }

    class Actions {
        var statisticsButtonAction: ((View) -> Unit)? = null
        var listButtonAction: ((View) -> Unit)? = null
    }

}
