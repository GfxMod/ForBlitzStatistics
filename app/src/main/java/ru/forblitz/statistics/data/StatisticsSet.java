package ru.forblitz.statistics.data;

import android.app.Activity;
import ru.forblitz.statistics.widget.data.PlayerFastStat;
import ru.forblitz.statistics.widget.data.DetailsLayout;

import ru.forblitz.statistics.R;


public class StatisticsSet {

    /**
     * Sets {@link ru.forblitz.statistics.R.layout#fragment_random base statistics} values
     * @param activity required to get resources
     * @param statistics statistics to be set
     */
    public static void setBaseStatistics(Activity activity, StatisticsData statistics) {
        ((PlayerFastStat) activity.findViewById(R.id.random_fast_stat)).setData(statistics);
        ((DetailsLayout) activity.findViewById(R.id.random_details_layout)).setData(statistics);
    }

    /**
     * Sets {@link ru.forblitz.statistics.R.layout#fragment_rating rating statistics} values
     * @param activity required to get resources
     * @param statisticsData statistics to be set
     */
    public static void setRatingStatistics(Activity activity, StatisticsData statisticsData) {
        ((PlayerFastStat) activity.findViewById(R.id.rating_fast_stat)).setData(statisticsData);
        ((DetailsLayout) activity.findViewById(R.id.rating_details_layout)).setData(statisticsData);
    }

}
