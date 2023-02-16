package ru.forblitz.statistics.data;

import static android.graphics.Typeface.BOLD;
import static android.view.Gravity.START;
import static androidx.core.widget.TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import ru.forblitz.statistics.R.*;

import java.util.ArrayList;

public class Clan {

    private String smallJson;
    private String bigJson;
    private String clanId;
    private String name;
    private String tag;
    private String role;
    private int membersCount;
    private String leader;
    private String motto;
    private String description;
    private String creator;
    private String createdAt;
    private String oldName;
    private String oldTag;
    private String renamedAt;
    private String recruitingPolicy;
    private String recruitingVehiclesLevel;
    private String recruitingWinsRatio;
    private String recruitingAverageBattlesPerDay;
    private String recruitingBattles;
    private String recruitingAverageDamage;
    private ArrayList<Member> members;
    private String nameValue;

    public boolean setSmallJson(String smallJson) {

        if (smallJson.contains("clan_id")) {
            this.smallJson = smallJson;
            this.clanId = parseValueFromJson("clan_id", smallJson);
            this.name = parseValueFromJson("name", smallJson);
            this.tag = parseValueFromJson("tag", smallJson);
            this.role = parseValueFromJson("role", smallJson);
            this.membersCount = Integer.parseInt(parseValueFromJson("members_count", smallJson));
            this.nameValue = "[" + tag + "] " + name;

            return true;
        } else {
            return false;
        }

    }

    public void setBigJson(String bigJson) {

        this.bigJson = bigJson;
        this.leader = parseValueFromJson("leader_name", bigJson);
        this.motto = parseValueFromJson("motto", bigJson);
        this.description = parseValueFromJson("description", bigJson);
        this.creator = parseValueFromJson("creator_name", bigJson);
        this.createdAt = parseValueFromJson("created_at", bigJson);
        if (bigJson.contains("old_name")) {
            this.oldName = parseValueFromJson("old_name", bigJson);
        }
        if (bigJson.contains("old_tag")) {
            this.oldTag = parseValueFromJson("old_tag", bigJson);
        }
        if (bigJson.contains("renamed_at")) {
            this.renamedAt = parseValueFromJson("renamed_at", bigJson);
        }
        this.recruitingPolicy = parseValueFromJson("recruiting_policy", bigJson);
        if (recruitingPolicy.equals("open")) {
            this.recruitingVehiclesLevel = parseValueFromJson("vehicles_level", bigJson);
            this.recruitingWinsRatio = parseValueFromJson("wins_ratio", bigJson);
            this.recruitingAverageBattlesPerDay = parseValueFromJson("average_battles_per_day", bigJson);
            this.recruitingBattles = parseValueFromJson("battles", bigJson);
            this.recruitingAverageDamage = parseValueFromJson("average_damage", bigJson);
        }

        String iBigJson = bigJson.substring(bigJson.indexOf("\"members\": {") + 12);
        iBigJson = iBigJson.substring(iBigJson.indexOf("\": {") + 4);
        members = new ArrayList<>(0);
        for (int i = 0; i < membersCount; i++) {
            members.add(new Member(iBigJson.substring(0, iBigJson.indexOf("},"))));
            iBigJson = iBigJson.substring(iBigJson.indexOf("},") + 2);
        }

    }

    public String getClanId() {
        return this.clanId;
    }

