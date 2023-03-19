package ru.forblitz.statistics.data;

import android.app.Activity;
import android.graphics.Paint;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.forblitz.statistics.R.drawable;
import ru.forblitz.statistics.R.id;
import ru.forblitz.statistics.R.string;
import ru.forblitz.statistics.adapters.MemberAdapter;
import ru.forblitz.statistics.utils.Utils;

public class Clan {

    private SmallClanData smallClanData;

    private BigClanData bigClanData;

    public SmallClanData getSmall() {
        return smallClanData;
    }

    public void setSmall(SmallClanData smallClanData) {
        this.smallClanData = smallClanData;
    }

    public void setBigClanData(BigClanData bigClanData) {
        this.bigClanData = bigClanData;
    }

    /**
     * Sets all clan values
     * @param activity required to get resources
     */
    public void set(Activity activity) {

        TextView randomClanName = activity.findViewById(id.random_clan_name);
        TextView ratingClanName = activity.findViewById(id.rating_clan_name);
        TextView randomClanRole = activity.findViewById(id.random_clan_role);
        TextView ratingClanRole = activity.findViewById(id.rating_clan_role);
        TextView clanName = activity.findViewById(id.clan_name);
        TextView clanRole = activity.findViewById(id.clan_role);
        TextView clanMotto = activity.findViewById(id.clan_motto);
        TextView clanDescription = activity.findViewById(id.clan_description);
        TextView clanCreator = activity.findViewById(id.clan_creator);
        TextView clanCreatedAt = activity.findViewById(id.clan_created_at);
        TextView clanOldName = activity.findViewById(id.clan_old_name);
        TextView clanOldTag = activity.findViewById(id.clan_old_tag);
        TextView clanRenamedAt = activity.findViewById(id.clan_renamed_at);
        
        ViewFlipper clanInfoFlipper = activity.findViewById(id.clan_info_flipper);
        TextView clanInfoButton = activity.findViewById(id.clan_info_button);
        TextView clanInfoRecruitingButton = activity.findViewById(id.clan_info_recruiting_button);

        TextView clanRecruitingOptionsBattles = activity.findViewById(id.clan_recruiting_options_battles);
        TextView clanRecruitingOptionsWinsRatio = activity.findViewById(id.clan_recruiting_options_wins_ratio);
        TextView clanRecruitingOptionsAverageDamage = activity.findViewById(id.clan_recruiting_options_average_damage);
        TextView clanRecruitingOptionsAverageBattlesPerDay = activity.findViewById(id.clan_recruiting_options_average_battles_per_day);
        TextView clanRecruitingOptionsVehiclesLevel = activity.findViewById(id.clan_recruiting_options_vehicles_level);

        ListView clanMembersList = activity.findViewById(id.clan_members_list);
        FloatingActionButton clanBackFab = activity.findViewById(id.clan_members_back);

        String fullName = "[" + bigClanData.getTag() + "] " + bigClanData.getName();
        
        activity.runOnUiThread(() -> {

            int paddingHorizontal = clanInfoButton.getPaddingStart();
            int paddingVertical = clanInfoButton.getPaddingTop();

            clanInfoFlipper.setDisplayedChild(0);
            clanInfoButton.setBackground(AppCompatResources.getDrawable(activity, drawable.background_button_insets));
            clanInfoRecruitingButton.setBackgroundColor(activity.getColor(android.R.color.transparent));

            clanInfoButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
            clanInfoRecruitingButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);

            randomClanName.setText(fullName);
            ratingClanName.setText(fullName);
            clanName.setText(fullName);
            randomClanRole.setText(Utils.parseRole(activity, smallClanData.getRole()));
            ratingClanRole.setText(Utils.parseRole(activity, smallClanData.getRole()));
            clanRole.setText(Utils.parseRole(activity, smallClanData.getRole()));
            clanMotto.setText(bigClanData.getMotto());
            clanDescription.setText(bigClanData.getDescription());
            clanCreator.setText(bigClanData.getCreatorName());
            clanCreatedAt.setText(Utils.parseTime(bigClanData.getCreatedAt()));
            if (bigClanData.getOldName() != null) {
                clanOldName.setText(bigClanData.getOldName());
            } else {
                clanOldName.setText(activity.getString(string.empty));
            }
            if (bigClanData.getOldTag() != null) {
                clanOldTag.setText(bigClanData.getOldTag());
            } else {
                clanOldTag.setText(activity.getString(string.empty));
            }
            if (bigClanData.getRenamedAt() != null) {
                clanRenamedAt.setText(Utils.parseTime(bigClanData.getRenamedAt()));
            } else {
                clanRenamedAt.setText(activity.getString(string.empty));
            }

            if (bigClanData.getRecruitingPolicy().equals("open")) {

                clanInfoRecruitingButton.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
                clanInfoButton.setOnClickListener(l -> {
                    clanInfoButton.setBackground(AppCompatResources.getDrawable(activity, drawable.background_button_insets));
                    clanInfoRecruitingButton.setBackgroundColor(activity.getColor(android.R.color.transparent));

                    clanInfoButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
                    clanInfoRecruitingButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);

                    clanInfoFlipper.setDisplayedChild(0);
                });
                clanInfoRecruitingButton.setOnClickListener(l -> {
                    clanInfoButton.setBackgroundColor(activity.getColor(android.R.color.transparent));
                    clanInfoRecruitingButton.setBackground(AppCompatResources.getDrawable(activity, drawable.background_button_insets));

                    clanInfoButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
                    clanInfoRecruitingButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);

                    clanInfoFlipper.setDisplayedChild(1);
                });

                clanRecruitingOptionsBattles.setText(bigClanData.getRecruitingOptions().battles);
                clanRecruitingOptionsWinsRatio.setText(bigClanData.getRecruitingOptions().winsRatio);
                clanRecruitingOptionsAverageDamage.setText(bigClanData.getRecruitingOptions().averageDamage);
                clanRecruitingOptionsAverageBattlesPerDay.setText(bigClanData.getRecruitingOptions().averageBattlesPerDay);
                clanRecruitingOptionsVehiclesLevel.setText(bigClanData.getRecruitingOptions().vehiclesLevel);

            } else {
                clanInfoRecruitingButton.setPaintFlags(clanInfoRecruitingButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                clanInfoRecruitingButton.setOnClickListener(null);

                clanInfoRecruitingButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
            }

            clanMembersList.setAdapter(
                    new MemberAdapter(
                            clanMembersList.getContext(),
                            bigClanData
                                    .getMembers()
                                    .values()
                                    .toArray(new BigClanData.Member[0])
                    )
            );
            clanBackFab.show();

        });

    }

    /**
     * Hides all clan elements; called when the account you are looking for is not a member of a clan
     * @param activity required to get resources
     */
    public void hide(Activity activity) {
        View clanTrueRandomView = activity.findViewById(id.clan_true_random);
        View clanFalseRandomView = activity.findViewById(id.clan_false_random);
        View clanTrueRatingView = activity.findViewById(id.clan_true_rating);
        View clanFalseRatingView = activity.findViewById(id.clan_false_rating);
        ViewFlipper fragmentClan = activity.findViewById(id.fragment_clan);

        activity.runOnUiThread(() -> {

            clanTrueRandomView.setVisibility(View.GONE);
            clanTrueRatingView.setVisibility(View.GONE);
            clanFalseRandomView.setVisibility(View.VISIBLE);
            clanFalseRatingView.setVisibility(View.VISIBLE);
            fragmentClan.setDisplayedChild(1);

        } );
    }

    /**
     * Shows all clan elements; called when the account you are looking for is a member of a clan
     * @param activity required to get resources
     */
    public void show(Activity activity) {
        View clanTrueRandomView = activity.findViewById(id.clan_true_random);
        View clanFalseRandomView = activity.findViewById(id.clan_false_random);
        View clanTrueRatingView = activity.findViewById(id.clan_true_rating);
        View clanFalseRatingView = activity.findViewById(id.clan_false_rating);
        ViewFlipper fragmentClan = activity.findViewById(id.fragment_clan);

        activity.runOnUiThread(() -> {

            clanTrueRandomView.setVisibility(View.VISIBLE);
            clanTrueRatingView.setVisibility(View.VISIBLE);
            clanFalseRandomView.setVisibility(View.GONE);
            clanFalseRatingView.setVisibility(View.GONE);
            fragmentClan.setDisplayedChild(0);

        } );

    }

}