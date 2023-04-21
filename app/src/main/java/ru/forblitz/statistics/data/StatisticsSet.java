package ru.forblitz.statistics.data;

import android.app.Activity;
import android.graphics.Color;
import android.widget.GPlayerFastStat;
import android.widget.TextView;

import ru.forblitz.statistics.R;
import ru.forblitz.statistics.utils.InterfaceUtils;


public class StatisticsSet {

    /**
     * Sets {@link ru.forblitz.statistics.R.layout#fragment_random base statistics} values
     * @param activity required to get resources
     * @param statistics statistics to be set
     */
    public static void setBaseStatistics(Activity activity, StatisticsData statistics) {
        ((GPlayerFastStat) activity.findViewById(R.id.random_fast_stat)).setData(statistics);
    }

    /**
     * Sets {@link ru.forblitz.statistics.R.layout#fragment_rating rating statistics} values
     * @param activity required to get resources
     * @param statistics statistics to be set
     */
    public static void setRatingStatistics(Activity activity, StatisticsData statistics) {
        ((GPlayerFastStat) activity.findViewById(R.id.rating_fast_stat)).setData(statistics);
    }

}
