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

import ru.forblitz.statistics.R;
import ru.forblitz.statistics.dto.Member;
import ru.forblitz.statistics.utils.InterfaceUtils;
import ru.forblitz.statistics.utils.ParseUtils;

public class MemberAdapter extends ArrayAdapter<Member> {

    final Context context;

    public MemberAdapter(@NonNull Context context, Member[] members) {
        super(context, R.layout.item_member, members);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Member member = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_member, null);
        }

        convertView.setLayoutParams(new ListView.LayoutParams(
                ListView.LayoutParams.MATCH_PARENT,
                (int) (InterfaceUtils.getX() * 0.15)
        ));

        convertView.setOnClickListener(l -> InterfaceUtils.search(context, member.getAccountName()));

        String name = member.getAccountName();
        String details = ParseUtils.formatClanRole(getContext(), member.getRole()) + "; " + getContext().getResources().getString(R.string.joined_at) + ParseUtils.formatSecondsTimestampToDate(member.getJoinedAt());

        TextView memberName = convertView.findViewById(R.id.member_name);
        TextView memberDetails = convertView.findViewById(R.id.member_details);

        memberName.setText(name);
        memberDetails.setText(details);

        return convertView;

    }

}