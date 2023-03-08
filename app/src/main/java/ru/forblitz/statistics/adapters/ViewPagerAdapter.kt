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

class ViewPagerAdapter(fragmentManager: FragmentManager, private val context: Context, private val tabCount: Int):
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
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

    override fun getCount(): Int {
        return tabCount
    }

    override fun getPageTitle(position: Int): CharSequence {
        val span: ImageSpan
        val drawable: Drawable = AppCompatResources.getDrawable(context,
            when (position) {
                0 -> {
                    R.drawable.ic_outline_bar_chart_36
                }
                1 -> {
                    R.drawable.ic_outline_auto_graph_36
                }
                2 -> {
                    R.drawable.ic_outline_group_36
                }
                else -> {
                    R.drawable.ic_tanks
                }
            }
        )!!
        val title = SpannableStringBuilder(" ")
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
