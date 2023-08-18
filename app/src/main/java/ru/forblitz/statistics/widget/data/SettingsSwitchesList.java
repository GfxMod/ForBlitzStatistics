package ru.forblitz.statistics.widget.data;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.materialswitch.MaterialSwitch;

import ru.forblitz.statistics.R;

public class SettingsSwitchesList extends LinearLayout {

    private int textSize;

    public SettingsSwitchesList(Context context) {
        super(context);
    }

    public SettingsSwitchesList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SettingsSwitchesList(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public View addItem(
            String tag,
            String title,
            String description,
            boolean isChecked
    ) {
        ConstraintLayout item = (ConstraintLayout)
                LayoutInflater
                        .from(getContext())
                        .inflate(R.layout.item_settings_switch, null);

        item.setTag(tag);

        TextView titleView = item.findViewWithTag("title");
        TextView descriptionView = item.findViewWithTag("description");
        MaterialSwitch switchView = item.findViewWithTag("switch");

        titleView.setText(title);
        descriptionView.setText(description);

        titleView.setTextSize(textSize);
        descriptionView.setTextSize(textSize);

        switchView.setChecked(isChecked);

        this.addView(item);

        return item;
    }

}
