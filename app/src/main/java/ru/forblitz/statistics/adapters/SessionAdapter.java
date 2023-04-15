package ru.forblitz.statistics.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import java.util.ArrayList;

import ru.forblitz.statistics.R;
import ru.forblitz.statistics.data.Session;
import ru.forblitz.statistics.utils.InterfaceUtils;
import ru.forblitz.statistics.utils.ParseUtils;

public class SessionAdapter extends ArrayAdapter<Session> {

    final Context context;

    public SessionAdapter(@NonNull Context context, ArrayList<Session> sessions) {
        super(context, R.layout.item_session, sessions);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Session session = getItem(position);

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_session, null);

        convertView.setLayoutParams(new ListView.LayoutParams(
                ListView.LayoutParams.MATCH_PARENT,
                (int) (InterfaceUtils.getX() * 0.15)
        ));

        String date = ParseUtils.parseTime(session.getPath().substring(session.getPath().lastIndexOf("-") + 1, session.getPath().lastIndexOf(".")));

        TextView sessionDate = convertView.findViewById(R.id.session_date);
        View sessionRemove = convertView.findViewById(R.id.session_remove);

        Drawable background;
        if (session.isSelected()) {
            background = AppCompatResources.getDrawable(context, R.drawable.background_layout_nested_selected);
        } else {
            background = AppCompatResources.getDrawable(context, R.drawable.background_layout_nested);
        }
        convertView.setBackground(background);

        sessionDate.setText(date);
        sessionDate.setOnClickListener(l -> session.getSet().run());
        sessionRemove.setOnClickListener(l -> session.getDelete().run());

        return convertView;

    }

}