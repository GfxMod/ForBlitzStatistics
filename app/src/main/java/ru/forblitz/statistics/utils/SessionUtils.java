package ru.forblitz.statistics.utils;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;

import ru.forblitz.statistics.R;
import ru.forblitz.statistics.data.StatisticsData;
import ru.forblitz.statistics.data.StatisticsSet;

import java.util.Locale;

public class SessionUtils {

    /**
     * Hides all session elements; called when the account you are looking for does not have any saved sessions
     * @param activity required to get resources
     */
    public static void hide(Activity activity) {

        View textBattlesSessionInfo = activity.findViewById(R.id.text_battles_session_info);
        View textWinsSessionInfo = activity.findViewById(R.id.text_win_rate_session_info);
        View textDamageSessionInfo = activity.findViewById(R.id.text_average_damage_session_info);
        View textEfficiencySessionInfo = activity.findViewById(R.id.text_efficiency_session_info);
        View textSurvivedSessionInfo = activity.findViewById(R.id.text_survived_session_info);
        View textHitsFromShotsSessionInfo = activity.findViewById(R.id.text_hits_from_shots_session_info);
        View textBattlesRatingSessionInfo = activity.findViewById(R.id.rating_text_battles_session_info);
        View textWinsRatingSessionInfo = activity.findViewById(R.id.rating_text_win_rate_session_info);
        View textDamageRatingSessionInfo = activity.findViewById(R.id.rating_text_average_damage_session_info);
        View textEfficiencyRatingSessionInfo = activity.findViewById(R.id.rating_text_efficiency_session_info);
        View textSurvivedRatingSessionInfo = activity.findViewById(R.id.rating_text_survived_session_info);
        View textHitsFromShotsRatingSessionInfo = activity.findViewById(R.id.rating_text_hits_from_shots_session_info);

        View randomSessionsButtonView = activity.findViewById(R.id.random_sessions_button);
        View randomSessionsViewView = activity.findViewById(R.id.random_sessions_view);
        View randomSessionStatButtonView = activity.findViewById(R.id.random_session_stat_button);
        View randomSessionStatViewView = activity.findViewById(R.id.random_session_stat_view);

        textBattlesSessionInfo.setVisibility(View.INVISIBLE);
        textWinsSessionInfo.setVisibility(View.INVISIBLE);
        textDamageSessionInfo.setVisibility(View.INVISIBLE);
        textEfficiencySessionInfo.setVisibility(View.INVISIBLE);
        textSurvivedSessionInfo.setVisibility(View.INVISIBLE);
        textHitsFromShotsSessionInfo.setVisibility(View.INVISIBLE);
        textBattlesRatingSessionInfo.setVisibility(View.INVISIBLE);
        textWinsRatingSessionInfo.setVisibility(View.INVISIBLE);
        textDamageRatingSessionInfo.setVisibility(View.INVISIBLE);
        textEfficiencyRatingSessionInfo.setVisibility(View.INVISIBLE);
        textSurvivedRatingSessionInfo.setVisibility(View.INVISIBLE);
        textHitsFromShotsRatingSessionInfo.setVisibility(View.INVISIBLE);

        randomSessionsButtonView.setVisibility(View.GONE);
        randomSessionsViewView.setVisibility(View.VISIBLE);
        randomSessionStatButtonView.setVisibility(View.GONE);
        randomSessionStatViewView.setVisibility(View.VISIBLE);

    }

