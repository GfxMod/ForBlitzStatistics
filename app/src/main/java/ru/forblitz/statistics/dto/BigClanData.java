package ru.forblitz.statistics.dto;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class BigClanData {

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
        return new Gson().toJson(this);
    }

}