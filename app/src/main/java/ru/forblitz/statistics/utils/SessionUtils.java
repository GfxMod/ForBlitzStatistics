package ru.forblitz.statistics.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import ru.forblitz.statistics.widget.data.PlayerFastStat;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import ru.forblitz.statistics.R;
import ru.forblitz.statistics.data.StatisticsData;
import ru.forblitz.statistics.data.StatisticsSet;

public class SessionUtils {

    /**
     * Calculates differences between the current data and then selected session data
     * @param current current statistics data
     * @param session selected session statistics data
     * @return the differences between the current data and then selected session data
     */
    @NonNull
    public static StatisticsData calculate(StatisticsData current, StatisticsData session) {
        StatisticsData sessionDifferences = new StatisticsData();

        sessionDifferences.setSpotted(Integer.toString(Integer.parseInt(current.getSpotted()) - Integer.parseInt(session.getSpotted())));
        sessionDifferences.setHits(Integer.toString(Integer.parseInt(current.getHits()) - Integer.parseInt(session.getHits())));
        sessionDifferences.setFrags(Integer.toString(Integer.parseInt(current.getFrags()) - Integer.parseInt(session.getFrags())));
        sessionDifferences.setMaxXp(Integer.toString(Integer.parseInt(current.getMaxXp()) - Integer.parseInt(session.getMaxXp())));
        sessionDifferences.setWins(Integer.toString(Integer.parseInt(current.getWins()) - Integer.parseInt(session.getWins())));
        sessionDifferences.setLosses(Integer.toString(Integer.parseInt(current.getLosses()) - Integer.parseInt(session.getLosses())));
        sessionDifferences.setCapturedPoints(Integer.toString(Integer.parseInt(current.getCapturedPoints()) - Integer.parseInt(session.getCapturedPoints())));
        sessionDifferences.setBattles(Integer.toString(Integer.parseInt(current.getBattles()) - Integer.parseInt(session.getBattles())));
        sessionDifferences.setDamageDealt(Integer.toString(Integer.parseInt(current.getDamageDealt()) - Integer.parseInt(session.getDamageDealt())));
        sessionDifferences.setDamageReceived(Integer.toString(Integer.parseInt(current.getDamageReceived()) - Integer.parseInt(session.getDamageReceived())));
        sessionDifferences.setShots(Integer.toString(Integer.parseInt(current.getShots()) - Integer.parseInt(session.getShots())));
        sessionDifferences.setFrags8p(Integer.toString(Integer.parseInt(current.getFrags8p()) - Integer.parseInt(session.getFrags8p())));
        sessionDifferences.setXp(Integer.toString(Integer.parseInt(current.getXp()) - Integer.parseInt(session.getXp())));
        sessionDifferences.setWinAndSurvived(Integer.toString(Integer.parseInt(current.getWinAndSurvived()) - Integer.parseInt(session.getWinAndSurvived())));
        sessionDifferences.setSurvivedBattles(Integer.toString(Integer.parseInt(current.getSurvivedBattles()) - Integer.parseInt(session.getSurvivedBattles())));
        sessionDifferences.setMaxFrags(Integer.toString(Integer.parseInt(current.getMaxFrags()) - Integer.parseInt(session.getMaxFrags())));
        sessionDifferences.setDroppedCapturePoints(Integer.toString(Integer.parseInt(current.getDroppedCapturePoints()) - Integer.parseInt(session.getDroppedCapturePoints())));
        sessionDifferences.setWinRate(String.format(Locale.US, "%.2f", Double.parseDouble(sessionDifferences.getWins()) / Double.parseDouble(sessionDifferences.getBattles()) * 100));
        sessionDifferences.setAverageDamage(String.format(Locale.US, "%.2f", Double.parseDouble(sessionDifferences.getDamageDealt()) / Double.parseDouble(sessionDifferences.getBattles())));
        sessionDifferences.setEfficiency(String.format(Locale.US, "%.2f", Double.parseDouble(sessionDifferences.getDamageDealt()) / Double.parseDouble(sessionDifferences.getDamageReceived())));
        sessionDifferences.setSurvived(String.format(Locale.US, "%.2f", Double.parseDouble(sessionDifferences.getSurvivedBattles()) / Double.parseDouble(sessionDifferences.getBattles()) * 100));
        sessionDifferences.setHitsFromShots(String.format(Locale.US, "%.2f", Double.parseDouble(sessionDifferences.getHits()) / Double.parseDouble(sessionDifferences.getShots()) * 100));
        sessionDifferences.setAverageXp(String.format(Locale.US, "%.2f", Double.parseDouble(sessionDifferences.getXp()) / Double.parseDouble(sessionDifferences.getBattles()) * 100));

        return sessionDifferences;
    }

