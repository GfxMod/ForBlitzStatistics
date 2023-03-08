package ru.forblitz.statistics.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.forblitz.statistics.*

class ViewPagerAdapter(fragmentManager: FragmentManager, private val context: Context):
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        // TODO: кажется, что можно удалить else или объединить его с 0 через запятую. Надо изучить
        return when (position) {
            0 -> {
                RandomFragment()
            }
            1 -> {
                RatingFragment()
            }
            2 -> {
                ClanFragment()
            }
            3 -> {
                TanksFragment()
            }
            else -> {
                RandomFragment()
            }
        }
    }

    // TODO: нельзя хардкодить числа внутри методов
    // Если у вас правда это такой особый размер, положите выше в константу
    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence {
        val title: SpannableStringBuilder
        val span: ImageSpan
        // TODO: аналогично объединить case, чтобы избежать дублирования кода
        when (position) {
            0 -> {
                val drawable: Drawable = AppCompatResources.getDrawable(context,
                    R.drawable.ic_outline_bar_chart_36
                )!!
                title = SpannableStringBuilder(" ")
                drawable.setBounds(
                    0,
                    0,
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight
                )
                span = ImageSpan(drawable, ImageSpan.ALIGN_BASELINE)
                title.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                return title
            }
            1 -> {
                val drawable: Drawable = AppCompatResources.getDrawable(context,
                    R.drawable.ic_outline_auto_graph_36
                )!!
                title = SpannableStringBuilder(" ")
                drawable.setBounds(
                    0,
                    0,
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight
                )
                span = ImageSpan(drawable, ImageSpan.ALIGN_BASELINE)
                title.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                return title
            }
            2 -> {
                val drawable: Drawable = AppCompatResources.getDrawable(context,
                    R.drawable.ic_outline_group_36
                )!!
                title = SpannableStringBuilder(" ")
                drawable.setBounds(
                    0,
                    0,
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight
                )
                span = ImageSpan(drawable, ImageSpan.ALIGN_BASELINE)
                title.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                return title
            }
            else -> {
                val drawable: Drawable = AppCompatResources.getDrawable(context,
                    R.drawable.ic_tanks
                )!!
                title = SpannableStringBuilder(" ")
                drawable.setBounds(
                    0,
                    0,
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight
                )
                span = ImageSpan(drawable, ImageSpan.ALIGN_BASELINE)
                title.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                return title
            }
        }
    }

}
