package ru.forblitz.statistics.utils;

import android.content.Context;
import android.icu.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ru.forblitz.statistics.R;
import ru.forblitz.statistics.dto.StatisticsData;

public class ParseUtils {

    public static StatisticsData statisticsData(String json, String key) {

        JsonObject dataObject = JsonParser
                .parseString(json)
                .getAsJsonObject()
                .getAsJsonObject("data");
        JsonObject innerDataObject = dataObject.
                getAsJsonObject(dataObject.keySet().iterator().next());

        //

        JsonObject data = innerDataObject.getAsJsonObject("statistics");

        StatisticsData statisticsData = new Gson().fromJson(
                data.getAsJsonObject(key),
                StatisticsData.class
        );
        statisticsData.setNickname(innerDataObject.get("nickname").getAsString());
        statisticsData.calculate();
        return statisticsData;
    }

    public static String time(String timestamp) {
        long offset = TimeZone.getDefault().getRawOffset() / 1000L; // насколько в секундах отличается часовой пояс в большую сторону относительно обычного Timestamp
        String time = java.time.format.DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond(Long.parseLong(timestamp) + offset));
        time = time.substring(0, 4) +  "." + time.substring(5, 7) + "." + time.substring(8, 10) + " " + time.substring(11, time.length() - 1);
        return time;
    }

    public static String role(Context context, String role) {
        if (role.equals("private")) {
            return context.getResources().getString(R.string.clan_role_private);
        } else if (role.equals("executive_officer")) {
            return context.getResources().getString(R.string.clan_role_executive_officer);
        } else {
            return context.getResources().getString(R.string.clan_role_commander);
        }
    }

    public static String timestamp(String string, boolean isFilename) {

        if (!isFilename) {

            // Parse the JSON string into a JsonObject
            JsonObject obj = new Gson().fromJson(string, JsonObject.class);

            // Get the "data" object
            JsonObject data = obj.getAsJsonObject("data");

            // Get the first object in the "data" object
            JsonObject playerData = data.entrySet().iterator().next().getValue().getAsJsonObject();

            return playerData.get("last_battle_time").getAsString();

        } else  {

            return string.substring(string.indexOf("-") + 1, string.lastIndexOf("."));

        }

    }

    public static int minimalAppVersion(String json) {

        JsonObject dataObject = JsonParser
                .parseString(json)
                .getAsJsonObject()
                .getAsJsonObject("statisticAppVersion");

        return dataObject.get("minimalAppVersion").getAsInt();

    }

    public static int currentAppVersion(String json) {

        JsonObject dataObject = JsonParser
                .parseString(json)
                .getAsJsonObject()
                .getAsJsonObject("statisticAppVersion");

        return dataObject.get("currentAppVersion").getAsInt();

    }

}
