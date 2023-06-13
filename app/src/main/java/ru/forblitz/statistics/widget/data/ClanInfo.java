package ru.forblitz.statistics.widget.data;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import ru.forblitz.statistics.R;
import ru.forblitz.statistics.dto.FullClanInfo;
import ru.forblitz.statistics.dto.ShortClanInfo;

public class ClanInfo extends LinearLayout {

    public ClanInfo(Context context) {
        super(context);
    }

    public ClanInfo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ClanInfo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(ShortClanInfo shortClanInfo, FullClanInfo fullClanInfo) {

        ((TextView) this.findViewById(R.id.clan_motto)).setText(fullClanInfo.getMotto());
        ((TextView) this.findViewById(R.id.clan_description)).setText(fullClanInfo.getDescription());

        ((ClanBrief) this.findViewById(R.id.clan_clan)).setData(shortClanInfo);

    }

}
