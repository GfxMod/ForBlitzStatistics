package ru.forblitz.statistics.utils;

import android.content.Context;
import android.icu.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ru.forblitz.statistics.R;
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

    public static String parseTime(String timestamp) {
        long offset = TimeZone.getDefault().getRawOffset() / 1000L; // насколько в секундах отличается часовой пояс в большую сторону относительно обычного Timestamp
        String time = java.time.format.DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond(Long.parseLong(timestamp) + offset));
        time = time.substring(0, 4) +  "." + time.substring(5, 7) + "." + time.substring(8, 10) + " " + time.substring(11, time.length() - 1);
        return time;
    }

    public static String parseRole(Context context, String role) {
        if (role.equals("private")) {
            return context.getResources().getString(R.string.clan_role_private);
        } else if (role.equals("executive_officer")) {
            return context.getResources().getString(R.string.clan_role_executive_officer);
        } else {
            return context.getResources().getString(R.string.clan_role_commander);
        }
    }

}
