package ru.forblitz.statistics.widget.data

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.appcompat.content.res.AppCompatResources
import ru.forblitz.statistics.R
import ru.forblitz.statistics.data.Constants.ClanInfoViewFlipperItems
import ru.forblitz.statistics.dto.FullClanInfo
import ru.forblitz.statistics.utils.InterfaceUtils
import ru.forblitz.statistics.utils.ParseUtils

class ClanDetails : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setData(fullClanInfo: FullClanInfo) {
        val clanCreator = findViewById<TextView>(R.id.clan_creator)
        val clanCreatedAt = findViewById<TextView>(R.id.clan_created_at)
        val clanOldName = findViewById<TextView>(R.id.clan_old_name)
        val clanOldTag = findViewById<TextView>(R.id.clan_old_tag)
        val clanRenamedAt = findViewById<TextView>(R.id.clan_renamed_at)
        val clanInfoFlipper = findViewById<ViewFlipper>(R.id.clan_info_flipper)
        val clanInfoButton = findViewById<TextView>(R.id.clan_info_button)
        val clanInfoRecruitingButton = findViewById<TextView>(R.id.clan_info_recruiting_button)
        val clanRecruitingOptionsBattles =
            findViewById<TextView>(R.id.clan_recruiting_options_battles)
        val clanRecruitingOptionsWinsRatio =
            findViewById<TextView>(R.id.clan_recruiting_options_wins_ratio)
        val clanRecruitingOptionsAverageDamage =
            findViewById<TextView>(R.id.clan_recruiting_options_average_damage)
        val clanRecruitingOptionsAverageBattlesPerDay =
            findViewById<TextView>(R.id.clan_recruiting_options_average_battles_per_day)
        val clanRecruitingOptionsVehiclesLevel =
            findViewById<TextView>(R.id.clan_recruiting_options_vehicles_level)
        val paddingHorizontal = clanInfoButton.paddingStart
        val paddingVertical = clanInfoButton.paddingTop
        clanInfoFlipper.displayedChild = 0
        clanInfoButton.background =
            AppCompatResources.getDrawable(this.context, R.drawable.background_button_insets)
        clanInfoRecruitingButton.setBackgroundColor(this.context.getColor(android.R.color.transparent))
        clanInfoButton.setPadding(
            paddingHorizontal,
            paddingVertical,
            paddingHorizontal,
            paddingVertical
        )
        clanInfoRecruitingButton.setPadding(
            paddingHorizontal,
            paddingVertical,
            paddingHorizontal,
            paddingVertical
        )
        clanCreator.text = fullClanInfo.creatorName
        if (fullClanInfo.creatorName != "") {
            clanCreator.setOnClickListener {
                InterfaceUtils.search(
                    clanCreator.context,
                    fullClanInfo.creatorName
                )
            }
        }
        clanCreatedAt.text = ParseUtils.formatSecondsTimestampToDate(fullClanInfo.createdAt)
        if (fullClanInfo.oldName != null) {
            clanOldName.text = fullClanInfo.oldName
        } else {
            clanOldName.text = this.context.getString(R.string.empty)
        }
        if (fullClanInfo.oldTag != null) {
            clanOldTag.text = fullClanInfo.oldTag
        } else {
            clanOldTag.text = this.context.getString(R.string.empty)
        }
        if (fullClanInfo.renamedAt != null && fullClanInfo.renamedAt.toLong() != 0L) {
            clanRenamedAt.text = ParseUtils.formatSecondsTimestampToDate(fullClanInfo.renamedAt)
        } else {
            clanRenamedAt.text = this.context.getString(R.string.empty)
        }
        if (fullClanInfo.recruitingPolicy == "open") {
            clanInfoRecruitingButton.paintFlags = Paint.LINEAR_TEXT_FLAG
            clanInfoButton.setOnClickListener {
                clanInfoButton.background = AppCompatResources.getDrawable(
                    this.context,
                    R.drawable.background_button_insets
                )
                clanInfoRecruitingButton.setBackgroundColor(this.context.getColor(android.R.color.transparent))
                clanInfoButton.setPadding(
                    paddingHorizontal,
                    paddingVertical,
                    paddingHorizontal,
                    paddingVertical
                )
                clanInfoRecruitingButton.setPadding(
                    paddingHorizontal,
                    paddingVertical,
                    paddingHorizontal,
                    paddingVertical
                )
                clanInfoFlipper.displayedChild = ClanInfoViewFlipperItems.INFO
            }
            clanInfoRecruitingButton.setOnClickListener {
                clanInfoButton.setBackgroundColor(this.context.getColor(android.R.color.transparent))
                clanInfoRecruitingButton.background = AppCompatResources.getDrawable(
                    this.context,
                    R.drawable.background_button_insets
                )
                clanInfoButton.setPadding(
                    paddingHorizontal,
                    paddingVertical,
                    paddingHorizontal,
                    paddingVertical
                )
                clanInfoRecruitingButton.setPadding(
                    paddingHorizontal,
                    paddingVertical,
                    paddingHorizontal,
                    paddingVertical
                )
                clanInfoFlipper.displayedChild = ClanInfoViewFlipperItems.RECRUITING_OPTIONS
            }
            clanRecruitingOptionsBattles.text = fullClanInfo.recruitingOptions.battles
            clanRecruitingOptionsWinsRatio.text = fullClanInfo.recruitingOptions.winsRatio
            clanRecruitingOptionsAverageDamage.text = fullClanInfo.recruitingOptions.averageDamage
            clanRecruitingOptionsAverageBattlesPerDay.text =
                fullClanInfo.recruitingOptions.averageBattlesPerDay
            clanRecruitingOptionsVehiclesLevel.text = fullClanInfo.recruitingOptions.vehiclesLevel
        } else {
            clanInfoRecruitingButton.paintFlags =
                clanInfoRecruitingButton.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            clanInfoRecruitingButton.setOnClickListener {
                Toast.makeText(
                    context,
                    context.getString(R.string.recruiting_options_disabled, fullClanInfo.tag, fullClanInfo.name),
                    Toast.LENGTH_SHORT
                ).show()
            }
            clanInfoRecruitingButton.setPadding(
                paddingHorizontal,
                paddingVertical,
                paddingHorizontal,
                paddingVertical
            )
        }
    }
}
