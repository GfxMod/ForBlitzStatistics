package ru.forblitz.statistics.data;

import static android.graphics.Typeface.BOLD;
import static androidx.core.widget.TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import ru.forblitz.statistics.R.color;
import ru.forblitz.statistics.R.drawable;
import ru.forblitz.statistics.R.font;
import ru.forblitz.statistics.R.id;
import ru.forblitz.statistics.R.string;
import ru.forblitz.statistics.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Session {

    /**
     * Hides all session elements; called when the account you are looking for does not have any saved sessions
     * @param activity required to get resources
     */
    public static void hide(Activity activity) {

        View textBattlesSessionInfo = activity.findViewById(id.text_battles_session_info);
        View textWinsSessionInfo = activity.findViewById(id.text_win_rate_session_info);
        View textDamageSessionInfo = activity.findViewById(id.text_average_damage_session_info);
        View textEfficiencySessionInfo = activity.findViewById(id.text_efficiency_session_info);
        View textSurvivedSessionInfo = activity.findViewById(id.text_survived_session_info);
        View textHitsFromShotsSessionInfo = activity.findViewById(id.text_hits_from_shots_session_info);
        View textBattlesRatingSessionInfo = activity.findViewById(id.rating_text_battles_session_info);
        View textWinsRatingSessionInfo = activity.findViewById(id.rating_text_win_rate_session_info);
        View textDamageRatingSessionInfo = activity.findViewById(id.rating_text_average_damage_session_info);
        View textEfficiencyRatingSessionInfo = activity.findViewById(id.rating_text_efficiency_session_info);
        View textSurvivedRatingSessionInfo = activity.findViewById(id.rating_text_survived_session_info);
        View textHitsFromShotsRatingSessionInfo = activity.findViewById(id.rating_text_hits_from_shots_session_info);

        View randomSessionsButtonView = activity.findViewById(id.random_sessions_button);
        View randomSessionsViewView = activity.findViewById(id.random_sessions_view);
        View randomSessionStatButtonView = activity.findViewById(id.random_session_stat_button);
        View randomSessionStatViewView = activity.findViewById(id.random_session_stat_view);

        LinearLayout randomSessionsListLayoutView = activity.findViewById(id.random_sessions_list_layout);

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

        randomSessionsListLayoutView.removeAllViews();

    }

    /**
     * Shows all session elements; called when the account you are looking for has any saved sessions
     * @param activity required to get resources
     */
    public static void show(Activity activity) {

        View textBattlesSessionInfo = activity.findViewById(id.text_battles_session_info);
        View textWinsSessionInfo = activity.findViewById(id.text_win_rate_session_info);
        View textDamageSessionInfo = activity.findViewById(id.text_average_damage_session_info);
        View textEfficiencySessionInfo = activity.findViewById(id.text_efficiency_session_info);
        View textSurvivedSessionInfo = activity.findViewById(id.text_survived_session_info);
        View textHitsFromShotsSessionInfo = activity.findViewById(id.text_hits_from_shots_session_info);
        View textBattlesRatingSessionInfo = activity.findViewById(id.rating_text_battles_session_info);
        View textWinsRatingSessionInfo = activity.findViewById(id.rating_text_win_rate_session_info);
        View textDamageRatingSessionInfo = activity.findViewById(id.rating_text_average_damage_session_info);
        View textEfficiencyRatingSessionInfo = activity.findViewById(id.rating_text_efficiency_session_info);
        View textSurvivedRatingSessionInfo = activity.findViewById(id.rating_text_survived_session_info);
        View textHitsFromShotsRatingSessionInfo = activity.findViewById(id.rating_text_hits_from_shots_session_info);

        View randomSessionsButtonView = activity.findViewById(id.random_sessions_button);
        View randomSessionsViewView = activity.findViewById(id.random_sessions_view);
        View randomSessionStatButtonView = activity.findViewById(id.random_session_stat_button);
        View randomSessionStatViewView = activity.findViewById(id.random_session_stat_view);

        LinearLayout randomSessionsListLayoutView = activity.findViewById(id.random_sessions_list_layout);

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

        randomSessionsListLayoutView.removeAllViews();

    }

    /**
     * Hides base session elements; called when the selected session has the same number of random battles as now
     * @param activity required to get resources
     */
    public static void hideBase(Activity activity) {

        View textBattlesSessionInfo = activity.findViewById(id.text_battles_session_info);
        View textWinsSessionInfo = activity.findViewById(id.text_win_rate_session_info);
        View textDamageSessionInfo = activity.findViewById(id.text_average_damage_session_info);
        View textEfficiencySessionInfo = activity.findViewById(id.text_efficiency_session_info);
        View textSurvivedSessionInfo = activity.findViewById(id.text_survived_session_info);
        View textHitsFromShotsSessionInfo = activity.findViewById(id.text_hits_from_shots_session_info);

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

        View textBattlesRatingSessionInfo = activity.findViewById(id.rating_text_battles_session_info);
        View textWinsRatingSessionInfo = activity.findViewById(id.rating_text_win_rate_session_info);
        View textDamageRatingSessionInfo = activity.findViewById(id.rating_text_average_damage_session_info);
        View textEfficiencyRatingSessionInfo = activity.findViewById(id.rating_text_efficiency_session_info);
        View textSurvivedRatingSessionInfo = activity.findViewById(id.rating_text_survived_session_info);
        View textHitsFromShotsRatingSessionInfo = activity.findViewById(id.rating_text_hits_from_shots_session_info);

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
        View randomSessionsButtonView = activity.findViewById(id.random_sessions_button);
        View randomSessionsView = activity.findViewById(id.random_sessions_view);
        randomSessionsButtonView.setVisibility(View.GONE);
        randomSessionsView.setVisibility(View.VISIBLE);
    }

    /**
     * Shows session selection button; called when the account you are looking for has more then one suitable session
     * @param activity required to get resources
     */
    public static void showSelect(Activity activity) {
        View randomSessionsButtonView = activity.findViewById(id.random_sessions_button);
        View randomSessionsView = activity.findViewById(id.random_sessions_view);
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

        TextView textBattlesSessionInfo = activity.findViewById(id.text_battles_session_info);
        TextView textWinRateSessionInfo = activity.findViewById(id.text_win_rate_session_info);
        TextView textAverageDamageSessionInfo = activity.findViewById(id.text_average_damage_session_info);
        TextView textEfficiencySessionInfo = activity.findViewById(id.text_efficiency_session_info);
        TextView textSurvivedSessionInfo = activity.findViewById(id.text_survived_session_info);
        TextView textHitsFromShotsSessionInfo = activity.findViewById(id.text_hits_from_shots_session_info);
        
        TextView textBattlesRatingSessionInfo = activity.findViewById(id.rating_text_battles_session_info);
        TextView textWinRateRatingSessionInfo = activity.findViewById(id.rating_text_win_rate_session_info);
        TextView textAverageDamageRatingSessionInfo = activity.findViewById(id.rating_text_average_damage_session_info);
        TextView textEfficiencyRatingSessionInfo = activity.findViewById(id.rating_text_efficiency_session_info);
        TextView textSurvivedRatingSessionInfo = activity.findViewById(id.rating_text_survived_session_info);
        TextView textHitsFromShotsRatingSessionInfo = activity.findViewById(id.rating_text_hits_from_shots_session_info);

        if (Integer.parseInt(sessionBaseDifferencesStatisticsData.getBattles()) > 0) {
            
            String valueBattles = "+" + sessionBaseDifferencesStatisticsData.getBattles();
            Utils.setSessionTrueValue(activity, textBattlesSessionInfo, valueBattles);

            if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getWinRate()) == 0) {
                textWinRateSessionInfo.setVisibility(View.INVISIBLE);
            } else if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getWinRate()) > 0) {
                String value = "+" + sessionBaseDifferencesStatisticsData.getWinRate() + "%";
                Utils.setSessionTrueValue(activity, textWinRateSessionInfo, value);
            } else if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getWinRate()) < 0) {
                String value = sessionBaseDifferencesStatisticsData.getWinRate() + "%";
                Utils.setSessionFalseValue(activity, textWinRateSessionInfo, value);
            }

            if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getAverageDamage()) == 0) {
                textAverageDamageSessionInfo.setVisibility(View.INVISIBLE);
            } else if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getAverageDamage()) > 0) {
                String value = "+" + sessionBaseDifferencesStatisticsData.getAverageDamage();
                Utils.setSessionTrueValue(activity, textAverageDamageSessionInfo, value);
            } else if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getAverageDamage()) < 0) {
                String value = sessionBaseDifferencesStatisticsData.getAverageDamage();
                Utils.setSessionFalseValue(activity, textAverageDamageSessionInfo, value);
            }

            if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getEfficiency()) == 0) {
                textEfficiencySessionInfo.setVisibility(View.INVISIBLE);
            } else if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getEfficiency()) > 0) {
                String value = "+" + sessionBaseDifferencesStatisticsData.getEfficiency() + "%";
                Utils.setSessionTrueValue(activity, textEfficiencySessionInfo, value);
            } else if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getEfficiency()) < 0) {
                String value = sessionBaseDifferencesStatisticsData.getEfficiency() + "%";
                Utils.setSessionFalseValue(activity, textEfficiencySessionInfo, value);
            }

            if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getSurvived()) == 0) {
                textSurvivedSessionInfo.setVisibility(View.INVISIBLE);
            } else if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getSurvived()) > 0) {
                String value = "+" + sessionBaseDifferencesStatisticsData.getSurvived() + "%";
                Utils.setSessionTrueValue(activity, textSurvivedSessionInfo, value);
            } else if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getSurvived()) < 0) {
                String value = sessionBaseDifferencesStatisticsData.getSurvived() + "%";
                Utils.setSessionFalseValue(activity, textSurvivedSessionInfo, value);
            }

            if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getHitsFromShots()) == 0) {
                textHitsFromShotsSessionInfo.setVisibility(View.INVISIBLE);
            } else if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getHitsFromShots()) > 0) {
                String value = "+" + sessionBaseDifferencesStatisticsData.getHitsFromShots() + "%";
                Utils.setSessionTrueValue(activity, textHitsFromShotsSessionInfo, value);
            } else if (Double.parseDouble(sessionBaseDifferencesStatisticsData.getHitsFromShots()) < 0) {
                String value = sessionBaseDifferencesStatisticsData.getHitsFromShots() + "%";
                Utils.setSessionFalseValue(activity, textHitsFromShotsSessionInfo, value);
            }

        } else {
            hideBase(activity);
        }

        if (Integer.parseInt(sessionRatingDifferencesStatisticsData.getBattles()) > 0) {

            String valueBattles = "+" + sessionRatingDifferencesStatisticsData.getBattles();
            Utils.setSessionTrueValue(activity, textBattlesRatingSessionInfo, valueBattles);

            if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getWinRate()) == 0) {
                textWinRateRatingSessionInfo.setVisibility(View.INVISIBLE);
            } else if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getWinRate()) > 0) {
                String value = "+" + sessionRatingDifferencesStatisticsData.getWinRate() + "%";
                Utils.setSessionTrueValue(activity, textWinRateRatingSessionInfo, value);
            } else if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getWinRate()) < 0) {
                String value = sessionRatingDifferencesStatisticsData.getWinRate() + "%";
                Utils.setSessionFalseValue(activity, textWinRateRatingSessionInfo, value);
            }

            if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getAverageDamage()) == 0) {
                textAverageDamageRatingSessionInfo.setVisibility(View.INVISIBLE);
            } else if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getAverageDamage()) > 0) {
                String value = "+" + sessionRatingDifferencesStatisticsData.getAverageDamage();
                Utils.setSessionTrueValue(activity, textAverageDamageRatingSessionInfo, value);
            } else if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getAverageDamage()) < 0) {
                String value = sessionRatingDifferencesStatisticsData.getAverageDamage();
                Utils.setSessionFalseValue(activity, textAverageDamageRatingSessionInfo, value);
            }

            if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getEfficiency()) == 0) {
                textEfficiencyRatingSessionInfo.setVisibility(View.INVISIBLE);
            } else if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getEfficiency()) > 0) {
                String value = "+" + sessionRatingDifferencesStatisticsData.getEfficiency() + "%";
                Utils.setSessionTrueValue(activity, textEfficiencyRatingSessionInfo, value);
            } else if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getEfficiency()) < 0) {
                String value = sessionRatingDifferencesStatisticsData.getEfficiency() + "%";
                Utils.setSessionFalseValue(activity, textEfficiencyRatingSessionInfo, value);
            }

            if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getSurvived()) == 0) {
                textSurvivedRatingSessionInfo.setVisibility(View.INVISIBLE);
            } else if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getSurvived()) > 0) {
                String value = "+" + sessionRatingDifferencesStatisticsData.getSurvived() + "%";
                Utils.setSessionTrueValue(activity, textSurvivedRatingSessionInfo, value);
            } else if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getSurvived()) < 0) {
                String value = sessionRatingDifferencesStatisticsData.getSurvived() + "%";
                Utils.setSessionFalseValue(activity, textSurvivedRatingSessionInfo, value);
            }

            if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getHitsFromShots()) == 0) {
                textHitsFromShotsRatingSessionInfo.setVisibility(View.INVISIBLE);
            } else if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getHitsFromShots()) > 0) {
                String value = "+" + sessionRatingDifferencesStatisticsData.getHitsFromShots() + "%";
                Utils.setSessionTrueValue(activity, textHitsFromShotsRatingSessionInfo, value);
            } else if (Double.parseDouble(sessionRatingDifferencesStatisticsData.getHitsFromShots()) < 0) {
                String value = sessionRatingDifferencesStatisticsData.getHitsFromShots() + "%";
                Utils.setSessionFalseValue(activity, textHitsFromShotsRatingSessionInfo, value);
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
        TextView randomSessionStatButton = activity.findViewById(id.random_session_stat_button);
        ViewFlipper fragmentRandom = activity.findViewById(id.fragment_random);
        ViewFlipper fragmentRating = activity.findViewById(id.fragment_rating);

        fragmentRandom.startAnimation(fragmentRandom.getOutAnimation());
        fragmentRating.startAnimation(fragmentRating.getOutAnimation());

        new Handler().postDelayed(() -> {
            StatisticsSet.setBaseStatistics(activity, calculate(baseStatisticsData, sessionBaseStatisticsData));
            StatisticsSet.setRatingStatistics(activity, calculate(ratingStatisticsData, sessionRatingStatisticsData));
            randomSessionStatButton.setText(activity.getString(string.from_session_stat));
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
        TextView randomSessionStatButton = activity.findViewById(id.random_session_stat_button);
        ViewFlipper fragmentRandom = activity.findViewById(id.fragment_random);
        ViewFlipper fragmentRating = activity.findViewById(id.fragment_rating);

        fragmentRandom.startAnimation(fragmentRandom.getOutAnimation());
        fragmentRating.startAnimation(fragmentRating.getOutAnimation());

        new Handler().postDelayed(() -> {
            StatisticsSet.setBaseStatistics(activity, baseStatisticsData);
            StatisticsSet.setRatingStatistics(activity, ratingStatisticsData);
            randomSessionStatButton.setText(activity.getString(string.to_session_stat));
            randomSessionStatButton.setActivated(false);
        }, 125);

    }

    /**
     * @param activity required to get resources
     * @param files {@link ArrayList<String> list} of suitable files
     * @param selectedNumber selectedNumber of selected session (usually 0; but you can explicitly select any session)
     * @return {@link ArrayList<AppCompatTextView> list} of {@link AppCompatTextView AppCompatTextViews} to be created
     */
    @NonNull
    @SuppressWarnings("SuspiciousNameCombination")
    @SuppressLint("RestrictedApi")
    public static ArrayList<AppCompatTextView> createSelectList(Activity activity, List<String> files, int selectedNumber) {

        ArrayList<AppCompatTextView> dateViews = new ArrayList<>(0);

        if (files.size() < 2) {
            hideSelect(activity);
        } else {
            showSelect(activity);

            files.forEach(path -> {
                LinearLayout randomSessionsListLayoutView = activity.findViewById(id.random_sessions_list_layout);
                
                int layoutWidth = Utils.getX() * 893 / 1000;
                int layoutHeight = Utils.getX() * 13395 / 100000;
                int padding = Utils.getX() * 13395 / 1000000;

                //
                ////
                //

                LinearLayout layout = new LinearLayout(activity);
                LinearLayout.LayoutParams layoutLayoutParams = new LinearLayout.LayoutParams(layoutWidth, layoutHeight);
                layoutLayoutParams.setMargins(0, padding, 0, 0);
                layout.setLayoutParams(layoutLayoutParams);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setPadding(padding, padding, padding, padding);
                if (files.indexOf(path) == selectedNumber) {
                    layout.setBackground(AppCompatResources.getDrawable(activity, drawable.background_layout_nested_selected ));
                } else {
                    layout.setBackground(AppCompatResources.getDrawable(activity, drawable.background_layout_nested));
                }

                //

                AppCompatTextView date = new AppCompatTextView(activity);
                date.setLayoutParams(new LinearLayout.LayoutParams(layoutWidth - layoutHeight, layoutHeight));
                date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                date.setTextColor(ContextCompat.getColor(activity, color.white));
                date.setTypeface(ResourcesCompat.getFont(activity, font.inter), BOLD);
                date.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_UNIFORM);
                date.setPadding(padding * 2, padding * 2, padding * 2, padding * 2);
                date.setText(Utils.parseTime(path.substring(path.indexOf("-") + 1, path.lastIndexOf("."))));

                //
                ////
                //

                AppCompatImageButton delete = new AppCompatImageButton(activity);
                delete.setLayoutParams(new LinearLayout.LayoutParams(layoutHeight, layoutHeight));
                delete.setBackground(AppCompatResources.getDrawable(activity, drawable.background_delete));
                delete.setImageDrawable(AppCompatResources.getDrawable(activity, drawable.ic_outline_delete_forever_36));
                delete.setPadding(padding, padding, padding, padding);
                delete.setOnClickListener(v -> {

                    if (files.indexOf(path) == selectedNumber) {
                        Toast.makeText(activity, activity.getString(string.delete_select), Toast.LENGTH_SHORT).show();
                    } else {
                        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(activity);
                        alertDialog.setTitle(activity.getString(string.delete));
                        alertDialog.setMessage(activity.getString(string.delete_alert));
                        alertDialog.setPositiveButton(activity.getString(string.delete), (dialog, id) -> {

                            if (new File(path).delete()) {
                                Toast.makeText(activity, activity.getString(string.delete_successfully), Toast.LENGTH_SHORT).show();
                                randomSessionsListLayoutView.removeView(layout);
                            } else {
                                Toast.makeText(activity, activity.getString(string.delete_failed), Toast.LENGTH_SHORT).show();
                            }
                            
                        });
                        alertDialog.setNegativeButton(activity.getString(android.R.string.cancel), (dialog, id) -> { });
                        alertDialog.show();
                    }

                });

                //
                ////
                //

                randomSessionsListLayoutView.addView(layout);
                layout.addView(date);
                layout.addView(delete);

                dateViews.add(date);
            });
        }

        return dateViews;
    }


}