    /**
     * Shows all session elements; called when the account you are looking for has any saved sessions
     * @param activity required to get resources
     */
    public static void show(Activity activity) {

        View textBattlesSessionInfo = activity.findViewById(R.id.text_battles_session_info);
        View textWinsSessionInfo = activity.findViewById(R.id.text_win_rate_session_info);
        View textDamageSessionInfo = activity.findViewById(R.id.text_average_damage_session_info);
        View textEfficiencySessionInfo = activity.findViewById(R.id.text_efficiency_session_info);
        View textSurvivedSessionInfo = activity.findViewById(R.id.text_survived_session_info);
        View textHitsFromShotsSessionInfo = activity.findViewById(R.id.text_hits_from_shots_session_info);
        View textBattlesRatingSessionInfo = activity.findViewById(R.id.rating_text_battles_session_info);
        View textWinsRatingSessionInfo = activity.findViewById(R.id.rating_text_win_rate_session_info);
        View textDamageRatingSessionInfo = activity.findViewById(R.id.rating_text_average_damage_session_info);
        View textEfficiencyRatingSessionInfo = activity.findViewById(R.id.rating_text_efficiency_session_info);
        View textSurvivedRatingSessionInfo = activity.findViewById(R.id.rating_text_survived_session_info);
        View textHitsFromShotsRatingSessionInfo = activity.findViewById(R.id.rating_text_hits_from_shots_session_info);

        View randomSessionsButtonView = activity.findViewById(R.id.random_sessions_button);
        View randomSessionsViewView = activity.findViewById(R.id.random_sessions_view);
        View randomSessionStatButtonView = activity.findViewById(R.id.random_session_stat_button);
        View randomSessionStatViewView = activity.findViewById(R.id.random_session_stat_view);

        textBattlesSessionInfo.setVisibility(View.VISIBLE);
        textWinsSessionInfo.setVisibility(View.VISIBLE);
        textDamageSessionInfo.setVisibility(View.VISIBLE);
        textEfficiencySessionInfo.setVisibility(View.VISIBLE);
        textSurvivedSessionInfo.setVisibility(View.VISIBLE);
        textHitsFromShotsSessionInfo.setVisibility(View.VISIBLE);
        textBattlesRatingSessionInfo.setVisibility(View.VISIBLE);
        textWinsRatingSessionInfo.setVisibility(View.VISIBLE);
        textDamageRatingSessionInfo.setVisibility(View.VISIBLE);
        textEfficiencyRatingSessionInfo.setVisibility(View.VISIBLE);
        textSurvivedRatingSessionInfo.setVisibility(View.VISIBLE);
        textHitsFromShotsRatingSessionInfo.setVisibility(View.VISIBLE);

        randomSessionsButtonView.setVisibility(View.VISIBLE);
        randomSessionsViewView.setVisibility(View.GONE);
        randomSessionStatButtonView.setVisibility(View.VISIBLE);
        randomSessionStatViewView.setVisibility(View.GONE);

    }

    /**
     * Hides base session elements; called when the selected session has the same number of random battles as now
     * @param activity required to get resources
     */
    public static void hideBase(Activity activity) {

        View textBattlesSessionInfo = activity.findViewById(R.id.text_battles_session_info);
        View textWinsSessionInfo = activity.findViewById(R.id.text_win_rate_session_info);
        View textDamageSessionInfo = activity.findViewById(R.id.text_average_damage_session_info);
        View textEfficiencySessionInfo = activity.findViewById(R.id.text_efficiency_session_info);
        View textSurvivedSessionInfo = activity.findViewById(R.id.text_survived_session_info);
        View textHitsFromShotsSessionInfo = activity.findViewById(R.id.text_hits_from_shots_session_info);

        textBattlesSessionInfo.setVisibility(View.INVISIBLE);
        textWinsSessionInfo.setVisibility(View.INVISIBLE);
        textDamageSessionInfo.setVisibility(View.INVISIBLE);
        textEfficiencySessionInfo.setVisibility(View.INVISIBLE);
        textSurvivedSessionInfo.setVisibility(View.INVISIBLE);
        textHitsFromShotsSessionInfo.setVisibility(View.INVISIBLE);

    }

    /**
     * Hides rating session elements; called when the selected session has the same number of rating battles as now
     * @param activity required to get resources
     */
    public static void hideRating(Activity activity) {

        View textBattlesRatingSessionInfo = activity.findViewById(R.id.rating_text_battles_session_info);
        View textWinsRatingSessionInfo = activity.findViewById(R.id.rating_text_win_rate_session_info);
        View textDamageRatingSessionInfo = activity.findViewById(R.id.rating_text_average_damage_session_info);
        View textEfficiencyRatingSessionInfo = activity.findViewById(R.id.rating_text_efficiency_session_info);
        View textSurvivedRatingSessionInfo = activity.findViewById(R.id.rating_text_survived_session_info);
        View textHitsFromShotsRatingSessionInfo = activity.findViewById(R.id.rating_text_hits_from_shots_session_info);

        textBattlesRatingSessionInfo.setVisibility(View.INVISIBLE);
        textWinsRatingSessionInfo.setVisibility(View.INVISIBLE);
        textDamageRatingSessionInfo.setVisibility(View.INVISIBLE);
        textEfficiencyRatingSessionInfo.setVisibility(View.INVISIBLE);
        textSurvivedRatingSessionInfo.setVisibility(View.INVISIBLE);
        textHitsFromShotsRatingSessionInfo.setVisibility(View.INVISIBLE);

    }

