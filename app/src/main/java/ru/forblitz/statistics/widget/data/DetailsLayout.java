package ru.forblitz.statistics.widget.data;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;

import ru.forblitz.statistics.dto.StatisticsDataModern;
import ru.forblitz.statistics.utils.ParseUtils;

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

    public void setData(StatisticsDataModern statisticsDataModern) {
        for (View view : getAllChildren(this)) {
            if (view instanceof TextView && view.getTag() != null) {
                try {
                    Field field = StatisticsDataModern.class.getDeclaredField(String.valueOf(view.getTag()));
                    field.setAccessible(true);
                    ((TextView) view).setText(ParseUtils.splitByThousands(Integer.toString((Integer) field.get(statisticsDataModern))));
                } catch (NoSuchFieldException|IllegalAccessException e) {
                    Log.e("DetailsLayout", e.toString());
                }
            }
        }
    }

    private ArrayList<View> getAllChildren(LinearLayout linearLayout) {
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
