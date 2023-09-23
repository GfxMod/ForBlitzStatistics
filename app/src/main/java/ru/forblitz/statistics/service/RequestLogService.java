package ru.forblitz.statistics.service;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;

import ru.forblitz.statistics.dto.RequestLogItem;

/**
 * The {@link RequestLogService} keeps a log of network requests
 */
public class RequestLogService {

    private final Activity activity;

    private final ArrayList<RequestLogItem> records = new ArrayList<>();

    private final HashMap<Object, OnRecordAddedListener> onRecordAddedListeners = new HashMap<>();

    public RequestLogService(Activity activity) {
        this.activity = activity;
    }

    public void addRecord(RequestLogItem requestLogItem) {
        if (!requestLogItem.isCompleted()) {
            activity.runOnUiThread(() -> records.add(requestLogItem));
        }

        onRecordAddedListeners.forEach((key, onRecordAddedListener) ->
                onRecordAddedListener.onRecordAdded(requestLogItem, records)
        );
    }

    public void addOnRecordAddedListener(Object key, OnRecordAddedListener onRecordAddedListener) {
        if (!onRecordAddedListeners.containsKey(key)) {
            onRecordAddedListeners.put(key, onRecordAddedListener);
        } else {
            onRecordAddedListeners.replace(key, onRecordAddedListener);
        }
    }

    public OnRecordAddedListener removeOnRecordAddedListener(Object key) {
        return onRecordAddedListeners.remove(key);
    }

    public ArrayList<RequestLogItem> getRecords() {
        return records;
    }

    /**
     * Removes all completed queries from the list, leaving only incomplete
     * ones
     */
    public void clearEndedRecords() {
        activity.runOnUiThread(() -> records.removeIf(RequestLogItem::isCompleted));
    }

    public interface OnRecordAddedListener {
        void onRecordAdded(RequestLogItem requestLogItem, ArrayList<RequestLogItem> records);
    }

}