    /**
     * Hides session selection button; called when the account you are looking for has only one suitable session
     * @param activity required to get resources
     */
    public static void hideSelect(Activity activity) {
        View randomSessionsButtonView = activity.findViewById(R.id.random_sessions_button);
        View randomSessionsView = activity.findViewById(R.id.random_sessions_view);
        randomSessionsButtonView.setVisibility(View.GONE);
        randomSessionsView.setVisibility(View.VISIBLE);
    }

    /**
     * Shows session selection button; called when the account you are looking for has more then one suitable session
     * @param activity required to get resources
     */
    public static void showSelect(Activity activity) {
        View randomSessionsButtonView = activity.findViewById(R.id.random_sessions_button);
        View randomSessionsView = activity.findViewById(R.id.random_sessions_view);
        randomSessionsButtonView.setVisibility(View.VISIBLE);
        randomSessionsView.setVisibility(View.GONE);
    }

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

        TextView textBattlesSessionInfo = activity.findViewById(R.id.text_battles_session_info);
        TextView textWinRateSessionInfo = activity.findViewById(R.id.text_win_rate_session_info);
        TextView textAverageDamageSessionInfo = activity.findViewById(R.id.text_average_damage_session_info);
        TextView textEfficiencySessionInfo = activity.findViewById(R.id.text_efficiency_session_info);
        TextView textSurvivedSessionInfo = activity.findViewById(R.id.text_survived_session_info);
        TextView textHitsFromShotsSessionInfo = activity.findViewById(R.id.text_hits_from_shots_session_info);
        
        TextView textBattlesRatingSessionInfo = activity.findViewById(R.id.rating_text_battles_session_info);
        TextView textWinRateRatingSessionInfo = activity.findViewById(R.id.rating_text_win_rate_session_info);
        TextView textAverageDamageRatingSessionInfo = activity.findViewById(R.id.rating_text_average_damage_session_info);
        TextView textEfficiencyRatingSessionInfo = activity.findViewById(R.id.rating_text_efficiency_session_info);
        TextView textSurvivedRatingSessionInfo = activity.findViewById(R.id.rating_text_survived_session_info);
        TextView textHitsFromShotsRatingSessionInfo = activity.findViewById(R.id.rating_text_hits_from_shots_session_info);

