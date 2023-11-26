package ru.forblitz.statistics.dto;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class UserStatisticsResponse extends APIResponse {

    @SerializedName("data")
    public HashMap<String, UserStatisticsElement> data;

    public static class UserStatisticsElement {

        @SerializedName("statistics")
        public HashMap<String, StatisticsData> statistics;

        @SerializedName("account_id")
        public String accountId;

        @SerializedName("created_at")
        public String createdAt;;

        @SerializedName("updated_at")
        public String updatedAt;

        @SerializedName("last_battle_time")
        public String lastBattleTime;

        @SerializedName("nickname")
        public String nickname;

    }

}
