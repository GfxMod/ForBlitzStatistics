package ru.forblitz.statistics.widget.data;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import ru.forblitz.statistics.R;
import ru.forblitz.statistics.data.Constants;
import ru.forblitz.statistics.data.StatisticsData;
import ru.forblitz.statistics.utils.InterfaceUtils;

public class PlayerFastStat extends LinearLayout {

    public PlayerFastStat(Context context) {
        super(context);
    }

    public PlayerFastStat(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerFastStat(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(StatisticsData statisticsData) {

        TextView battles = this.findViewWithTag("battles");
        TextView winRate = this.findViewWithTag("winRate");
        TextView averageDamage = this.findViewWithTag("averageDamage");
        TextView efficiency = this.findViewWithTag("efficiency");
        TextView survived = this.findViewWithTag("survived");
        TextView hitsFromShots = this.findViewWithTag("hitsFromShots");

        String battlesValue = statisticsData.getBattles();
        String winRateValue = statisticsData.getWinRate();
        String averageDamageValue = statisticsData.getAverageDamage();
        String efficiencyValue = statisticsData.getEfficiency();
        String survivedValue = statisticsData.getSurvived();
        String hitsFromShotsValue = statisticsData.getHitsFromShots();

        int winRateColor = InterfaceUtils.getValueColor(this.getContext(), statisticsData.getWinRate(), Constants.Steps.winRate);
        int averageDamageColor = InterfaceUtils.getValueColor(this.getContext(), statisticsData.getAverageDamage(), Constants.Steps.averageDamage);
        int efficiencyColor = InterfaceUtils.getValueColor(this.getContext(), statisticsData.getEfficiency(), Constants.Steps.efficiency);

        if (winRateValue.equals("NaN")) { winRateValue = getContext().getString(R.string.empty); winRateColor = this.getContext().getColor(R.color.white); }
        if (averageDamageValue.equals("NaN")) { averageDamageValue = getContext().getString(R.string.empty); averageDamageColor = this.getContext().getColor(R.color.white); }
        if (efficiencyValue.equals("NaN")) { efficiencyValue = getContext().getString(R.string.empty); efficiencyColor = this.getContext().getColor(R.color.white); }
        if (survivedValue.equals("NaN")) { survivedValue = getContext().getString(R.string.empty); }
        if (hitsFromShotsValue.equals("NaN")) { hitsFromShotsValue = getContext().getString(R.string.empty); }

        battles.setText(battlesValue);
        winRate.setText(winRateValue);
        averageDamage.setText(averageDamageValue);
        efficiency.setText(efficiencyValue);
        survived.setText(survivedValue);
        hitsFromShots.setText(hitsFromShotsValue);

        battles.setTextColor(InterfaceUtils.getValueColor(this.getContext(), statisticsData.getBattles(), Constants.Steps.battles));
        winRate.setTextColor(winRateColor);
        averageDamage.setTextColor(averageDamageColor);
        efficiency.setTextColor(efficiencyColor);

    }

    public void setSessionData(StatisticsData statisticsData) {

        String battlesDiff = statisticsData.getBattles();
        String winRateDiff = statisticsData.getWinRate();
        String averageDamageDiff = statisticsData.getAverageDamage();
        String efficiencyDiff = statisticsData.getEfficiency();
        String survivedDiff = statisticsData.getSurvived();
        String hitsFromShotsDiff = statisticsData.getHitsFromShots();

        battlesDiff = "+" + battlesDiff;
        if (Double.parseDouble(winRateDiff) > 0) { winRateDiff = "+" + winRateDiff; }
        winRateDiff = winRateDiff + "%";
        if (Double.parseDouble(averageDamageDiff) > 0) { averageDamageDiff = "+" + averageDamageDiff; }
        if (Double.parseDouble(efficiencyDiff) > 0) { efficiencyDiff = "+" + efficiencyDiff; }
        if (Double.parseDouble(survivedDiff) > 0) { survivedDiff = "+" + survivedDiff; }
        survivedDiff = survivedDiff + "%";
        if (Double.parseDouble(hitsFromShotsDiff) > 0) { hitsFromShotsDiff = "+" + hitsFromShotsDiff; }
        hitsFromShotsDiff = hitsFromShotsDiff + "%";

        ((DifferenceIndicatorView) this.findViewWithTag("battlesDiff")).setValue(battlesDiff, false);
        ((DifferenceIndicatorView) this.findViewWithTag("winRateDiff")).setValue(winRateDiff, true);
        ((DifferenceIndicatorView) this.findViewWithTag("averageDamageDiff")).setValue(averageDamageDiff, false);
        ((DifferenceIndicatorView) this.findViewWithTag("efficiencyDiff")).setValue(efficiencyDiff, false);
        ((DifferenceIndicatorView) this.findViewWithTag("survivedDiff")).setValue(survivedDiff, true);
        ((DifferenceIndicatorView) this.findViewWithTag("hitsFromShotsDiff")).setValue(hitsFromShotsDiff, true);

    }

}
