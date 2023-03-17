package ru.forblitz.statistics.data;

import androidx.annotation.NonNull;

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

    public void calculate() {
        this.winRate = String.format(Locale.US, "%.2f", Double.parseDouble(wins) / Double.parseDouble(battles) * 100);
        this.averageDamage = String.format(Locale.US, "%.2f", Double.parseDouble(damageDealt) / Double.parseDouble(battles));
        this.efficiency = String.format(Locale.US, "%.2f", Double.parseDouble(damageDealt) / Double.parseDouble(damageReceived));
        this.survived = String.format(Locale.US, "%.2f", Double.parseDouble(survivedBattles) / Double.parseDouble(battles) * 100);
        this.hitsFromShots = String.format(Locale.US, "%.2f", Double.parseDouble(hits) / Double.parseDouble(shots) * 100);
        this.averageXp = String.format(Locale.US, "%.2f", Double.parseDouble(xp) / Double.parseDouble(battles) * 100);
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @NonNull
    @Override
    public String toString() {
        String result = "-\n--STATISTICS DATA--\n";

        result += "spotted: " + spotted + "\n";
        result += "hits: " + hits + "\n";
        result += "frags: " + frags + "\n";
        result += "maxXp: " + maxXp + "\n";
        result += "wins: " + wins + "\n";
        result += "losses: " + losses + "\n";
        result += "capturePoints: " + capturedPoints + "\n";
        result += "battles: " + battles + "\n";
        result += "damageDealt: " + damageDealt + "\n";
        result += "damageReceived: " + damageReceived + "\n";
        result += "shots: " + shots + "\n";
        result += "frags8p: " + frags8p + "\n";
        result += "xp: " + xp + "\n";
        result += "winAndSurvived: " + winAndSurvived + "\n";
        result += "survivedBattles: " + survivedBattles + "\n";
        result += "maxFrags: " + maxFrags + "\n";
        result += "droppedCapturePoints: " + droppedCapturePoints + "\n";
        result += "nickname: " + nickname + "\n";

        result += "winRate: " + winRate + "\n";
        result += "averageDamage: " + averageDamage + "\n";
        result += "efficiency: " + efficiency + "\n";
        result += "survived: " + survived + "\n";
        result += "hitsFromShots: : " + hitsFromShots + "\n";

        return result;
    }

}