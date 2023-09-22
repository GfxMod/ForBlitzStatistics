package ru.forblitz.statistics.widget.common;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import ru.forblitz.statistics.utils.HapticUtils;

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
            HapticUtils.performHapticFeedback(this);
            if (l != null) {
                l.onClick(v);
            }
        });
    }

}
