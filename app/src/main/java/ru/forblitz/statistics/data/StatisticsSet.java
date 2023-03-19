package ru.forblitz.statistics.data;

import android.app.Activity;
import android.graphics.Color;
import android.widget.TextView;

import ru.forblitz.statistics.R.id;
import ru.forblitz.statistics.utils.Utils;


public class StatisticsSet {

    /**
     * Sets {@link ru.forblitz.statistics.R.layout#fragment_random base statistics} values
     * @param activity required to get resources
     * @param statistics statistics to be set
     */
    public static void setBaseStatistics(Activity activity, StatisticsData statistics) {

        TextView battlesView = activity.findViewById(id.text_battles_info);
        TextView winRateView = activity.findViewById(id.text_wins_info);
        TextView averageDamageView = activity.findViewById(id.text_damage_info);
        TextView efficiencyView = activity.findViewById(id.text_efficiency_info);
        TextView survivedView = activity.findViewById(id.text_survived_info);
        TextView hitsFromShotsView = activity.findViewById(id.text_hits_from_shots_info);

        TextView randomWinsView = activity.findViewById(id.random_wins);
        TextView randomLossesView = activity.findViewById(id.random_losses);
        TextView randomWinAndSurviveView = activity.findViewById(id.random_win_and_survive);
        TextView randomSurviveView = activity.findViewById(id.random_survive);
        TextView randomXpView = activity.findViewById(id.random_xp);
        TextView randomFragsView = activity.findViewById(id.random_frags);
        TextView randomShotsView = activity.findViewById(id.random_shots);
        TextView randomHitsView = activity.findViewById(id.random_hits);
        TextView randomSpottedView = activity.findViewById(id.random_spotted);
        TextView randomFrags8pView = activity.findViewById(id.random_frags8p);
        TextView randomMaxXpView = activity.findViewById(id.random_max_xp);
        TextView randomMaxFragsView = activity.findViewById(id.random_max_frags);
        TextView randomCapturedPointsView = activity.findViewById(id.random_captured_points);
        TextView randomDroppedView = activity.findViewById(id.random_dropped);
        TextView randomDamageDealtView = activity.findViewById(id.random_damage_dealt);
        TextView randomDamageReceivedView = activity.findViewById(id.random_damage_received);
        
        String spotted = statistics.getSpotted();
        String hits = statistics.getHits();
        String frags = statistics.getFrags();
        String maxXp = statistics.getMaxXp();
        String wins = statistics.getWins();
        String losses = statistics.getLosses();
        String capturePoints = statistics.getCapturedPoints();
        String battles = statistics.getBattles();
        String damageDealt = statistics.getDamageDealt();
        String damageReceived = statistics.getDamageReceived();
        String shots = statistics.getShots();
        String frags8p = statistics.getFrags8p();
        String xp = statistics.getXp();
        String winAndSurvived = statistics.getWinAndSurvived();
        String survivedBattles = statistics.getSurvivedBattles();
        String maxFrags = statistics.getMaxFrags();
        String droppedCapturePoints = statistics.getDroppedCapturePoints();
        String winRate = statistics.getWinRate();
        String averageDamage = statistics.getAverageDamage();
        String efficiency = statistics.getEfficiency();
        String survived = statistics.getSurvived();
        String hitsFromShots = statistics.getHitsFromShots();

        //

        if (Integer.parseInt(battles) == 0) {

            Utils.setBaseStatisticsVisibility(activity, false);

        } else {

            Utils.setBaseStatisticsVisibility(activity, true);

            //
            /////
            //

            battlesView.setText(battles);
            winRateView.setText(winRate);
            averageDamageView.setText(averageDamage);
            efficiencyView.setText(efficiency);
            survivedView.setText(survived);
            hitsFromShotsView.setText(hitsFromShots);

            if (Double.parseDouble(battles) < 2000) {
                battlesView.setTextColor(Color.parseColor("#FFFFFF"));
            }
            if (2000 <= Double.parseDouble(battles)) {
                if (Double.parseDouble(battles) < 10000) {
                    battlesView.setTextColor(Color.parseColor("#00B540"));
                }
            }
            if (10000 <= Double.parseDouble(battles)) {
                if (Double.parseDouble(battles) < 25000) {
                    battlesView.setTextColor(Color.parseColor("#7285FA"));
                }
            }
            if (25000 <= Double.parseDouble(battles)) {
                battlesView.setTextColor(Color.parseColor("#CC5FFF"));
            }
            
            if (Double.parseDouble(winRate) < 50) {
                winRateView.setTextColor(Color.parseColor("#FFFFFF"));
            }
            if (50 <= Double.parseDouble(winRate)) {
                if (Double.parseDouble(winRate) < 60) {
                    winRateView.setTextColor(Color.parseColor("#00B540"));
                }
            }
            if (60 <= Double.parseDouble(winRate)) {
                if (Double.parseDouble(winRate) < 70) {
                    winRateView.setTextColor(Color.parseColor("#7285FA"));
                }
            }
            if (70 <= Double.parseDouble(winRate)) {
                winRateView.setTextColor(Color.parseColor("#CC5FFF"));
            }

            if (Double.parseDouble(averageDamage) < 1000) {
                averageDamageView.setTextColor(Color.parseColor("#FFFFFF"));
            }
            if (1000 <= Double.parseDouble(averageDamage)) {
                if (Double.parseDouble(averageDamage) < 1500) {
                    averageDamageView.setTextColor(Color.parseColor("#00B540"));
                }
            }
            if (1500 <= Double.parseDouble(averageDamage)) {
                if (Double.parseDouble(averageDamage) < 2500) {
                    averageDamageView.setTextColor(Color.parseColor("#7285FA"));
                }
            }
            if (2500 <= Double.parseDouble(averageDamage)) {
                averageDamageView.setTextColor(Color.parseColor("#CC5FFF"));
            }

            if (1 <= Double.parseDouble(efficiency)) {
                efficiencyView.setTextColor(Color.parseColor("#CC5FFF"));
            } else {
                efficiencyView.setTextColor(Color.parseColor("#FFFFFF"));
            }
            
            // details

            randomWinsView.setText(wins);
            randomLossesView.setText(losses);
            randomWinAndSurviveView.setText(winAndSurvived);
            randomSurviveView.setText(survivedBattles);
            randomXpView.setText(xp);
            randomFragsView.setText(frags);
            randomShotsView.setText(shots);
            randomHitsView.setText(hits);
            randomSpottedView.setText(spotted);
            randomFrags8pView.setText(frags8p);
            randomMaxXpView.setText(maxXp);
            randomMaxFragsView.setText(maxFrags);
            randomCapturedPointsView.setText(capturePoints);
            randomDroppedView.setText(droppedCapturePoints);
            randomDamageDealtView.setText(damageDealt);
            randomDamageReceivedView.setText(damageReceived);

        }

    }

