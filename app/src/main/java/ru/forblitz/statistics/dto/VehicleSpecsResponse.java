package ru.forblitz.statistics.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class VehicleSpecsResponse extends APIResponse {

    @SerializedName("data")
    public Map<String, VehicleSpecs> data;

}
