package ru.forblitz.statistics.dto;

import androidx.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class VehicleStat {

    @SerializedName("all")
    public StatisticsData all;

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


    @NonNull
    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

}
