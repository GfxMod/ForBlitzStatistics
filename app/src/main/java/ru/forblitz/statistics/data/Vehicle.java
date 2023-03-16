package ru.forblitz.statistics.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.gson.annotations.SerializedName;

import ru.forblitz.statistics.R;

/**
 * Object containing all the vehicle statistical data
 */
public class Vehicle {

    // TODO: изучите, есть ли сервисы, которые онлайн по вашей json-схеме 
    // (это отдельная штука для описания JSON-данных) генерируют вам класс gson

    // Иначе хотя бы подключите библиотеку lombok (в четверг на консультации расскажу)
    // она позволяет добавлять перед свойством @Getter @Setter и они автоматически будут генерироваться


    @SerializedName("name")
    private String name;
    @SerializedName("nation")
    private String nation;
    @SerializedName("tier")
    private int tier;
    @SerializedName("type")
    private String type;

    @SerializedName("all")
    private StatisticsData data = new StatisticsData();
    @SerializedName("tank_id")
    private String tankId;

    public StatisticsData getData() {
        return data;
    }

    public void setData(StatisticsData data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTankId() {
        return tankId;
    }

    public void setTankId(String tankId) {
        this.tankId = tankId;
    }

    public String getSpotted() {
        return data.getSpotted();
    }

    public String getHits() {
        return data.getHits();
    }

    public String getFrags() {
        return data.getFrags();
    }

    public String getMaxXp() {
        return data.getMaxXp();
    }

    public String getWins() {
        return data.getWins();
    }

    public String getLosses() {
        return data.getLosses();
    }

    public String getCapturePoints() {
        return data.getCapturedPoints();
    }

    public String getBattles() {
        return data.getBattles();
    }

    public String getDamageDealt() {
        return data.getDamageDealt();
    }

    public String getDamageReceived() {
        return data.getDamageReceived();
    }

    public String getShots() {
        return data.getShots();
    }

    public String getFrags8p() {
        return data.getFrags8p();
    }

    public String getXp() {
        return data.getXp();
    }

    public String getWinAndSurvived() {
        return data.getWinAndSurvived();
    }

    public String getSurvivedBattles() {
        return data.getSurvivedBattles();
    }

    public String getMaxFrags() {
        return data.getMaxFrags();
    }

    public String getDroppedCapturePoints() {
        return data.getDroppedCapturePoints();
    }

    public String getNickname() {
        return data.getNickname();
    }

    public String getWinRate() {
        return data.getWinRate();
    }

    public String getAverageDamage() {
        return data.getAverageDamage();
    }

    public String getEfficiency() {
        return data.getEfficiency();
    }

    public String getSurvived() {
        return data.getSurvived();
    }

    public String getHitsFromShots() {
        return data.getHitsFromShots();
    }

    public String getAverageXp() {
        return data.getAverageXp();
    }

    public void setWinRate(String winRate) {
        data.setWinRate(winRate);
    }

    public void setAverageDamage(String averageDamage) {
        data.setAverageDamage(averageDamage);
    }

    public void setEfficiency(String efficiency) {
        data.setEfficiency(efficiency);
    }

    public void setSurvived(String survived) {
        data.setSurvived(survived);
    }

    public void setHitsFromShots(String hitsFromShots) {
        data.setHitsFromShots(hitsFromShots);
    }

    public void setAverageXp(String averageXp) {
        data.setAverageXp(averageXp);
    }

    public int getBattlesColor(Context context) {
        int battlesColor;
        if (Integer.parseInt(data.getBattles()) < 100) {
            battlesColor = ContextCompat.getColor(context, R.color.white);
        } else if (Integer.parseInt(data.getBattles()) < 250) {
            battlesColor = ContextCompat.getColor(context, R.color.green);
        } else if (Integer.parseInt(data.getBattles()) < 500) {
            battlesColor = ContextCompat.getColor(context, R.color.blue);
        } else {
            battlesColor = ContextCompat.getColor(context, R.color.violet);
        }
        return  battlesColor;
    }
    
    public int getAverageDamageColor(Context context) {
        int averageDamageColor;
        if (tier == 10) {
            if (Double.parseDouble(data.getAverageDamage()) < 2000) {
                averageDamageColor = ContextCompat.getColor(context, R.color.white);
            } else if (Double.parseDouble(data.getAverageDamage()) < 2700) {
                averageDamageColor = ContextCompat.getColor(context, R.color.green);
            } else if (Double.parseDouble(data.getAverageDamage()) < 3300) {
                averageDamageColor = ContextCompat.getColor(context, R.color.blue);
            } else {
                averageDamageColor = ContextCompat.getColor(context, R.color.violet);
            }
        } else if (tier == 9) {
            if (Double.parseDouble(data.getAverageDamage()) < 1700) {
                averageDamageColor = ContextCompat.getColor(context, R.color.white);
            } else if (Double.parseDouble(data.getAverageDamage()) < 2200) {
                averageDamageColor = ContextCompat.getColor(context, R.color.green);
            } else if (Double.parseDouble(data.getAverageDamage()) < 2600) {
                averageDamageColor = ContextCompat.getColor(context, R.color.blue);
            } else {
                averageDamageColor = ContextCompat.getColor(context, R.color.violet);
            }
        } else if (tier == 8) {
            if (Double.parseDouble(data.getAverageDamage()) < 1300) {
                averageDamageColor = ContextCompat.getColor(context, R.color.white);
            } else if (Double.parseDouble(data.getAverageDamage()) < 1800) {
                averageDamageColor = ContextCompat.getColor(context, R.color.green);
            } else if (Double.parseDouble(data.getAverageDamage()) < 2200) {
                averageDamageColor = ContextCompat.getColor(context, R.color.blue);
            } else {
                averageDamageColor = ContextCompat.getColor(context, R.color.violet);
            }
        } else {
            averageDamageColor = ContextCompat.getColor(context, R.color.white);
        }
        return averageDamageColor;
    }

    public int getWinRateColor(Context context) {
        int winRateColor;
        if (Double.parseDouble(data.getWinRate()) < 50) {
            winRateColor = ContextCompat.getColor(context, R.color.white);
        } else if (Double.parseDouble(data.getWinRate()) < 60) {
            winRateColor = ContextCompat.getColor(context, R.color.green);
        } else if (Double.parseDouble(data.getWinRate()) < 70) {
            winRateColor = ContextCompat.getColor(context, R.color.blue);
        } else {
            winRateColor = ContextCompat.getColor(context, R.color.violet);
        }
        return winRateColor;
    }

    public int getEfficiencyColor(Context context) {
        int efficiencyColor;
        if (Double.parseDouble(data.getEfficiency()) < 1) {
            efficiencyColor = ContextCompat.getColor(context, R.color.white);
        } else if (Double.parseDouble(data.getEfficiency()) < 1.5) {
            efficiencyColor = ContextCompat.getColor(context, R.color.green);
        } else if (Double.parseDouble(data.getEfficiency()) < 2) {
            efficiencyColor = ContextCompat.getColor(context, R.color.blue);
        } else {
            efficiencyColor = ContextCompat.getColor(context, R.color.violet);
        }
        return efficiencyColor;
    }

    public void clear() {
        this.data.clear();
    }

    public void calculate() {
        data.calculate();
    }

    @NonNull
    @Override
    public String toString() {
        String result = data.toString();

        result += "tankId: " + tankId + "\n";
        result += "name: " + name + "\n";
        result += "nation: " + nation + "\n";
        result += "tier: " + tier + "\n";
        result += "type: " + type + "\n";

        return result;
    }

}