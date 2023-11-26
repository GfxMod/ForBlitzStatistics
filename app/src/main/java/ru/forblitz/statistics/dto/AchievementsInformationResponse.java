package ru.forblitz.statistics.dto;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

@Keep
public class AchievementsInformationResponse extends APIResponse {

    @SerializedName("data")
    public HashMap<String, AchievementInfo> data;

}
