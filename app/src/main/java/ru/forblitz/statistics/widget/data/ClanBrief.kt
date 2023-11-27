package ru.forblitz.statistics.widget.data

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import ru.forblitz.statistics.ForBlitzStatisticsApplication
import ru.forblitz.statistics.R
import ru.forblitz.statistics.apiloadservice.APILoadService
import ru.forblitz.statistics.data.Constants.ClanBriefFlipperItems
import ru.forblitz.statistics.dto.ShortClanInfo
import ru.forblitz.statistics.utils.ParseUtils
import ru.forblitz.statistics.widget.common.DifferenceViewFlipper

class ClanBrief : DifferenceViewFlipper {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    fun setData(shortClanInfo: ShortClanInfo?) {
        if (shortClanInfo?.clanData != null) {
            this.displayedChild = ClanBriefFlipperItems.CLAN_INFORMATION
            val name = "[" + shortClanInfo.clanData.tag + "] " + shortClanInfo.clanData.name
            val role = ParseUtils.formatClanRole(this.context, shortClanInfo.role)
            findViewWithTag<TextView>("clan_name").text = name
            findViewWithTag<TextView>("clan_role").text = role
        } else {
            this.displayedChild = ClanBriefFlipperItems.IS_NOT_A_MEMBER
        }
    }

    fun setServerException(exception: APILoadService.ServerException) {
        displayedChild = ClanBriefFlipperItems.CLAN_INFORMATION

        findViewWithTag<TextView>("clan_name").setText(
            if (
                (context.applicationContext as ForBlitzStatisticsApplication)
                    .preferencesService
                    .region == "ru") {
                R.string.lesta_exception
            } else {
                R.string.wargaming_exception
            }
        )
        findViewWithTag<TextView>("clan_role")
            .text = resources
            .getString(
                R.string.error_message,
                exception.responseError.code.toString(),
                exception.responseError.message
            )
    }

}