package ru.forblitz.statistics.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ru.forblitz.statistics.R;
import ru.forblitz.statistics.dto.RequestLogItem;
import ru.forblitz.statistics.utils.ParseUtils;

public class RequestLogAdapter extends ArrayAdapter<RequestLogItem> {

    @LayoutRes
    private static final int resourceId = R.layout.item_request_log;

    private final ArrayList<RequestLogItem> items = new ArrayList<>();

    public RequestLogAdapter(@NonNull Context context, List<RequestLogItem> requestLogItems) {
        super(context, resourceId, requestLogItems);
        setNotifyOnChange(true);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        RequestLogItem requestLogItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
        }

        if (!requestLogItem.isCompleted()) {
            ((TextView) convertView).setTextColor(getContext().getColor(R.color.session_red));
            ((TextView) convertView).setText(createTextSent(requestLogItem));
        } else {
            ((TextView) convertView).setTextColor(getContext().getColor(R.color.session_green));
            ((TextView) convertView).setText(createTextReceived(requestLogItem));
        }

        return convertView;
    }

    public void add(@NonNull RequestLogItem requestLogItem, ListView parent) {
        if (!requestLogItem.isCompleted()) {
            super.add(requestLogItem);
            items.add(requestLogItem);
        } else {
            for (int i = 0; i < parent.getChildCount(); i++) {
                if (getItem(i).getTimestamp() == requestLogItem.getTimestamp()) {
                    TextView view = ((TextView) parent.getChildAt(i));

                    view.setText(createTextReceived(requestLogItem));
                    view.setTextColor(getContext().getColor(R.color.session_green));

                    getItem(i).setCompleted(true);
                    items.get(i).setCompleted(true);
                }
            }
        }
    }

    private String createTextSent(RequestLogItem requestLogItem) {
        String text = ParseUtils.formatMillisTimestampToTime(
                String.valueOf(requestLogItem.getTimestamp())
        );
        text += ": " + requestLogItem.getRequestType() + " — ";
        text += getContext().getResources().getString(R.string.log_request_sent);
        return text;
    }

    private String createTextReceived(RequestLogItem requestLogItem) {
        String text = ParseUtils.formatMillisTimestampToTime(
                String.valueOf(requestLogItem.getTimestamp())
        );
        text += ": " + requestLogItem.getRequestType() + " — ";
        text += getContext().getResources().getString(R.string.log_response_received);
        return text;
    }

}
