package ru.forblitz.statistics.widget.data

import android.content.Context
import android.util.AttributeSet
import android.widget.ListView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.forblitz.statistics.ForBlitzStatisticsApplication
import ru.forblitz.statistics.R
import ru.forblitz.statistics.adapters.MemberAdapter
import ru.forblitz.statistics.apiloadservice.APILoadService
import ru.forblitz.statistics.data.Constants.ClanViewFlipperItems
import ru.forblitz.statistics.dto.FullClanInfo
import ru.forblitz.statistics.dto.ShortClanInfo
import ru.forblitz.statistics.widget.common.DifferenceViewFlipper

class ClanScreen : DifferenceViewFlipper {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    fun setData(shortClanInfo: ShortClanInfo?, fullClanInfo: FullClanInfo?) {
        if (fullClanInfo != null) {
            displayedChild = ClanViewFlipperItems.IS_A_MEMBER
            findViewById<ClanInfo>(R.id.clan_info).setData(shortClanInfo, fullClanInfo)
            findViewById<ClanDetails>(R.id.clan_details).setData(fullClanInfo)
            findViewById<ListView>(R.id.clan_members_list).adapter =
                MemberAdapter(
                    context,
                    fullClanInfo
                        .members
                        .values
                        .toTypedArray()
                )
            findViewById<FloatingActionButton>(R.id.clan_members_back).show()
        } else {
            displayedChild = ClanViewFlipperItems.NOT_IS_A_MEMBER
        }
    }

    fun setServerException(exception: APILoadService.ServerException) {
        displayedChild = ClanViewFlipperItems.SERVER_EXCEPTION

        findViewById<TextView>(R.id.clan_server_exception_title).setText(
            if (
                (context.applicationContext as ForBlitzStatisticsApplication)
                    .preferencesService
                    .region == "ru") {
                R.string.lesta_exception
            } else {
                R.string.wargaming_exception
            }
        )
        findViewById<TextView>(R.id.clan_server_exception_message)
            .text = resources
                .getString(
                    R.string.error_message,
                    exception.responseError.code.toString(),
                    exception.responseError.message
                )
    }
}