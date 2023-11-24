package ru.forblitz.statistics.dto;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

@Keep
public class MetadataResponse {

    @SerializedName("statisticAppVersion")
    public StatisticAppVersion statisticAppVersion;

    @SerializedName("statisticsApiKeys")
    public StatisticsApiKeys statisticsApiKeys;

    @SerializedName("statisticsAdUnitIds")
    public StatisticsAdUnitIds statisticsAdUnitIds;

    public static class StatisticsApiKeys {

        @SerializedName("lesta")
        public String lesta;

        @SerializedName("wargaming")
        public String wargaming;

    }

    public static class StatisticsAdUnitIds {

        @SerializedName("banner")
        public String banner;

        @SerializedName("interstitial")
        public String interstitial;

    }

    public static class StatisticAppVersion {

        @SerializedName("currentAppVersion")
        public Integer currentAppVersion;

        @SerializedName("minimalAppVersion")
        public Integer minimalAppVersion;

    }

    @NonNull
    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

}