    /**
     * Calculates differences between the current data and then selected session data
     * @param current current statistics data
     * @param session selected session statistics data
     * @return the differences between the current data and then selected session data
     */
    @NonNull
    public static StatisticsData calculateDifferences(StatisticsData current, StatisticsData session) {
        StatisticsData sessionDifferences = new StatisticsData();

        sessionDifferences.setSpotted(Integer.toString(Integer.parseInt(current.getSpotted()) - Integer.parseInt(session.getSpotted())));
        sessionDifferences.setHits(Integer.toString(Integer.parseInt(current.getHits()) - Integer.parseInt(session.getHits())));
        sessionDifferences.setFrags(Integer.toString(Integer.parseInt(current.getFrags()) - Integer.parseInt(session.getFrags())));
        sessionDifferences.setMaxXp(Integer.toString(Integer.parseInt(current.getMaxXp()) - Integer.parseInt(session.getMaxXp())));
        sessionDifferences.setWins(Integer.toString(Integer.parseInt(current.getWins()) - Integer.parseInt(session.getWins())));
        sessionDifferences.setLosses(Integer.toString(Integer.parseInt(current.getLosses()) - Integer.parseInt(session.getLosses())));
        sessionDifferences.setCapturedPoints(Integer.toString(Integer.parseInt(current.getCapturedPoints()) - Integer.parseInt(session.getCapturedPoints())));
        sessionDifferences.setBattles(Integer.toString(Integer.parseInt(current.getBattles()) - Integer.parseInt(session.getBattles())));
        sessionDifferences.setDamageDealt(Integer.toString(Integer.parseInt(current.getDamageDealt()) - Integer.parseInt(session.getDamageDealt())));
        sessionDifferences.setDamageReceived(Integer.toString(Integer.parseInt(current.getDamageReceived()) - Integer.parseInt(session.getDamageReceived())));
        sessionDifferences.setShots(Integer.toString(Integer.parseInt(current.getShots()) - Integer.parseInt(session.getShots())));
        sessionDifferences.setFrags8p(Integer.toString(Integer.parseInt(current.getFrags8p()) - Integer.parseInt(session.getFrags8p())));
        sessionDifferences.setXp(Integer.toString(Integer.parseInt(current.getXp()) - Integer.parseInt(session.getXp())));
        sessionDifferences.setWinAndSurvived(Integer.toString(Integer.parseInt(current.getWinAndSurvived()) - Integer.parseInt(session.getWinAndSurvived())));
        sessionDifferences.setSurvivedBattles(Integer.toString(Integer.parseInt(current.getSurvivedBattles()) - Integer.parseInt(session.getSurvivedBattles())));
        sessionDifferences.setMaxFrags(Integer.toString(Integer.parseInt(current.getMaxFrags()) - Integer.parseInt(session.getMaxFrags())));
        sessionDifferences.setDroppedCapturePoints(Integer.toString(Integer.parseInt(current.getDroppedCapturePoints()) - Integer.parseInt(session.getDroppedCapturePoints())));
        sessionDifferences.setWinRate(String.format(Locale.US, "%.2f",Double.parseDouble(current.getWinRate()) - Double.parseDouble(session.getWinRate())));
        sessionDifferences.setAverageDamage(String.format(Locale.US, "%.2f",Double.parseDouble(current.getAverageDamage()) - Double.parseDouble(session.getAverageDamage())));
        sessionDifferences.setEfficiency(String.format(Locale.US, "%.2f",Double.parseDouble(current.getEfficiency()) - Double.parseDouble(session.getEfficiency())));
        sessionDifferences.setSurvived(String.format(Locale.US, "%.2f",Double.parseDouble(current.getSurvived()) - Double.parseDouble(session.getSurvived())));
        sessionDifferences.setHitsFromShots(String.format(Locale.US, "%.2f",Double.parseDouble(current.getHitsFromShots()) - Double.parseDouble(session.getHitsFromShots())));
        sessionDifferences.setAverageXp(String.format(Locale.US, "%.2f",Double.parseDouble(current.getAverageXp()) - Double.parseDouble(session.getAverageXp())));

        return sessionDifferences;
    }

    /**
     * Sets all session values
     * @param activity required to get resources
     * @param baseStatisticsData current base statistics data
     * @param ratingStatisticsData current rating statistics data
     * @param sessionBaseDifferencesStatisticsData differences between the current base data and then selected session base data
     * @param sessionRatingDifferencesStatisticsData differences between the current rating data and then selected session rating data
     */
    public static void set(Activity activity, StatisticsData baseStatisticsData, StatisticsData ratingStatisticsData, StatisticsData sessionBaseDifferencesStatisticsData, StatisticsData sessionRatingDifferencesStatisticsData) {
        from(activity, baseStatisticsData, ratingStatisticsData);

        ((PlayerFastStat) activity.findViewById(R.id.random_fast_stat)).setSessionData(sessionBaseDifferencesStatisticsData);
        ((PlayerFastStat) activity.findViewById(R.id.rating_fast_stat)).setSessionData(sessionRatingDifferencesStatisticsData);

    }

