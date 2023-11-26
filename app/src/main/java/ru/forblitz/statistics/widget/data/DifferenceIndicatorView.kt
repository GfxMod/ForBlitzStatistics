package ru.forblitz.statistics.widget.data

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatTextView
import ru.forblitz.statistics.R
import ru.forblitz.statistics.utils.ParseUtils
import ru.forblitz.statistics.utils.RoundingUtils.Companion.round

class DifferenceIndicatorView : AppCompatTextView {
    private var plusBackground: Drawable? = null
    private var minusBackground: Drawable? = null
    private var plusTextColor = 0
    private var minusTextColor = 0

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.DifferenceIndicatorView)
        plusBackground = a.getDrawable(R.styleable.DifferenceIndicatorView_plusBackground)
        minusBackground = a.getDrawable(R.styleable.DifferenceIndicatorView_minusBackground)
        plusTextColor = a.getColor(
            R.styleable.DifferenceIndicatorView_plusTextColor,
            context.getColor(R.color.session_green)
        )
        minusTextColor = a.getColor(
            R.styleable.DifferenceIndicatorView_minusTextColor,
            context.getColor(R.color.session_red)
        )
        if (plusBackground == null) {
            plusBackground =
                AppCompatResources.getDrawable(context, R.drawable.background_sessions_true)
        }
        if (minusBackground == null) {
            minusBackground =
                AppCompatResources.getDrawable(context, R.drawable.background_sessions_false)
        }
        a.recycle()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.DifferenceIndicatorView,
            defStyleAttr,
            0
        )
        plusBackground = a.getDrawable(R.styleable.DifferenceIndicatorView_plusBackground)
        minusBackground = a.getDrawable(R.styleable.DifferenceIndicatorView_minusBackground)
        plusTextColor = a.getColor(
            R.styleable.DifferenceIndicatorView_plusTextColor,
            context.getColor(R.color.session_green)
        )
        minusTextColor = a.getColor(
            R.styleable.DifferenceIndicatorView_minusTextColor,
            context.getColor(R.color.session_red)
        )
        if (plusBackground == null) {
            plusBackground =
                AppCompatResources.getDrawable(context, R.drawable.background_sessions_true)
        }
        if (minusBackground == null) {
            minusBackground =
                AppCompatResources.getDrawable(context, R.drawable.background_sessions_false)
        }
        a.recycle()
    }

    fun setValue(value: Number, isPercentageValue: Boolean = false) {
        if (!value.toDouble().isFinite() || value.round() == 0.0) {
            visibility = INVISIBLE
        } else {
            visibility = VISIBLE
            with(if (value.toDouble() >= 0) {
                "+${ParseUtils.splitByThousands(value.round().toString())}"
            } else {
                ParseUtils.splitByThousands(value.round().toString())
            }) {
                text = if (isPercentageValue) {
                    "$this%"
                } else {
                    this
                }
            }

            if (value.toDouble() > 0) {
                setTextColor(plusTextColor)
                background = plusBackground
            } else if (value.toDouble() < 0) {
                setTextColor(minusTextColor)
                background = minusBackground
            }
        }
    }

}