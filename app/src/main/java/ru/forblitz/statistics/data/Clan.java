package ru.forblitz.statistics.data;

import android.app.Activity;
import android.graphics.Paint;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.forblitz.statistics.R;
import ru.forblitz.statistics.adapters.MemberAdapter;
import ru.forblitz.statistics.utils.InterfaceUtils;
import ru.forblitz.statistics.utils.ParseUtils;

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

        TextView randomClanName = activity.findViewById(R.id.random_clan_name);
        TextView ratingClanName = activity.findViewById(R.id.rating_clan_name);
        TextView randomClanRole = activity.findViewById(R.id.random_clan_role);
        TextView ratingClanRole = activity.findViewById(R.id.rating_clan_role);
        TextView clanName = activity.findViewById(R.id.clan_name);
        TextView clanRole = activity.findViewById(R.id.clan_role);
        TextView clanMotto = activity.findViewById(R.id.clan_motto);
        TextView clanDescription = activity.findViewById(R.id.clan_description);
        TextView clanCreator = activity.findViewById(R.id.clan_creator);
        TextView clanCreatedAt = activity.findViewById(R.id.clan_created_at);
        TextView clanOldName = activity.findViewById(R.id.clan_old_name);
        TextView clanOldTag = activity.findViewById(R.id.clan_old_tag);
        TextView clanRenamedAt = activity.findViewById(R.id.clan_renamed_at);
        
        ViewFlipper clanInfoFlipper = activity.findViewById(R.id.clan_info_flipper);
        TextView clanInfoButton = activity.findViewById(R.id.clan_info_button);
        TextView clanInfoRecruitingButton = activity.findViewById(R.id.clan_info_recruiting_button);

        TextView clanRecruitingOptionsBattles = activity.findViewById(R.id.clan_recruiting_options_battles);
        TextView clanRecruitingOptionsWinsRatio = activity.findViewById(R.id.clan_recruiting_options_wins_ratio);
        TextView clanRecruitingOptionsAverageDamage = activity.findViewById(R.id.clan_recruiting_options_average_damage);
        TextView clanRecruitingOptionsAverageBattlesPerDay = activity.findViewById(R.id.clan_recruiting_options_average_battles_per_day);
        TextView clanRecruitingOptionsVehiclesLevel = activity.findViewById(R.id.clan_recruiting_options_vehicles_level);

        ListView clanMembersList = activity.findViewById(R.id.clan_members_list);
        FloatingActionButton clanBackFab = activity.findViewById(R.id.clan_members_back);

        String fullName = "[" + bigClanData.getTag() + "] " + bigClanData.getName();
        
        activity.runOnUiThread(() -> {

            int paddingHorizontal = clanInfoButton.getPaddingStart();
            int paddingVertical = clanInfoButton.getPaddingTop();

            clanInfoFlipper.setDisplayedChild(0);
            clanInfoButton.setBackground(AppCompatResources.getDrawable(activity, R.drawable.background_button_insets));
            clanInfoRecruitingButton.setBackgroundColor(activity.getColor(android.R.color.transparent));

            clanInfoButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
            clanInfoRecruitingButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);

            randomClanName.setText(fullName);
            ratingClanName.setText(fullName);
            clanName.setText(fullName);
            randomClanRole.setText(ParseUtils.parseRole(activity, smallClanData.getRole()));
            ratingClanRole.setText(ParseUtils.parseRole(activity, smallClanData.getRole()));
            clanRole.setText(ParseUtils.parseRole(activity, smallClanData.getRole()));
            clanMotto.setText(bigClanData.getMotto());
            clanDescription.setText(bigClanData.getDescription());
            clanCreator.setText(bigClanData.getCreatorName());
            if (!bigClanData.getCreatorName().equals("")) {
                clanCreator.setOnClickListener(l -> InterfaceUtils.search(clanCreator.getContext(), bigClanData.getCreatorName()));
            }
            clanCreatedAt.setText(ParseUtils.parseTime(bigClanData.getCreatedAt()));
            if (bigClanData.getOldName() != null) {
                clanOldName.setText(bigClanData.getOldName());
            } else {
                clanOldName.setText(activity.getString(R.string.empty));
            }
            if (bigClanData.getOldTag() != null) {
                clanOldTag.setText(bigClanData.getOldTag());
            } else {
                clanOldTag.setText(activity.getString(R.string.empty));
            }
            if (bigClanData.getRenamedAt() != null) {
                clanRenamedAt.setText(ParseUtils.parseTime(bigClanData.getRenamedAt()));
            } else {
                clanRenamedAt.setText(activity.getString(R.string.empty));
            }

            if (bigClanData.getRecruitingPolicy().equals("open")) {

                clanInfoRecruitingButton.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
                clanInfoButton.setOnClickListener(l -> {
                    clanInfoButton.setBackground(AppCompatResources.getDrawable(activity, R.drawable.background_button_insets));
                    clanInfoRecruitingButton.setBackgroundColor(activity.getColor(android.R.color.transparent));

                    clanInfoButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
                    clanInfoRecruitingButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);

                    clanInfoFlipper.setDisplayedChild(0);
                });
                clanInfoRecruitingButton.setOnClickListener(l -> {
                    clanInfoButton.setBackgroundColor(activity.getColor(android.R.color.transparent));
                    clanInfoRecruitingButton.setBackground(AppCompatResources.getDrawable(activity, R.drawable.background_button_insets));

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
        View clanTrueRandomView = activity.findViewById(R.id.clan_true_random);
        View clanFalseRandomView = activity.findViewById(R.id.clan_false_random);
        View clanTrueRatingView = activity.findViewById(R.id.clan_true_rating);
        View clanFalseRatingView = activity.findViewById(R.id.clan_false_rating);
        ViewFlipper fragmentClan = activity.findViewById(R.id.fragment_clan);

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
        View clanTrueRandomView = activity.findViewById(R.id.clan_true_random);
        View clanFalseRandomView = activity.findViewById(R.id.clan_false_random);
        View clanTrueRatingView = activity.findViewById(R.id.clan_true_rating);
        View clanFalseRatingView = activity.findViewById(R.id.clan_false_rating);
        ViewFlipper fragmentClan = activity.findViewById(R.id.fragment_clan);

        activity.runOnUiThread(() -> {

            clanTrueRandomView.setVisibility(View.VISIBLE);
            clanTrueRatingView.setVisibility(View.VISIBLE);
            clanFalseRandomView.setVisibility(View.GONE);
            clanFalseRatingView.setVisibility(View.GONE);
            fragmentClan.setDisplayedChild(0);

        } );

    }

}