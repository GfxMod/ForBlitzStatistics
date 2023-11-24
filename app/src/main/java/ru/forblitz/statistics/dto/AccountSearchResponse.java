package ru.forblitz.statistics.dto;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class AccountSearchResponse extends APIResponse {

    @SerializedName("data")
    public AccountSearchElement[] data;

    public static class AccountSearchElement {

        @SerializedName("nickname")
        public String nickname;

        @SerializedName("account_id")
        public String accountId;

    }

}
