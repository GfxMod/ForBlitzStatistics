package ru.forblitz.statistics.utils;

import android.content.Context;
import android.icu.util.Calendar;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import ru.forblitz.statistics.R;

public class  ParseUtils {

    public static String formatSecondsTimestampToDate(String timestamp) {
        long offset = Calendar.getInstance().getTimeZone().getRawOffset() / 1000;
        String time = java.time.format.DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond(Long.parseLong(timestamp) + offset));
        time = time.substring(0, 4) +  "." + time.substring(5, 7) + "." + time.substring(8, 10) + " " + time.substring(11, time.length() - 1);
        return time;
    }

    public static String formatMillisTimestampToDate(String timestamp) {
        long offset = Calendar.getInstance().getTimeZone().getRawOffset();
        String time = java.time.format.DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochMilli(Long.parseLong(timestamp) + offset));
        time = time.substring(0, 4) +  "." + time.substring(5, 7) + "." + time.substring(8, 10) + " " + time.substring(11, time.length() - 1);
        return time;
    }

    public static String formatMillisTimestampToTime(String timestamp) {
        String time = formatMillisTimestampToDate(timestamp);
        time = time.substring(time.indexOf(" ") + 1);
        return time;
    }

    public static String formatClanRole(Context context, String role) {
        if (role.equals("private")) {
            return context.getResources().getString(R.string.clan_role_private);
        } else if (role.equals("executive_officer")) {
            return context.getResources().getString(R.string.clan_role_executive_officer);
        } else {
            return context.getResources().getString(R.string.clan_role_commander);
        }
    }

    public static String parseTimestamp(String string, boolean isFilename) {

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

    public static int parseMinimalAppVersion(String json) {

        JsonObject dataObject = JsonParser
                .parseString(json)
                .getAsJsonObject()
                .getAsJsonObject("statisticAppVersion");

        return dataObject.get("minimalAppVersion").getAsInt();

    }

    public static int parseCurrentAppVersion(String json) {

        JsonObject dataObject = JsonParser
                .parseString(json)
                .getAsJsonObject()
                .getAsJsonObject("statisticAppVersion");

        return dataObject.get("currentAppVersion").getAsInt();

    }

    public static String parseLestaAPIToken(String json) {
        JsonObject dataObject = JsonParser
                .parseString(json)
                .getAsJsonObject()
                .getAsJsonObject("statisticsApiKeys");
        return dataObject.get("lesta").getAsString();
    }

    public static String parseWargamingAPIToken(String json) {
        JsonObject dataObject = JsonParser
                .parseString(json)
                .getAsJsonObject()
                .getAsJsonObject("statisticsApiKeys");

        return dataObject.get("wargaming").getAsString();
    }

    public static String parseBannerAdUnitId(String json) {
        JsonObject dataObject = JsonParser
                .parseString(json)
                .getAsJsonObject()
                .getAsJsonObject("statisticsAdUnitIds");
        return dataObject.get("banner").getAsString();
    }

    public static String parseInterstitialAdUnitId(String json) {
        JsonObject dataObject = JsonParser
                .parseString(json)
                .getAsJsonObject()
                .getAsJsonObject("statisticsAdUnitIds");
        return dataObject.get("interstitial").getAsString();
    }

    /**
     * Divides every third digit of the number with a space.
     * @param number number to split
     * @return formatted number
     */
    public static String splitByThousands(String number) {

        String prefix = "";
        if (number.charAt(0) == '+') {
            prefix = "+";
        }

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);

        try {
            return prefix + formatter.format(Double.parseDouble(number));
        } catch (java.lang.IllegalArgumentException e) {
            return number;
        }

    }

}
