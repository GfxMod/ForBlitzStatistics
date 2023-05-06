package ru.forblitz.statistics.dto;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

@Keep
public class ClanData {

    @SerializedName("clan_id")
    public String clanId;

    @SerializedName("created_at")
    public String createdAt;

    @SerializedName("emblem_set_id")
    public String emblemSetId;

    @SerializedName("members_count")
    public int membersCount;

    @SerializedName("name")
    public String name;

    @SerializedName("tag")
    public String tag;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