    public String getName() {
        return this.name;
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
        
        activity.runOnUiThread(() -> {


            int paddingHorizontal = clanInfoButton.getPaddingStart();
            int paddingVertical = clanInfoButton.getPaddingTop();

            clanInfoFlipper.setDisplayedChild(0);
            clanInfoButton.setBackground(AppCompatResources.getDrawable(activity, drawable.background_button_insets));
            clanInfoRecruitingButton.setBackgroundColor(activity.getColor(android.R.color.transparent));

            clanInfoButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
            clanInfoRecruitingButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);

            randomClanName.setText(nameValue);
            ratingClanName.setText(nameValue);
            clanName.setText(nameValue);
            if (role.equals("private")) {
                randomClanRole.setText(string.clan_role_private);
                ratingClanRole.setText(string.clan_role_private);
                clanRole.setText(string.clan_role_private);
            } else if (role.equals("executive_officer")) {
                randomClanRole.setText(string.clan_role_executive_officer);
                ratingClanRole.setText(string.clan_role_executive_officer);
                clanRole.setText(string.clan_role_executive_officer);
            } else {
                randomClanRole.setText(string.clan_role_commander);
                ratingClanRole.setText(string.clan_role_commander);
                clanRole.setText(string.clan_role_commander);
            }
            clanMotto.setText(motto);
            clanDescription.setText(description);
            clanCreator.setText(creator);
            clanCreatedAt.setText(Utils.parseTime(createdAt));
            if (oldName != null) {
                clanOldName.setText(oldName);
            } else {
                clanOldName.setText(activity.getString(string.empty));
            }
            if (oldTag != null) {
                clanOldTag.setText(oldTag);
            } else {
                clanOldTag.setText(activity.getString(string.empty));
            }
            if (renamedAt != null) {
                clanRenamedAt.setText(Utils.parseTime(renamedAt));
            } else {
                clanRenamedAt.setText(activity.getString(string.empty));
            }

            if (recruitingPolicy.equals("open")) {

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

                clanRecruitingOptionsBattles.setText(recruitingBattles);
                clanRecruitingOptionsWinsRatio.setText(recruitingWinsRatio);
                clanRecruitingOptionsAverageDamage.setText(recruitingAverageDamage);
                clanRecruitingOptionsAverageBattlesPerDay.setText(recruitingAverageBattlesPerDay);
                clanRecruitingOptionsVehiclesLevel.setText(recruitingVehiclesLevel);

            } else {
                clanInfoRecruitingButton.setPaintFlags(clanInfoRecruitingButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                clanInfoRecruitingButton.setOnClickListener(l -> {  });

                clanInfoRecruitingButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
            }

            members.forEach(it -> it.create(activity));
        });

    }

