package ru.forblitz.statistics.data;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Locale;

/**
 * Object containing all the main statistical data
 */
public class StatisticsData {

    private String json = "0";

    private String spotted = "0";
    private String hits = "0";
    private String frags = "0";
    private String maxXp = "0";
    private String wins = "0";
    private String losses = "0";
    private String capturePoints = "0";
    private String battles = "0";
    private String damageDealt = "0";
    private String damageReceived = "0";
    private String shots = "0";
    private String frags8p = "0";
    private String xp = "0";
    private String winAndSurvived = "0";
    private String survivedBattles = "0";
    private String maxFrags = "0";
    private String droppedCapturePoints = "0";
    private String nickname = "";

    private String winRate = "0";
    private String averageDamage = "0";
    private String efficiency = "0";
    private String survived = "0";
    private String hitsFromShots = "0";
    private String averageXp = "0";

    public String getJson() {
        return this.json;
    }

    public void setJson(String json) {
        this.json = json;
        this.spotted = parseValueFromJson("spotted");
        this.hits = parseValueFromJson("hits");
        this.frags = parseValueFromJson("frags");
        this.maxXp = parseValueFromJson("max_xp");
        this.wins = parseValueFromJson("wins");
        this.losses = parseValueFromJson("losses");
        this.capturePoints = parseValueFromJson("capture_points");
        this.battles = parseValueFromJson("battles");
        this.damageDealt = parseValueFromJson("damage_dealt");
        this.damageReceived = parseValueFromJson("damage_received");
        this.shots = parseValueFromJson("shots");
        this.frags8p = parseValueFromJson("frags8p");
        this.xp = parseValueFromJson("xp");
        this.winAndSurvived = parseValueFromJson("win_and_survived");
        this.survivedBattles = parseValueFromJson("survived_battles");
        this.maxFrags = parseValueFromJson("max_frags");
        this.droppedCapturePoints = parseValueFromJson("dropped_capture_points");
        this.nickname = parseValueFromJson("nickname");

        this.winRate = String.format(Locale.US, "%.2f", Double.parseDouble(wins) / Double.parseDouble(battles) * 100);
        this.averageDamage = String.format(Locale.US, "%.2f", Double.parseDouble(damageDealt) / Double.parseDouble(battles));
        this.efficiency = String.format(Locale.US, "%.2f", Double.parseDouble(damageDealt) / Double.parseDouble(damageReceived));
        this.survived = String.format(Locale.US, "%.2f", Double.parseDouble(survivedBattles) / Double.parseDouble(battles) * 100);
        this.hitsFromShots = String.format(Locale.US, "%.2f", Double.parseDouble(hits) / Double.parseDouble(shots) * 100);
        this.averageXp = String.format(Locale.US, "%.2f", Double.parseDouble(xp) / Double.parseDouble(battles) * 100);

    }

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

    public String getCapturePoints() {
        return this.capturePoints;
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

    public void setCapturePoints(String capturePoints) {
        this.capturePoints = capturePoints;
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
     * Gets value of the parameter from json
     * @param param the name of the parameter whose value should be found
     * @return the value of the parameter
     */
    @NonNull
    private String parseValueFromJson(String param) {

        String currentJson = json.substring(json.indexOf(param + "\": ") + param.length() + 3);

        try {

            if (currentJson.indexOf(",") < currentJson.indexOf("\n") && currentJson.contains(",")) {
                String t = currentJson.substring(0, currentJson.indexOf(","));
                if (t.contains("\"")) { t = t.substring(1, t.length() - 1); }

                return t;
            } else {
                String t = currentJson.substring(0, currentJson.indexOf("\n"));
                if (t.contains("\"")) { t = t.substring(1, t.length() - 1); }

                return t;
            }

        } catch (Exception e) {
            Log.e("Parsing error", e.getMessage());
            Log.d("param", param);
            Log.d("json", json);
            Log.d("currentJson", currentJson);
            return "0";
        }

    }

    /**
     * Clear all values
     */
    public void clear() {
        this.json = "0";
        this.spotted = "0";
        this.hits = "0";
        this.frags = "0";
        this.maxXp = "0";
        this.wins = "0";
        this.losses = "0";
        this.capturePoints = "0";
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
        result += "capturePoints: " + capturePoints + "\n";
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