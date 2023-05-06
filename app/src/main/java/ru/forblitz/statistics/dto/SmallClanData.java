package ru.forblitz.statistics.dto;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import ru.forblitz.statistics.dto.ClanData;

@Keep
public class SmallClanData {

    @SerializedName("account_id")
    public String accountId;

    @SerializedName("account_name")
    public String accountName;

    @SerializedName("clan")
    public ClanData clanData;

    @SerializedName("clan_id")
    public String clanId;

    @SerializedName("joined_at")
    public String joinedAt;

    @SerializedName("role")
    public String role;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}

