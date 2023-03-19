package ru.forblitz.statistics.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ru.forblitz.statistics.data.StatisticsData;

public class ParseUtils {

    public static StatisticsData parseStatisticsData(String json, String key, String userID) {
        JsonObject jsonObject =
                JsonParser.parseString(json)
                        .getAsJsonObject()
                        .getAsJsonObject("data")
                        .getAsJsonObject(userID);
        JsonObject data = jsonObject.getAsJsonObject("statistics");

        StatisticsData statisticsData = new Gson().fromJson(
                data.getAsJsonObject(key),
                StatisticsData.class
        );
        statisticsData.setNickname(jsonObject.get("nickname").getAsString());
        statisticsData.calculate();
        return statisticsData;
    }

}
