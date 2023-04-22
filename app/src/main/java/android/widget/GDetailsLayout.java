package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;

import ru.forblitz.statistics.data.StatisticsData;

public class GDetailsLayout extends LinearLayout {

    public GDetailsLayout(Context context) {
        super(context);
    }

    public GDetailsLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GDetailsLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
