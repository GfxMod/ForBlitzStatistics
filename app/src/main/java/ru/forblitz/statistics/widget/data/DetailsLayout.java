package ru.forblitz.statistics.widget.data;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;

import ru.forblitz.statistics.dto.StatisticsData;

public class DetailsLayout extends LinearLayout {

    public DetailsLayout(Context context) {
        super(context);
    }

    public DetailsLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DetailsLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(StatisticsData statisticsData) {
        for (View view : getAllChildren(this)) {
            if (view instanceof TextView && view.getTag() != null) {
                try {
                    Field field = StatisticsData.class.getDeclaredField(String.valueOf(view.getTag()));
                    field.setAccessible(true);
                    ((TextView) view).setText((String) field.get(statisticsData));
                } catch (NoSuchFieldException|IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // TODO: add private
    ArrayList<View> getAllChildren(LinearLayout linearLayout) {
        ArrayList<View> children = new ArrayList<>();

        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);
            children.add(child);
            if (child instanceof LinearLayout) {
                children.addAll(getAllChildren((LinearLayout) child));
            }
        }

        return children;
    }

}
