package ru.forblitz.statistics.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ru.forblitz.statistics.R.string;

import ru.forblitz.statistics.R.id;
import ru.forblitz.statistics.R.layout;
import ru.forblitz.statistics.data.BigClanData.*;
import ru.forblitz.statistics.utils.Utils;

public class MemberAdapter extends ArrayAdapter<Member> {

    final Context context;

    public MemberAdapter(@NonNull Context context, Member[] members) {
        super(context, layout.item_member, members);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Member member = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layout.item_member, null);
        }

        convertView.setLayoutParams(new ListView.LayoutParams(
                ListView.LayoutParams.MATCH_PARENT,
                (int) (Utils.getX() * 0.15)
        ));

        String name = member.getAccountName();
        String details = Utils.parseRole(getContext(), member.getRole()) + "; " + getContext().getResources().getString(string.joined_at) + Utils.parseTime(member.getJoinedAt());

        TextView memberName = convertView.findViewById(id.member_name);
        TextView memberDetails = convertView.findViewById(id.member_details);

        memberName.setText(name);
        memberDetails.setText(details);

        return convertView;

    }

}