package ru.forblitz.statistics.dto;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

@Keep
public class UserAchievementsMap {

    @Keep
    @SerializedName("achievements")
    public HashMap<String, Integer> achievements;

}