    /**
     * Replaces all statistics with the value of the difference between the current statistics and session statistics
     * @param activity required to get resources
     * @param baseStatisticsData current base statistics data
     * @param sessionBaseStatisticsData session base statistics data
     * @param ratingStatisticsData current rating statistics data
     * @param sessionRatingStatisticsData session rating statistics data
     */
    public static void to(Activity activity, StatisticsData baseStatisticsData, StatisticsData sessionBaseStatisticsData, StatisticsData ratingStatisticsData, StatisticsData sessionRatingStatisticsData) {
        TextView randomSessionStatButton = activity.findViewById(R.id.random_session_stat_button);
        ViewFlipper fragmentRandom = activity.findViewById(R.id.fragment_random);
        ViewFlipper fragmentRating = activity.findViewById(R.id.fragment_rating);

        fragmentRandom.startAnimation(fragmentRandom.getOutAnimation());
        fragmentRating.startAnimation(fragmentRating.getOutAnimation());

        new Handler().postDelayed(() -> {
            StatisticsSet.setBaseStatistics(activity, calculate(baseStatisticsData, sessionBaseStatisticsData));
            StatisticsSet.setRatingStatistics(activity, calculate(ratingStatisticsData, sessionRatingStatisticsData));
            randomSessionStatButton.setText(activity.getString(R.string.from_session_stat));
            randomSessionStatButton.setActivated(true);
        }, 125);

    }

    /**
     * Replaces all statistics with the value of the current statistics
     * @param activity required to get resources
     * @param baseStatisticsData current base statistics data
     * @param ratingStatisticsData current rating statistics data
     */
    public static void from(Activity activity, StatisticsData baseStatisticsData, StatisticsData ratingStatisticsData) {
        TextView randomSessionStatButton = activity.findViewById(R.id.random_session_stat_button);
        ViewFlipper fragmentRandom = activity.findViewById(R.id.fragment_random);
        ViewFlipper fragmentRating = activity.findViewById(R.id.fragment_rating);

        fragmentRandom.startAnimation(fragmentRandom.getOutAnimation());
        fragmentRating.startAnimation(fragmentRating.getOutAnimation());

        new Handler().postDelayed(() -> {
            StatisticsSet.setBaseStatistics(activity, baseStatisticsData);
            StatisticsSet.setRatingStatistics(activity, ratingStatisticsData);
            randomSessionStatButton.setText(activity.getString(R.string.to_session_stat));
            randomSessionStatButton.setActivated(false);
        }, 125);

    }

    public static ArrayList<String> getSessions(Context context, String userID, String region) throws IOException {
        ArrayList<String> files = new ArrayList<>();

        Files.walk(Paths.get(getSessionDir(context).toString()))
                .filter( Files::isRegularFile )
                .filter(i -> i.toString().substring(i.toString().lastIndexOf("/") + 1, i.toString().indexOf("-")).equals(userID))
                .filter(i -> i.toString().substring(i.toString().lastIndexOf(".") + 1).equals(region))
                .forEach(i -> files.add(i.toString()));

        Collections.sort(files);
        Collections.reverse(files);

        return files;
    }

    public static File getSessionDir(Context context) {
        return new File(context.getFilesDir(), "sessions");
    }

    public static void setButtonsVisibility(Activity activity, ButtonsVisibility variant) {
        View randomSessionsStatButton = activity.findViewById(R.id.random_session_stat_button);
        View randomSessionsListButton = activity.findViewById(R.id.random_sessions_list_button);
        if (variant == ButtonsVisibility.NOTHING) {
            randomSessionsStatButton.setVisibility(View.GONE);
            randomSessionsListButton.setVisibility(View.GONE);
        }
        if (variant == ButtonsVisibility.ONLY_FLIP) {
            randomSessionsStatButton.setVisibility(View.VISIBLE);
            randomSessionsListButton.setVisibility(View.GONE);
        }
        if (variant == ButtonsVisibility.ALL) {
            randomSessionsStatButton.setVisibility(View.VISIBLE);
            randomSessionsListButton.setVisibility(View.VISIBLE);
        }
    }

    public enum ButtonsVisibility {
        NOTHING,
        ONLY_FLIP,
        ALL
    }

}