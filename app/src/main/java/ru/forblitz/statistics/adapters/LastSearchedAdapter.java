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
import ru.forblitz.statistics.dto.Record;
import ru.forblitz.statistics.utils.InterfaceUtils;
import ru.forblitz.statistics.utils.ParseUtils;

public class LastSearchedAdapter extends ArrayAdapter<Record> {

    private final Context context;

    private final int itemHeight;

    public LastSearchedAdapter(@NonNull Context context, Record[] records, int itemHeight) {
        super(context, R.layout.item_last_search, records);
        this.context = context;
        this.itemHeight = itemHeight;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Record record = getItem(position);

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_last_search, null);

        convertView.setLayoutParams(new ListView.LayoutParams(
                ListView.LayoutParams.MATCH_PARENT,
                itemHeight
        ));

        convertView.setOnClickListener(l -> InterfaceUtils.search(context, record.nickname));

        String nickname = record.nickname;
        String date =
                getContext().getResources().getString(R.string.last_search) +
                        ParseUtils.formatMillisTimestampToDate(record.timestamp);

        ((TextView) convertView.findViewWithTag("nickname")).setText(nickname);
        ((TextView) convertView.findViewWithTag("date")).setText(date);

        return convertView;

    }

}
