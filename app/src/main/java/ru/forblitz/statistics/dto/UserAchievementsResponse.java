package ru.forblitz.statistics.dto;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

@Keep
public class UserAchievementsResponse extends APIResponse {

    @SerializedName("data")
    public HashMap<String, UserAchievementsMap> data;

}
