package ru.forblitz.statistics.widget.data;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import ru.forblitz.statistics.data.BigClanData;
import ru.forblitz.statistics.data.SmallClanData;
import ru.forblitz.statistics.utils.ParseUtils;

public class ClanSmall extends LinearLayout {

    public ClanSmall(Context context) {
        super(context);
    }

    public ClanSmall(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ClanSmall(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(SmallClanData smallClanData, BigClanData bigClanData) {

        String name = "[" + bigClanData.getTag() + "] " + bigClanData.getName();
        String role = ParseUtils.parseRole(this.getContext(), smallClanData.getRole());

        ((TextView) this.findViewWithTag("clan_name")).setText(name);
        ((TextView) this.findViewWithTag("clan_role")).setText(role);

    }

}
