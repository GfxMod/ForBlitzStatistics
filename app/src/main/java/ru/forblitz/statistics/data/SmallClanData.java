package ru.forblitz.statistics.data;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

@Keep
public class SmallClanData {

    @SerializedName("clan")
    public ClanData clanData;
    @SerializedName("account_id")
    public String accountId;
    @SerializedName("joined_at")
    public String joinedAt;
    @SerializedName("clan_id")
    public String clanId;
    @SerializedName("role")
    public String role;
    @SerializedName("account_name")
    public String accountName;

    public ClanData getClanData() {
        return clanData;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getJoinedAt() {
        return joinedAt;
    }

    public String getClanId() {
        return clanId;
    }

    public String getRole() {
        return role;
    }

    public String getAccountName() {
        return accountName;
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}

@Keep
class ClanData {

    @SerializedName("members_count")
    int membersCount;
    @SerializedName("name")
    String name;
    @SerializedName("created_at")
    String createdAt;
    @SerializedName("tag")
    String tag;
    @SerializedName("clan_id")
    String clanId;
    @SerializedName("emblem_set_id")
    String emblemSetId;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
