package ru.forblitz.statistics.widget.data

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import ru.forblitz.statistics.dto.StatisticsData
import ru.forblitz.statistics.utils.ParseUtils

class DetailsLayout : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setData(statisticsData: StatisticsData?) {
        for (view in getAllChildren(this)) {
            if (view is TextView && view.tag != null) {
                try {
                    val field =
                        StatisticsData::class.java.getDeclaredField(view.getTag().toString())
                    field.isAccessible = true
                    view.text = ParseUtils.splitByThousands(
                        (field[statisticsData] as Int).toString()
                    )
                } catch (e: NoSuchFieldException) {
                    Log.e("DetailsLayout", e.toString())
                } catch (e: IllegalAccessException) {
                    Log.e("DetailsLayout", e.toString())
                }
            }
        }
    }

    private fun getAllChildren(linearLayout: LinearLayout): ArrayList<View> {
        val children = ArrayList<View>()
        for (i in 0 until linearLayout.childCount) {
            val child = linearLayout.getChildAt(i)
            children.add(child)
            if (child is LinearLayout) {
                children.addAll(getAllChildren(child))
            }
        }
        return children
    }
}