        if (Integer.parseInt(sessionBaseDifferencesStatisticsData.getBattles()) > 0) {
            
            String valueBattles = "+" + sessionBaseDifferencesStatisticsData.getBattles();
            InterfaceUtils.setSessionTrueValue(activity, textBattlesSessionInfo, valueBattles);

            if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getWinRate()) == 0) {
                textWinRateSessionInfo.setVisibility(View.INVISIBLE);
            } else if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getWinRate()) > 0) {
                String value = "+" + sessionBaseDifferencesStatisticsData.getWinRate() + "%";
                InterfaceUtils.setSessionTrueValue(activity, textWinRateSessionInfo, value);
            } else if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getWinRate()) < 0) {
                String value = sessionBaseDifferencesStatisticsData.getWinRate() + "%";
                InterfaceUtils.setSessionFalseValue(activity, textWinRateSessionInfo, value);
            }

            if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getAverageDamage()) == 0) {
                textAverageDamageSessionInfo.setVisibility(View.INVISIBLE);
            } else if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getAverageDamage()) > 0) {
                String value = "+" + sessionBaseDifferencesStatisticsData.getAverageDamage();
                InterfaceUtils.setSessionTrueValue(activity, textAverageDamageSessionInfo, value);
            } else if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getAverageDamage()) < 0) {
                String value = sessionBaseDifferencesStatisticsData.getAverageDamage();
                InterfaceUtils.setSessionFalseValue(activity, textAverageDamageSessionInfo, value);
            }

            if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getEfficiency()) == 0) {
                textEfficiencySessionInfo.setVisibility(View.INVISIBLE);
            } else if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getEfficiency()) > 0) {
                String value = "+" + sessionBaseDifferencesStatisticsData.getEfficiency() + "%";
                InterfaceUtils.setSessionTrueValue(activity, textEfficiencySessionInfo, value);
            } else if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getEfficiency()) < 0) {
                String value = sessionBaseDifferencesStatisticsData.getEfficiency() + "%";
                InterfaceUtils.setSessionFalseValue(activity, textEfficiencySessionInfo, value);
            }

            if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getSurvived()) == 0) {
                textSurvivedSessionInfo.setVisibility(View.INVISIBLE);
            } else if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getSurvived()) > 0) {
                String value = "+" + sessionBaseDifferencesStatisticsData.getSurvived() + "%";
                InterfaceUtils.setSessionTrueValue(activity, textSurvivedSessionInfo, value);
            } else if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getSurvived()) < 0) {
                String value = sessionBaseDifferencesStatisticsData.getSurvived() + "%";
                InterfaceUtils.setSessionFalseValue(activity, textSurvivedSessionInfo, value);
            }

            if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getHitsFromShots()) == 0) {
                textHitsFromShotsSessionInfo.setVisibility(View.INVISIBLE);
            } else if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getHitsFromShots()) > 0) {
                String value = "+" + sessionBaseDifferencesStatisticsData.getHitsFromShots() + "%";
                InterfaceUtils.setSessionTrueValue(activity, textHitsFromShotsSessionInfo, value);
            } else if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getHitsFromShots()) < 0) {
                String value = sessionBaseDifferencesStatisticsData.getHitsFromShots() + "%";
                InterfaceUtils.setSessionFalseValue(activity, textHitsFromShotsSessionInfo, value);
            }

        } else {
            hideBase(activity);
        }

        if (Integer.parseInt(sessionRatingDifferencesStatisticsData.getBattles()) > 0) {

            String valueBattles = "+" + sessionRatingDifferencesStatisticsData.getBattles();
            InterfaceUtils.setSessionTrueValue(activity, textBattlesRatingSessionInfo, valueBattles);

            if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getWinRate()) == 0) {
                textWinRateRatingSessionInfo.setVisibility(View.INVISIBLE);
            } else if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getWinRate()) > 0) {
                String value = "+" + sessionRatingDifferencesStatisticsData.getWinRate() + "%";
                InterfaceUtils.setSessionTrueValue(activity, textWinRateRatingSessionInfo, value);
            } else if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getWinRate()) < 0) {
                String value = sessionRatingDifferencesStatisticsData.getWinRate() + "%";
                InterfaceUtils.setSessionFalseValue(activity, textWinRateRatingSessionInfo, value);
            }

            if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getAverageDamage()) == 0) {
                textAverageDamageRatingSessionInfo.setVisibility(View.INVISIBLE);
            } else if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getAverageDamage()) > 0) {
                String value = "+" + sessionRatingDifferencesStatisticsData.getAverageDamage();
                InterfaceUtils.setSessionTrueValue(activity, textAverageDamageRatingSessionInfo, value);
            } else if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getAverageDamage()) < 0) {
                String value = sessionRatingDifferencesStatisticsData.getAverageDamage();
                InterfaceUtils.setSessionFalseValue(activity, textAverageDamageRatingSessionInfo, value);
            }

            if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getEfficiency()) == 0) {
                textEfficiencyRatingSessionInfo.setVisibility(View.INVISIBLE);
            } else if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getEfficiency()) > 0) {
                String value = "+" + sessionRatingDifferencesStatisticsData.getEfficiency() + "%";
                InterfaceUtils.setSessionTrueValue(activity, textEfficiencyRatingSessionInfo, value);
            } else if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getEfficiency()) < 0) {
                String value = sessionRatingDifferencesStatisticsData.getEfficiency() + "%";
                InterfaceUtils.setSessionFalseValue(activity, textEfficiencyRatingSessionInfo, value);
            }

            if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getSurvived()) == 0) {
                textSurvivedRatingSessionInfo.setVisibility(View.INVISIBLE);
            } else if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getSurvived()) > 0) {
                String value = "+" + sessionRatingDifferencesStatisticsData.getSurvived() + "%";
                InterfaceUtils.setSessionTrueValue(activity, textSurvivedRatingSessionInfo, value);
            } else if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getSurvived()) < 0) {
                String value = sessionRatingDifferencesStatisticsData.getSurvived() + "%";
                InterfaceUtils.setSessionFalseValue(activity, textSurvivedRatingSessionInfo, value);
            }

            if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getHitsFromShots()) == 0) {
                textHitsFromShotsRatingSessionInfo.setVisibility(View.INVISIBLE);
            } else if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getHitsFromShots()) > 0) {
                String value = "+" + sessionRatingDifferencesStatisticsData.getHitsFromShots() + "%";
                InterfaceUtils.setSessionTrueValue(activity, textHitsFromShotsRatingSessionInfo, value);
            } else if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getHitsFromShots()) < 0) {
                String value = sessionRatingDifferencesStatisticsData.getHitsFromShots() + "%";
                InterfaceUtils.setSessionFalseValue(activity, textHitsFromShotsRatingSessionInfo, value);
            }

        } else {
            hideRating(activity);
        }

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

}