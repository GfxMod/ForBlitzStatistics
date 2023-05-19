package ru.forblitz.statistics.widget.data;

import static ru.forblitz.statistics.data.Constants.ClanInfoViewFlipperItems.INFO;
import static ru.forblitz.statistics.data.Constants.ClanInfoViewFlipperItems.RECRUITING_OPTIONS;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import ru.forblitz.statistics.R;
import ru.forblitz.statistics.dto.FullClanInfo;
import ru.forblitz.statistics.utils.InterfaceUtils;
import ru.forblitz.statistics.utils.ParseUtils;

public class ClanDetails extends LinearLayout {

    public ClanDetails(Context context) {
        super(context);
    }

    public ClanDetails(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ClanDetails(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(FullClanInfo fullClanInfo) {

        TextView clanCreator = this.findViewById(R.id.clan_creator);
        TextView clanCreatedAt = this.findViewById(R.id.clan_created_at);
        TextView clanOldName = this.findViewById(R.id.clan_old_name);
        TextView clanOldTag = this.findViewById(R.id.clan_old_tag);
        TextView clanRenamedAt = this.findViewById(R.id.clan_renamed_at);

        ViewFlipper clanInfoFlipper = this.findViewById(R.id.clan_info_flipper);
        TextView clanInfoButton = this.findViewById(R.id.clan_info_button);
        TextView clanInfoRecruitingButton = this.findViewById(R.id.clan_info_recruiting_button);

        TextView clanRecruitingOptionsBattles = this.findViewById(R.id.clan_recruiting_options_battles);
        TextView clanRecruitingOptionsWinsRatio = this.findViewById(R.id.clan_recruiting_options_wins_ratio);
        TextView clanRecruitingOptionsAverageDamage = this.findViewById(R.id.clan_recruiting_options_average_damage);
        TextView clanRecruitingOptionsAverageBattlesPerDay = this.findViewById(R.id.clan_recruiting_options_average_battles_per_day);
        TextView clanRecruitingOptionsVehiclesLevel = this.findViewById(R.id.clan_recruiting_options_vehicles_level);

        int paddingHorizontal = clanInfoButton.getPaddingStart();
        int paddingVertical = clanInfoButton.getPaddingTop();

        clanInfoFlipper.setDisplayedChild(0);
        clanInfoButton.setBackground(AppCompatResources.getDrawable(this.getContext(), R.drawable.background_button_insets));
        clanInfoRecruitingButton.setBackgroundColor(this.getContext().getColor(android.R.color.transparent));

        clanInfoButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
        clanInfoRecruitingButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);

        clanCreator.setText(fullClanInfo.getCreatorName());
        if (!fullClanInfo.getCreatorName().equals("")) {
            clanCreator.setOnClickListener(l -> InterfaceUtils.search(clanCreator.getContext(), fullClanInfo.getCreatorName()));
        }
        clanCreatedAt.setText(ParseUtils.time(fullClanInfo.getCreatedAt()));
        if (fullClanInfo.getOldName() != null) {
            clanOldName.setText(fullClanInfo.getOldName());
        } else {
            clanOldName.setText(this.getContext().getString(R.string.empty));
        }
        if (fullClanInfo.getOldTag() != null) {
            clanOldTag.setText(fullClanInfo.getOldTag());
        } else {
            clanOldTag.setText(this.getContext().getString(R.string.empty));
        }
        if (fullClanInfo.getRenamedAt() != null) {
            clanRenamedAt.setText(ParseUtils.time(fullClanInfo.getRenamedAt()));
        } else {
            clanRenamedAt.setText(this.getContext().getString(R.string.empty));
        }

        if (fullClanInfo.getRecruitingPolicy().equals("open")) {

            clanInfoRecruitingButton.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
            clanInfoButton.setOnClickListener(l -> {
                clanInfoButton.setBackground(AppCompatResources.getDrawable(this.getContext(), R.drawable.background_button_insets));
                clanInfoRecruitingButton.setBackgroundColor(this.getContext().getColor(android.R.color.transparent));

                clanInfoButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
                clanInfoRecruitingButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);

                clanInfoFlipper.setDisplayedChild(INFO);
            });
            clanInfoRecruitingButton.setOnClickListener(l -> {
                clanInfoButton.setBackgroundColor(this.getContext().getColor(android.R.color.transparent));
                clanInfoRecruitingButton.setBackground(AppCompatResources.getDrawable(this.getContext(), R.drawable.background_button_insets));

                clanInfoButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
                clanInfoRecruitingButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);

                clanInfoFlipper.setDisplayedChild(RECRUITING_OPTIONS);
            });

            clanRecruitingOptionsBattles.setText(fullClanInfo.getRecruitingOptions().battles);
            clanRecruitingOptionsWinsRatio.setText(fullClanInfo.getRecruitingOptions().winsRatio);
            clanRecruitingOptionsAverageDamage.setText(fullClanInfo.getRecruitingOptions().averageDamage);
            clanRecruitingOptionsAverageBattlesPerDay.setText(fullClanInfo.getRecruitingOptions().averageBattlesPerDay);
            clanRecruitingOptionsVehiclesLevel.setText(fullClanInfo.getRecruitingOptions().vehiclesLevel);

        } else {
            clanInfoRecruitingButton.setPaintFlags(clanInfoRecruitingButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            clanInfoRecruitingButton.setOnClickListener(null);

            clanInfoRecruitingButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
        }

    }

}
