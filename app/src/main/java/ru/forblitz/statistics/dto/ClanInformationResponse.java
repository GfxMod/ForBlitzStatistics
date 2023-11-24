package ru.forblitz.statistics.dto;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

@Keep
public class ClanInformationResponse extends APIResponse {

    @SerializedName("data")
    public HashMap<String, FullClanInfo> data;

}
