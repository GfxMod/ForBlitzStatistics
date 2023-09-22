package ru.forblitz.statistics.widget.data;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import ru.forblitz.statistics.R;

public class SessionButtonsLayout extends LinearLayout {

    public SessionButtonsLayout(Context context) {
        super(context);
    }

    public SessionButtonsLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SessionButtonsLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setButtonsVisibility(ButtonsVisibility variant) {
        View randomSessionsStatButton = this.findViewById(R.id.statistics_session_stat_button);
        View randomSessionsListButton = this.findViewById(R.id.statistics_sessions_list_button);
        if (variant == ButtonsVisibility.NOTHING) {
            randomSessionsStatButton.setVisibility(View.GONE);
            randomSessionsListButton.setVisibility(View.GONE);
        }
        if (variant == ButtonsVisibility.ONLY_FLIP) {
            randomSessionsStatButton.setVisibility(View.VISIBLE);
            randomSessionsListButton.setVisibility(View.GONE);
        }
        if (variant == ButtonsVisibility.ALL) {
            randomSessionsStatButton.setVisibility(View.VISIBLE);
            randomSessionsListButton.setVisibility(View.VISIBLE);
        }
    }

    public enum ButtonsVisibility {
        NOTHING,
        ONLY_FLIP,
        ALL
    }

}