    /**
     * Clears all values and removes all views in the members list layout
     * @param activity required to get resources
     */
    public void clear(Activity activity) {
        LinearLayout list = activity.findViewById(id.clan_members_list);
        list.removeAllViews();

        smallJson = null;
        bigJson = null;
        clanId = null;
        name = null;
        tag = null;
        role = null;
        membersCount = 0;
        leader = null;
        motto = null;
        description = null;
        creator = null;
        createdAt = null;
        oldName = null;
        oldTag = null;
        renamedAt = null;
        recruitingPolicy = null;
        recruitingVehiclesLevel = null;
        recruitingWinsRatio = null;
        recruitingAverageBattlesPerDay = null;
        recruitingBattles = null;
        recruitingAverageDamage = null;
        if (members != null) { members.clear(); }
        nameValue = null;
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

    /**
     * Gets value of the parameter from json
     * @param param the name of the parameter whose value should be found
     * @param json json in which to search for the value
     * @return the value of the parameter
     */
    @NonNull
    private String parseValueFromJson(String param, String json) {

        String currentJson = json.substring(json.indexOf(param + "\": ") + param.length() + 3);

        try {

            if (currentJson.indexOf(",") < currentJson.indexOf("\n") && currentJson.contains(",")) {
                String t = currentJson.substring(0, currentJson.indexOf(","));
                if (t.contains("\"")) { t = t.substring(1, t.length() - 1); }

                return t;
            } else {
                String t = currentJson.substring(0, currentJson.indexOf("\n"));
                if (t.contains("\"")) { t = t.substring(1, t.length() - 1); }

                return t;
            }

        } catch (java.lang.StringIndexOutOfBoundsException e) {
            Log.d("param", param);
            Log.d("json", json);
            Log.d("currentJson", currentJson);
            return "0";
        }

    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @NonNull
    @Override
    public String toString() {

        String result = "-\n--MEMBER DATA--\n";

        result += "smallJson: " + smallJson + "\n";
        result += "bigJson: " + bigJson + "\n";
        result += "clanId: " + clanId + "\n";
        result += "name: " + name + "\n";
        result += "tag: " + tag + "\n";
        result += "role: " + role + "\n";
        result += "membersCount: " + membersCount + "\n";
        result += "leader: " + leader + "\n";
        result += "motto: " + motto + "\n";
        result += "description: " + description + "\n";
        result += "creator: " + creator + "\n";
        result += "createdAt: " + createdAt + "\n";
        result += "oldName: " + oldName + "\n";
        result += "oldTag: " + oldTag + "\n";
        result += "renamedAt: " + renamedAt + "\n";
        result += "recruitingPolicy: " + recruitingPolicy + "\n";
        result += "recruitingVehiclesLevel: " + recruitingVehiclesLevel + "\n";
        result += "recruitingWinsRatio: " + recruitingWinsRatio + "\n";
        result += "recruitingAverageBattlesPerDay: " + recruitingAverageBattlesPerDay + "\n";
        result += "recruitingBattles: " + recruitingBattles + "\n";
        result += "recruitingAverageDamage: " + recruitingAverageDamage + "\n";
        result += "members: " + members + "\n";
        result += "nameValue: " + nameValue + "\n";

        return result;

    }

}

class Member {

    Member(String json) {
        this.role = json.substring(json.indexOf("\"role\": \"") + 9);
        role = role.substring(0, role.indexOf("\","));
        json = json.substring(json.indexOf("\",") + 2);
        joinedAt = json.substring(json.indexOf("\"joined_at\": ") + 13);
        joinedAt = joinedAt.substring(0, joinedAt.indexOf(","));
        json = json.substring(json.indexOf(",") + 1);
        memberId = json.substring(json.indexOf("\"account_id\": ") + 14);
        memberId = memberId.substring(0, memberId.indexOf(","));
        json = json.substring(json.indexOf(",") + 1);
        name = json.substring(json.indexOf("\"account_name\": \"") + 17);
        name = name.substring(0, name.indexOf("\""));
    }

    String role;
    String joinedAt;
    String memberId;
    String name;

    @SuppressLint("RestrictedApi")
    void create(Activity activity) {
        LinearLayout list = activity.findViewById(id.clan_members_list);
        int width = Utils.getX(activity);
        int padding = width * 25 / 1000;

        //
        ////
        //

        LinearLayout mainLayout = new LinearLayout(activity);
        LinearLayout.LayoutParams mainLayoutLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, padding * 6);
        mainLayoutLayoutParams.setMargins(0, padding, 0, 0);
        mainLayout.setLayoutParams(mainLayoutLayoutParams);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackground(AppCompatResources.getDrawable(activity, drawable.background_layout_nested));
        mainLayout.setPadding(padding, padding, padding, padding);

        //
        ////
        //

        AppCompatTextView nameMember = new AppCompatTextView(activity);
        nameMember.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                padding * 2)
        );
        nameMember.setTextColor(ContextCompat.getColor(activity, color.white));
        nameMember.setTypeface(ResourcesCompat.getFont(activity, font.inter), BOLD);
        nameMember.setGravity(START);
        nameMember.setMaxLines(1);
        nameMember.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_UNIFORM);
        nameMember.setText(name);

        //
        ////
        //

        AppCompatTextView roleAndJoinedAtMember = new AppCompatTextView(activity);
        roleAndJoinedAtMember.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                padding * 2)
        );
        roleAndJoinedAtMember.setTextColor(ContextCompat.getColor(activity, color.grey));
        roleAndJoinedAtMember.setTypeface(ResourcesCompat.getFont(activity, font.inter));
        roleAndJoinedAtMember.setGravity(START);
        roleAndJoinedAtMember.setMaxLines(1);
        roleAndJoinedAtMember.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_UNIFORM);
        String roleAndJoinedAt = "; " + activity.getString(string.joined_at) + Utils.parseTime(joinedAt);
        if (role.equals("private")) {
            roleAndJoinedAt = activity.getString(string.clan_role_private) + roleAndJoinedAt;
        } else if (role.equals("executive_officer")) {
            roleAndJoinedAt = activity.getString(string.clan_role_executive_officer) + roleAndJoinedAt;
        } else {
            roleAndJoinedAt = activity.getString(string.clan_role_commander) + roleAndJoinedAt;
        }
        roleAndJoinedAtMember.setText(roleAndJoinedAt);

        //
        ////
        //

        list.addView(mainLayout);
        mainLayout.addView(nameMember);
        mainLayout.addView(roleAndJoinedAtMember);
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @NonNull
    @Override
    public String toString() {
        String result = "-\n--MEMBER DATA--\n";

        result += "role: " + role + "\n";
        result += "joinedAt: " + joinedAt + "\n";
        result += "memberId: " + memberId + "\n";
        result += "name: " + name + "\n";

        return result;
    }

}