package ru.forblitz.statistics.dto;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class Member {

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
