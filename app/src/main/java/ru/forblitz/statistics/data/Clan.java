package ru.forblitz.statistics.data;

import android.app.Activity;
import android.view.View;
import android.widget.GClanInfo;
import android.widget.GClanSmall;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.forblitz.statistics.R;
import ru.forblitz.statistics.adapters.MemberAdapter;
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

        TextView clanName = activity.findViewById(R.id.clan_name);
        TextView clanRole = activity.findViewById(R.id.clan_role);
        TextView clanMotto = activity.findViewById(R.id.clan_motto);
        TextView clanDescription = activity.findViewById(R.id.clan_description);

        ListView clanMembersList = activity.findViewById(R.id.clan_members_list);
        FloatingActionButton clanBackFab = activity.findViewById(R.id.clan_members_back);

        String fullName = "[" + bigClanData.getTag() + "] " + bigClanData.getName();
        
        activity.runOnUiThread(() -> {

            clanName.setText(fullName);
            clanRole.setText(ParseUtils.parseRole(activity, smallClanData.getRole()));
            clanMotto.setText(bigClanData.getMotto());
            clanDescription.setText(bigClanData.getDescription());

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

        ((GClanInfo) activity.findViewById(R.id.gclan_info)).setData(bigClanData);
        ((GClanSmall) activity.findViewById(R.id.clan_true_random)).setData(smallClanData, bigClanData);
        ((GClanSmall) activity.findViewById(R.id.clan_true_rating)).setData(smallClanData, bigClanData);

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