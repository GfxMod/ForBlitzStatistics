package ru.forblitz.statistics.data;

import android.app.Activity;
import android.widget.GPlayerFastStat;
import android.widget.GDetailsLayout;

import ru.forblitz.statistics.R;


public class StatisticsSet {

    /**
     * Sets {@link ru.forblitz.statistics.R.layout#fragment_random base statistics} values
     * @param activity required to get resources
     * @param statistics statistics to be set
     */
    public static void setBaseStatistics(Activity activity, StatisticsData statistics) {
        ((GPlayerFastStat) activity.findViewById(R.id.random_fast_stat)).setData(statistics);
        ((GDetailsLayout) activity.findViewById(R.id.random_details_layout)).setData(statistics);
    }

    /**
     * Sets {@link ru.forblitz.statistics.R.layout#fragment_rating rating statistics} values
     * @param activity required to get resources
     * @param statisticsData statistics to be set
     */
    public static void setRatingStatistics(Activity activity, StatisticsData statisticsData) {
        ((GPlayerFastStat) activity.findViewById(R.id.rating_fast_stat)).setData(statisticsData);
        ((GDetailsLayout) activity.findViewById(R.id.rating_details_layout)).setData(statisticsData);
    }

}
