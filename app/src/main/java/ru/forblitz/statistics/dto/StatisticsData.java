package ru.forblitz.statistics.dto;

import androidx.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Object containing all the main statistical data
 */
public class StatisticsData {

    @SerializedName("spotted")
    private String spotted = "0";
    @SerializedName("hits")
    private String hits = "0";
    @SerializedName("frags")
    private String frags = "0";
    @SerializedName("max_xp")
    private String maxXp = "0";
    @SerializedName("wins")
    private String wins = "0";
    @SerializedName("losses")
    private String losses = "0";
    @SerializedName("capture_points")
    private String capturedPoints = "0";
    @SerializedName("battles")
    private String battles = "0";
    @SerializedName("damage_dealt")
    private String damageDealt = "0";
    @SerializedName("damage_received")
    private String damageReceived = "0";
    @SerializedName("shots")
    private String shots = "0";
    @SerializedName("frags8p")
    private String frags8p = "0";
    @SerializedName("xp")
    private String xp = "0";
    @SerializedName("win_and_survived")
    private String winAndSurvived = "0";
    @SerializedName("survived_battles")
    private String survivedBattles = "0";
    @SerializedName("max_frags")
    private String maxFrags = "0";
    @SerializedName("dropped_capture_points")
    private String droppedCapturePoints = "0";
    @SerializedName("nickname")
    private String nickname = "";

    private String winRate = "0";
    private String averageDamage = "0";
    private String efficiency = "0";
    private String survived = "0";
    private String hitsFromShots = "0";
    private String averageXp = "0";
    
    private boolean detailedAverageDamage = false;

    public String getSpotted() {
        return this.spotted;
    }

    public String getHits() {
        return this.hits;
    }

    public String getFrags() {
        return this.frags;
    }

    public String getMaxXp() {
        return this.maxXp;
    }

    public String getWins() {
        return this.wins;
    }

    public String getLosses() {
        return this.losses;
    }

    public String getCapturedPoints() {
        return this.capturedPoints;
    }

    public String getBattles() {
        return this.battles;
    }

    public String getDamageDealt() {
        return this.damageDealt;
    }

    public String getDamageReceived() {
        return this.damageReceived;
    }

    public String getShots() {
        return this.shots;
    }

    public String getFrags8p() {
        return this.frags8p;
    }

    public String getXp() {
        return this.xp;
    }

    public String getWinAndSurvived() {
        return this.winAndSurvived;
    }

    public String getSurvivedBattles() {
        return this.survivedBattles;
    }

    public String getMaxFrags() {
        return this.maxFrags;
    }

    public String getDroppedCapturePoints() {
        return this.droppedCapturePoints;
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getWinRate() {
        return this.winRate;
    }

    public String getAverageDamage() {
        return this.averageDamage;
    }

    public String getEfficiency() {
        return this.efficiency;
    }

    public String getSurvived() {
        return this.survived;
    }

    public String getHitsFromShots() {
        return this.hitsFromShots;
    }

    public String getAverageXp() {
        return this.averageXp;
    }

    public void setSpotted(String spotted) {
        this.spotted = spotted;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public void setFrags(String frags) {
        this.frags = frags;
    }

    public void setMaxXp(String maxXp) {
        this.maxXp = maxXp;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    public void setLosses(String losses) {
        this.losses = losses;
    }

    public void setCapturedPoints(String capturedPoints) {
        this.capturedPoints = capturedPoints;
    }

    public void setBattles(String battles) {
        this.battles = battles;
    }

    public void setDamageDealt(String damageDealt) {
        this.damageDealt = damageDealt;
    }

    public void setDamageReceived(String damageReceived) {
        this.damageReceived = damageReceived;
    }

    public void setShots(String shots) {
        this.shots = shots;
    }

    public void setFrags8p(String frags8p) {
        this.frags8p = frags8p;
    }

    public void setXp(String xp) {
        this.xp = xp;
    }

    public void setWinAndSurvived(String winAndSurvived) {
        this.winAndSurvived = winAndSurvived;
    }

    public void setSurvivedBattles(String survivedBattles) {
        this.survivedBattles = survivedBattles;
    }

    public void setMaxFrags(String maxFrags) {
        this.maxFrags = maxFrags;
    }

    public void setDroppedCapturePoints(String droppedCapturePoints) {
        this.droppedCapturePoints = droppedCapturePoints;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setWinRate(String winRate) {
        this.winRate = winRate;
    }

    public void setAverageDamage(String averageDamage) {
        this.averageDamage = averageDamage;
    }

    public void setEfficiency(String efficiency) {
        this.efficiency = efficiency;
    }

    public void setSurvived(String survived) {
        this.survived = survived;
    }

    public void setHitsFromShots(String hitsFromShots) {
        this.hitsFromShots = hitsFromShots;
    }

    public void setAverageXp(String averageXp) {
        this.averageXp = averageXp;
    }

    public boolean isDetailedAverageDamage() {
        return detailedAverageDamage;
    }

    /**
     * Clear all values
     */
    public void clear() {
        this.spotted = "0";
        this.hits = "0";
        this.frags = "0";
        this.maxXp = "0";
        this.wins = "0";
        this.losses = "0";
        this.capturedPoints = "0";
        this.battles = "0";
        this.damageDealt = "0";
        this.damageReceived = "0";
        this.shots = "0";
        this.frags8p = "0";
        this.xp = "0";
        this.winAndSurvived = "0";
        this.survivedBattles = "0";
        this.maxFrags = "0";
        this.droppedCapturePoints = "0";
        this.nickname = "0";
        
        this.winRate = "0";
        this.averageDamage = "0";
        this.efficiency = "0";
        this.survived = "0";
        this.hitsFromShots = "0";
    }

    public void calculate(boolean detailedAverageDamage) {
        this.detailedAverageDamage = detailedAverageDamage;
        this.winRate = String.format(Locale.US, "%.2f", Double.parseDouble(wins) / Double.parseDouble(battles) * 100);
        if (detailedAverageDamage) {
            this.averageDamage = String.format(Locale.US, "%.2f", Double.parseDouble(damageDealt) / Double.parseDouble(battles));
        } else {
            this.averageDamage = Integer.toString((int) (Double.parseDouble(damageDealt) / Double.parseDouble(battles)));
        }
        this.efficiency = String.format(Locale.US, "%.2f", Double.parseDouble(damageDealt) / Double.parseDouble(damageReceived));
        this.survived = String.format(Locale.US, "%.2f", Double.parseDouble(survivedBattles) / Double.parseDouble(battles) * 100);
        this.hitsFromShots = String.format(Locale.US, "%.2f", Double.parseDouble(hits) / Double.parseDouble(shots) * 100);
        this.averageXp = String.format(Locale.US, "%.2f", Double.parseDouble(xp) / Double.parseDouble(battles) * 100);
    }

    @NonNull
    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

}