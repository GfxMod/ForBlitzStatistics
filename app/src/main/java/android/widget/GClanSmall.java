package android.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import ru.forblitz.statistics.data.BigClanData;
import ru.forblitz.statistics.data.SmallClanData;
import ru.forblitz.statistics.utils.ParseUtils;

public class GClanSmall extends LinearLayout {

    public GClanSmall(Context context) {
        super(context);
    }

    public GClanSmall(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GClanSmall(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(SmallClanData smallClanData, BigClanData bigClanData) {

        String name = "[" + bigClanData.getTag() + "] " + bigClanData.getName();
        String role = ParseUtils.parseRole(this.getContext(), smallClanData.getRole());

        ((TextView) this.findViewWithTag("clan_name")).setText(name);
        ((TextView) this.findViewWithTag("clan_role")).setText(role);

    }

}
