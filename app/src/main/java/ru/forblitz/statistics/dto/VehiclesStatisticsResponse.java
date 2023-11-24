package ru.forblitz.statistics.dto;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class VehiclesStatisticsResponse extends APIResponse {

    @SerializedName("data")
    public HashMap<String, VehicleStatistics[]> data;

    public static class VehicleStatistics {

        @SerializedName("all")
        public StatisticsData statistics;

        @SerializedName("last_battle_time")
        public String lastBattleTime;

        @SerializedName("account_id")
        public String accountId;

        @SerializedName("max_xp")
        public String maxXp;

        @SerializedName("in_garage_updated")
        public String inGarageUpdated;

        @SerializedName("max_frags")
        public String maxFrags;

        @SerializedName("mark_of_mastery")
        public String markOfMastery;

        @SerializedName("battle_life_time")
        public String battleLifeTime;

        @SerializedName("tank_id")
        public String tankId;

    }

}
