package ru.forblitz.statistics.widget.data

import android.content.Context
import android.util.AttributeSet
import android.widget.ListView
import android.widget.TextView
import ru.forblitz.statistics.ForBlitzStatisticsApplication
import ru.forblitz.statistics.R
import ru.forblitz.statistics.adapters.AchievementsAdapter
import ru.forblitz.statistics.apiloadservice.APILoadService
import ru.forblitz.statistics.data.Constants.AchievementsViewFlipperItems.ACHIEVEMENTS
import ru.forblitz.statistics.data.Constants.AchievementsViewFlipperItems.SERVER_EXCEPTION
import ru.forblitz.statistics.widget.common.DifferenceViewFlipper

class AchievementsScreen : DifferenceViewFlipper {

    var adapter: AchievementsAdapter? = null
        set(value) {
            findViewById<ListView>(R.id.achievements_list).adapter = value
            displayedChild = ACHIEVEMENTS
            field = value
        }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    fun setServerException(exception: APILoadService.ServerException) {
        displayedChild = SERVER_EXCEPTION

        findViewById<TextView>(R.id.achievements_server_exception_title).setText(
            if (
                (context.applicationContext as ForBlitzStatisticsApplication)
                    .preferencesService
                    .region == "ru") {
                R.string.lesta_exception
            } else {
                R.string.wargaming_exception
            }
        )
        findViewById<TextView>(R.id.achievements_server_exception_message)
            .text = resources
            .getString(
                R.string.error_message,
                exception.responseError.code.toString(),
                exception.responseError.message
            )
    }

}