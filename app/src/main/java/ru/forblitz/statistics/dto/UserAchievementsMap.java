package ru.forblitz.statistics.dto;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

@Keep
public class UserAchievementsMap {

    @Keep
    @SerializedName("achievements")
    public Map<String, Integer> achievements;

}
