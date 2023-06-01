package ru.forblitz.statistics.utils;

import androidx.annotation.NonNull;

import java.util.Locale;

import ru.forblitz.statistics.dto.StatisticsData;

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

}