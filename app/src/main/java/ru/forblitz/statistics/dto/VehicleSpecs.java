package ru.forblitz.statistics.dto;

import androidx.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class VehicleSpecs {

    @SerializedName("name")
    public String name;

    @SerializedName("nation")
    public String nation;

    @SerializedName("tier")
    public int tier;

    @SerializedName("type")
    public String type;

    @NonNull
    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

}
