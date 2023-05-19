package ru.forblitz.statistics.dto;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

@Keep
public class ApiResponse {

    @Keep
    @SerializedName("data")
    public Map<String, VehicleSpecs> data;

}
