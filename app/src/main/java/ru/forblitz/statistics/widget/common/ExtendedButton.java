package ru.forblitz.statistics.widget.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import ru.forblitz.statistics.ForBlitzStatisticsApplication;

public class ExtendedButton extends AppCompatButton {

    public ExtendedButton(@NonNull Context context) {
        super(context);
    }

    public ExtendedButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtendedButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(v -> {
            if (((ForBlitzStatisticsApplication) getContext().getApplicationContext()).isHapticsEnabled()) {
                performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK);
            }
            if (l != null) {
                l.onClick(v);
            }
        });
    }

}
