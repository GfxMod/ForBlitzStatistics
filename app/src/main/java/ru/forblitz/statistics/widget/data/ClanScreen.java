package ru.forblitz.statistics.widget.data;

import static ru.forblitz.statistics.data.Constants.ClanViewFlipperItems.IS_A_MEMBER;
import static ru.forblitz.statistics.data.Constants.ClanViewFlipperItems.NOT_IS_A_MEMBER;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.forblitz.statistics.R;
import ru.forblitz.statistics.adapters.MemberAdapter;
import ru.forblitz.statistics.dto.BigClanData;
import ru.forblitz.statistics.dto.Member;
import ru.forblitz.statistics.dto.SmallClanData;
import ru.forblitz.statistics.widget.common.DifferenceViewFlipper;

public class ClanScreen extends DifferenceViewFlipper {

    public ClanScreen(Context context) {
        super(context);
    }

    public ClanScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(SmallClanData smallClanData, BigClanData bigClanData) {

        if (bigClanData != null) {

            this.setDisplayedChild(IS_A_MEMBER);

            ((ClanInfo) this.findViewById(R.id.clan_info)).setData(smallClanData, bigClanData);

            ((ClanDetails) this.findViewById(R.id.clan_details)).setData(bigClanData);

            ((ListView) this.findViewById(R.id.clan_members_list)).setAdapter(
                    new MemberAdapter(
                            getContext(),
                            bigClanData
                                    .getMembers()
                                    .values()
                                    .toArray(new Member[0])
                    )
            );
            ((FloatingActionButton) this.findViewById(R.id.clan_members_back)).show();

        } else {

            this.setDisplayedChild(NOT_IS_A_MEMBER);

        }

    }

}
