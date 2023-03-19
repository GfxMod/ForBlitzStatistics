package ru.forblitz.statistics.data;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.HashMap;

public class BigClanData {

    public static class RecruitingOptions {

        @SerializedName("vehicles_level")
        String vehiclesLevel;
        @SerializedName("wins_ratio")
        String winsRatio;
        @SerializedName("average_battles_per_day")
        String averageBattlesPerDay;
        @SerializedName("battles")
        String battles;
        @SerializedName("average_damage")
        String averageDamage;

    }

    public static class Member {

        @SerializedName("role")
        String role;
        @SerializedName("joined_at")
        String joinedAt;
        @SerializedName("account_id")
        String accountId;
        @SerializedName("account_name")
        String accountName;

        public String getRole() {
            return role;
        }

        public String getJoinedAt() {
            return joinedAt;
        }

        public String getAccountId() {
            return accountId;
        }

        public String getAccountName() {
            return accountName;
        }

    }

    @SerializedName("recruiting_options")
    private RecruitingOptions recruitingOptions;

    @SerializedName("members_count")
    private String membersCount;
    @SerializedName("name")
    private String name;
    @SerializedName("creator_name")
    private String creatorName;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("tag")
    private String tag;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("recruiting_policy")
    private String recruitingPolicy;
    @SerializedName("leader_name")
    private String leaderName;
    @SerializedName("members_ids")
    private String[] membersIds;
    @SerializedName("creator_id")
    private String creatorId;
    @SerializedName("clan_id")
    private String clanId;

    @SerializedName("members")
    private HashMap<String, Member> members;
    
    @SerializedName("old_name")
    private String oldName;
    @SerializedName("emblem_set_id")
    private String emblemSetId;
    @SerializedName("is_clan_disbanded")
    private String isClanDisbanded;
    @SerializedName("motto")
    private String motto;
    @SerializedName("renamed_at")
    private String renamedAt;
    @SerializedName("old_tag")
    private String oldTag;
    @SerializedName("leader_id")
    private String leaderId;
    @SerializedName("description")
    private String description;

    public RecruitingOptions getRecruitingOptions() {
        return recruitingOptions;
    }

    public String getMembersCount() {
        return membersCount;
    }

    public String getName() {
        return name;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getTag() {
        return tag;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getRecruitingPolicy() {
        return recruitingPolicy;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public String[] getMembersIds() {
        return membersIds;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getClanId() {
        return clanId;
    }

    public HashMap<String, Member> getMembers() {
        return members;
    }

    public String getOldName() {
        return oldName;
    }

    public String getEmblemSetId() {
        return emblemSetId;
    }

    public String getIsClanDisbanded() {
        return isClanDisbanded;
    }

    public String getMotto() {
        return motto;
    }

    public String getRenamedAt() {
        return renamedAt;
    }

    public String getOldTag() {
        return oldTag;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public String getDescription() {
        return description;
    }

    @NonNull
    @Override
    public String toString() {
        String result = "";

        result += "recruitingOptions.vehiclesLevel = " + recruitingOptions.vehiclesLevel + "\n";
        result += "recruitingOptions.winsRatio = " + recruitingOptions.winsRatio + "\n";
        result += "recruitingOptions.averageBattlesPerDay = " + recruitingOptions.averageBattlesPerDay + "\n";
        result += "recruitingOptions.battles = " + recruitingOptions.battles + "\n";
        result += "recruitingOptions.averageDamage = " + recruitingOptions.averageDamage + "\n";
        result += "\n";
        result += "membersCount = " + membersCount + "\n";
        result += "name = " + name + "\n";
        result += "creatorName = " + creatorName + "\n";
        result += "createdAt = " + createdAt + "\n";
        result += "tag = " + tag + "\n";
        result += "updatedAt = " + updatedAt + "\n";
        result += "recruitingPolicy = " + recruitingPolicy + "\n";
        result += "leaderName = " + leaderName + "\n";
        result += "\n";
        result += "membersIds = " + Arrays.toString(membersIds) + "\n";
        result += "\n";
        result += "oldName = " + oldName + "\n";
        result += "creatorId = " + creatorId + "\n";
        result += "clanId = " + clanId + "\n";
        result += "emblemSetId = " + emblemSetId + "\n";
        result += "isClanDisbanded = " + isClanDisbanded + "\n";
        result += "motto = " + motto + "\n";
        result += "renamedAt = " + renamedAt + "\n";
        result += "oldTag = " + oldTag + "\n";
        result += "leaderId = " + leaderId + "\n";
        result += "description = " + description + "\n";

        Member[] membersArray = members.values().toArray(new Member[0]);

        StringBuilder resultBuilder = new StringBuilder(result);
        for (Member member : membersArray) {
            resultBuilder
                    .append(member.accountName).append(" (ID: ")
                    .append(member.accountId)
                    .append(", joined at ")
                    .append(member.joinedAt)
                    .append(") - ")
                    .append(member.role)
                    .append("\n");
        }
        result = resultBuilder.toString();

        return result;
    }

}