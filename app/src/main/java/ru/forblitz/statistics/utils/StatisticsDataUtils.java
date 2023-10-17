package ru.forblitz.statistics.utils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Locale;

import ru.forblitz.statistics.data.Constants;
import ru.forblitz.statistics.dto.StatisticsData;

public class StatisticsDataUtils {

    /**
     * Calculates differences between the current data and then selected session data
     * @param current current statistics data
     * @param session selected session statistics data
     * @return the differences between the current data and then selected session data
     */
    @NonNull
    public static StatisticsData calculateSession(StatisticsData current, StatisticsData session) {
        StatisticsData sessionDifferences = calculateFieldDifferences(current, session);
        sessionDifferences.setWinRate(String.format(Locale.US, "%.2f", Double.parseDouble(sessionDifferences.getWins()) / Double.parseDouble(sessionDifferences.getBattles()) * 100));
        sessionDifferences.setAverageDamage(String.format(Locale.US, "%.2f", Double.parseDouble(sessionDifferences.getDamageDealt()) / Double.parseDouble(sessionDifferences.getBattles())));
        sessionDifferences.setEfficiency(String.format(Locale.US, "%.2f", Double.parseDouble(sessionDifferences.getDamageDealt()) / Double.parseDouble(sessionDifferences.getDamageReceived())));
        sessionDifferences.setSurvived(String.format(Locale.US, "%.2f", Double.parseDouble(sessionDifferences.getSurvivedBattles()) / Double.parseDouble(sessionDifferences.getBattles()) * 100));
        sessionDifferences.setHitsFromShots(String.format(Locale.US, "%.2f", Double.parseDouble(sessionDifferences.getHits()) / Double.parseDouble(sessionDifferences.getShots()) * 100));
        sessionDifferences.setAverageXp(String.format(Locale.US, "%.2f", Double.parseDouble(sessionDifferences.getXp()) / Double.parseDouble(sessionDifferences.getBattles()) * 100));
        sessionDifferences.setNickname(current.getNickname());

        return sessionDifferences;
    }

    /**
     * Calculates differences between the current data and then selected session data
     * @param current current statistics data
     * @param session selected session statistics data
     * @return the differences between the current data and then selected session data
     */
    @NonNull
    public static StatisticsData calculateSessionDifferences(StatisticsData current, StatisticsData session) {
        StatisticsData sessionDifferences = calculateFieldDifferences(current, session);
        sessionDifferences.setWinRate(String.format(Locale.US, "%.2f",Double.parseDouble(current.getWinRate()) - Double.parseDouble(session.getWinRate())));
        sessionDifferences.setAverageDamage(String.format(Locale.US, "%.2f",Double.parseDouble(current.getAverageDamage()) - Double.parseDouble(session.getAverageDamage())));
        sessionDifferences.setEfficiency(String.format(Locale.US, "%.2f",Double.parseDouble(current.getEfficiency()) - Double.parseDouble(session.getEfficiency())));
        sessionDifferences.setSurvived(String.format(Locale.US, "%.2f",Double.parseDouble(current.getSurvived()) - Double.parseDouble(session.getSurvived())));
        sessionDifferences.setHitsFromShots(String.format(Locale.US, "%.2f",Double.parseDouble(current.getHitsFromShots()) - Double.parseDouble(session.getHitsFromShots())));
        sessionDifferences.setAverageXp(String.format(Locale.US, "%.2f",Double.parseDouble(current.getAverageXp()) - Double.parseDouble(session.getAverageXp())));

        return sessionDifferences;
    }

    /**
     * Calculates differences between fields
     * @param current current statistics data
     * @param session selected session statistics data
     * @return the differences between the current data and then selected session data
     */
    private static StatisticsData calculateFieldDifferences(StatisticsData current, StatisticsData session) {
        StatisticsData result = new StatisticsData();

        Field[] fields = result.getClass().getDeclaredFields();

        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }

        for (String fieldName : fieldNames) {
            try {
                Field field = StatisticsData.class.getDeclaredField(fieldName);
                field.setAccessible(true);

                String currentValue = (String) field.get(current);
                String sessionValue = (String) field.get(session);

                if (currentValue != null && sessionValue != null) {
                    field.set(
                            result,
                            Integer.toString(
                                    Integer.parseInt(currentValue) - Integer.parseInt(sessionValue)
                            )
                    );
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static StatisticsData add(StatisticsData firstStatisticsData, StatisticsData secondStatisticsData) {
        StatisticsData result = new StatisticsData();

        String nickname;
        if (!firstStatisticsData.getNickname().equals("")) {
            nickname = firstStatisticsData.getNickname();
        } else {
            nickname = secondStatisticsData.getNickname();
        }
        result.setNickname(nickname);

        Field[] fields = result.getClass().getDeclaredFields();

        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }

        for (String fieldName : fieldNames) {
            try {
                Field field = StatisticsData.class.getDeclaredField(fieldName);
                field.setAccessible(true);

                String firstValue = (String) field.get(firstStatisticsData);
                String secondValue = (String) field.get(secondStatisticsData);

                if (firstValue != null && secondValue != null) {
                    field.set(
                            result,
                            Integer.toString(
                                    Integer.parseInt(firstValue) + Integer.parseInt(secondValue)
                            )
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        result.calculate(firstStatisticsData.isDetailedAverageDamage());
        return result;
    }

    public static StatisticsData parse(String json, String key, boolean detailedAverageDamage) {

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
        statisticsData.calculate(detailedAverageDamage);
        return statisticsData;
    }

    public static StatisticsData parse(String json, Collection<String> statisticsTypes, boolean detailedAverageDamage) {
        StatisticsData result = new StatisticsData();

        if (statisticsTypes.contains(Constants.PlayerStatisticsTypes.RANDOM)) {
            result = StatisticsDataUtils.add(result, parse(json, Constants.PlayerStatisticsTypes.RANDOM, detailedAverageDamage));
        }
        if (statisticsTypes.contains(Constants.PlayerStatisticsTypes.RATING)) {
            result = StatisticsDataUtils.add(result, parse(json, Constants.PlayerStatisticsTypes.RATING, detailedAverageDamage));
        }
        if (statisticsTypes.contains(Constants.PlayerStatisticsTypes.CLAN)) {
            result = StatisticsDataUtils.add(result, parse(json, Constants.PlayerStatisticsTypes.CLAN, detailedAverageDamage));
        }

        return result;
    }

}