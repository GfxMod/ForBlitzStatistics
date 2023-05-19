package ru.forblitz.statistics.dto;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class RecruitingOptions {

    @SerializedName("vehicles_level")
    public String vehiclesLevel;
    @SerializedName("wins_ratio")
    public String winsRatio;
    @SerializedName("average_battles_per_day")
    public String averageBattlesPerDay;
    @SerializedName("battles")
    public String battles;
    @SerializedName("average_damage")
    public String averageDamage;

}
