package ru.forblitz.statistics.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ru.forblitz.statistics.R.layout;
import ru.forblitz.statistics.data.Utils;

public class NothingFoundAdapter extends ArrayAdapter<Object> {

    Activity activity;

    public NothingFoundAdapter(@NonNull Activity activity, List<Object> list) {
        super(activity, layout.item_nothing_found, list);
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layout.item_nothing_found, null);
        }

        convertView.setLayoutParams(new ListView.LayoutParams(
                ListView.LayoutParams.MATCH_PARENT,
                (int) (Utils.getY(activity) * 0.1)
        ));

        return convertView;

    }

}
