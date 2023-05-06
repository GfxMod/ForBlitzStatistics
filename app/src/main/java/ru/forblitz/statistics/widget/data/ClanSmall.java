package ru.forblitz.statistics.widget.data;

import static ru.forblitz.statistics.data.Constants.ClanViewFlipperItems.IS_A_MEMBER;
import static ru.forblitz.statistics.data.Constants.ClanViewFlipperItems.NOT_IS_A_MEMBER;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import ru.forblitz.statistics.dto.SmallClanData;
import ru.forblitz.statistics.utils.ParseUtils;
import ru.forblitz.statistics.widget.common.DifferenceViewFlipper;

public class ClanSmall extends DifferenceViewFlipper {

    public ClanSmall(Context context) {
        super(context);
    }

    public ClanSmall(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(SmallClanData smallClanData) {

        if (smallClanData.clanData != null) {

            this.setDisplayedChild(IS_A_MEMBER);

            String name = "[" + smallClanData.clanData.tag + "] " + smallClanData.clanData.name;
            String role = ParseUtils.role(this.getContext(), smallClanData.role);

            ((TextView) this.findViewWithTag("clan_name")).setText(name);
            ((TextView) this.findViewWithTag("clan_role")).setText(role);

        } else {

            this.setDisplayedChild(NOT_IS_A_MEMBER);

        }

    }

}