    /**
     * Sets {@link ru.forblitz.statistics.R.layout#fragment_rating rating statistics} values
     * @param activity required to get resources
     * @param statistics statistics to be set
     */
    public static void setRatingStatistics(Activity activity, StatisticsData statistics) {
        TextView battlesView = activity.findViewById(id.rating_text_battles_info);
        TextView winRateView = activity.findViewById(id.rating_text_wins_info);
        TextView averageDamageView = activity.findViewById(id.rating_text_damage_info);
        TextView efficiencyView = activity.findViewById(id.rating_text_efficiency_info);
        TextView survivedView = activity.findViewById(id.rating_text_survived_info);
        TextView hitsFromShotsView = activity.findViewById(id.rating_text_hits_from_shots_info);

        TextView ratingWinsView = activity.findViewById(id.rating_wins);
        TextView ratingLossesView = activity.findViewById(id.rating_losses);
        TextView ratingWinAndSurviveView = activity.findViewById(id.rating_win_and_survive);
        TextView ratingSurviveView = activity.findViewById(id.rating_survive);
        TextView ratingXpView = activity.findViewById(id.rating_xp);
        TextView ratingFragsView = activity.findViewById(id.rating_frags);
        TextView ratingShotsView = activity.findViewById(id.rating_shots);
        TextView ratingHitsView = activity.findViewById(id.rating_hits);
        TextView ratingSpottedView = activity.findViewById(id.rating_spotted);
        TextView ratingFrags8pView = activity.findViewById(id.rating_frags8p);
        TextView ratingCapturedPointsView = activity.findViewById(id.rating_captured_points);
        TextView ratingDefendedView = activity.findViewById(id.rating_dropped);
        TextView ratingDamageDealtView = activity.findViewById(id.rating_damage_dealt);
        TextView ratingDamageReceivedView = activity.findViewById(id.rating_damage_received);

        String spotted = statistics.getSpotted();
        String hits = statistics.getHits();
        String frags = statistics.getFrags();
        String wins = statistics.getWins();
        String losses = statistics.getLosses();
        String capturePoints = statistics.getCapturedPoints();
        String battles = statistics.getBattles();
        String damageDealt = statistics.getDamageDealt();
        String damageReceived = statistics.getDamageReceived();
        String shots = statistics.getShots();
        String frags8p = statistics.getFrags8p();
        String xp = statistics.getXp();
        String winAndSurvived = statistics.getWinAndSurvived();
        String survivedBattles = statistics.getSurvivedBattles();
        String droppedCapturePoints = statistics.getDroppedCapturePoints();
        String winRate = statistics.getWinRate();
        String averageDamage = statistics.getAverageDamage();
        String efficiency = statistics.getEfficiency();
        String survived = statistics.getSurvived();
        String hitsFromShots = statistics.getHitsFromShots();

        //

        if (Integer.parseInt(battles) == 0) {

            Utils.setRatingStatisticsVisibility(activity, false);

        } else {

            Utils.setRatingStatisticsVisibility(activity, true);

            //
            /////
            //

            battlesView.setText(battles);
            winRateView.setText(winRate);
            averageDamageView.setText(averageDamage);
            efficiencyView.setText(efficiency);
            survivedView.setText(survived);
            hitsFromShotsView.setText(hitsFromShots);

            if (Double.parseDouble(battles) < 2000) {
                battlesView.setTextColor(Color.parseColor("#FFFFFF"));
            }
            if (2000 <= Double.parseDouble(battles)) {
                if (Double.parseDouble(battles) < 10000) {
                    battlesView.setTextColor(Color.parseColor("#00B540"));
                }
            }
            if (10000 <= Double.parseDouble(battles)) {
                if (Double.parseDouble(battles) < 25000) {
                    battlesView.setTextColor(Color.parseColor("#7285FA"));
                }
            }
            if (25000 <= Double.parseDouble(battles)) {
                battlesView.setTextColor(Color.parseColor("#CC5FFF"));
            }

            if (Double.parseDouble(winRate) < 50) {
                winRateView.setTextColor(Color.parseColor("#FFFFFF"));
            }
            if (50 <= Double.parseDouble(winRate)) {
                if (Double.parseDouble(winRate) < 60) {
                    winRateView.setTextColor(Color.parseColor("#00B540"));
                }
            }
            if (60 <= Double.parseDouble(winRate)) {
                if (Double.parseDouble(winRate) < 70) {
                    winRateView.setTextColor(Color.parseColor("#7285FA"));
                }
            }
            if (70 <= Double.parseDouble(winRate)) {
                winRateView.setTextColor(Color.parseColor("#CC5FFF"));
            }

            if (Double.parseDouble(averageDamage) < 1000) {
                averageDamageView.setTextColor(Color.parseColor("#FFFFFF"));
            }
            if (1000 <= Double.parseDouble(averageDamage)) {
                if (Double.parseDouble(averageDamage) < 1500) {
                    averageDamageView.setTextColor(Color.parseColor("#00B540"));
                }
            }
            if (1500 <= Double.parseDouble(averageDamage)) {
                if (Double.parseDouble(averageDamage) < 2500) {
                    averageDamageView.setTextColor(Color.parseColor("#7285FA"));
                }
            }
            if (2500 <= Double.parseDouble(averageDamage)) {
                averageDamageView.setTextColor(Color.parseColor("#CC5FFF"));
            }

            if (1 <= Double.parseDouble(efficiency)) {
                efficiencyView.setTextColor(Color.parseColor("#CC5FFF"));
            } else {
                efficiencyView.setTextColor(Color.parseColor("#FFFFFF"));
            }

            // details

            ratingWinsView.setText(wins);
            ratingLossesView.setText(losses);
            ratingWinAndSurviveView.setText(winAndSurvived);
            ratingSurviveView.setText(survivedBattles);
            ratingXpView.setText(xp);
            ratingFragsView.setText(frags);
            ratingShotsView.setText(shots);
            ratingHitsView.setText(hits);
            ratingSpottedView.setText(spotted);
            ratingFrags8pView.setText(frags8p);
            ratingCapturedPointsView.setText(capturePoints);
            ratingDefendedView.setText(droppedCapturePoints);
            ratingDamageDealtView.setText(damageDealt);
            ratingDamageReceivedView.setText(damageReceived);

        }

    }

}
