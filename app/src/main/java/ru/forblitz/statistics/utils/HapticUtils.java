package ru.forblitz.statistics.utils;

import static ru.forblitz.statistics.data.Constants.hapticFeedbackType;

import android.view.View;

import ru.forblitz.statistics.ForBlitzStatisticsApplication;

public class HapticUtils {

    public static void performHapticFeedback(View view) {
        if (((ForBlitzStatisticsApplication) view.getContext().getApplicationContext()).isHapticsEnabled()) {
            view.performHapticFeedback(hapticFeedbackType);
        }
    }

}
