package ru.forblitz.statistics.service;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;

import ru.forblitz.statistics.dto.RequestLogItem;

/**
 * The {@link RequestsService} keeps a log of network requests
 */
public class RequestsService {

    private final Activity activity;

    private final ArrayList<RequestLogItem> records = new ArrayList<>();

    private final HashMap<Object, OnRequestListChangedListener> onRequestListChangedListeners = new HashMap<>();

    public RequestsService(Activity activity) {
        this.activity = activity;
    }

    public void addRecord(RequestLogItem requestLogItem) {
        if (!requestLogItem.isCompleted()) {
            activity.runOnUiThread(() -> {
                records.add(requestLogItem);
                notifyRequestListChanged();
            });
        }
    }

    public void completeRecord(RequestLogItem requestLogItem) {
        activity.runOnUiThread(() -> {
            requestLogItem.setCompleted(true);
            notifyRequestListChanged();
        });
    }

    public void addOnRequestListChangedListener(Object key, OnRequestListChangedListener onRequestListChangedListener) {
        if (!onRequestListChangedListeners.containsKey(key)) {
            onRequestListChangedListeners.put(key, onRequestListChangedListener);
        } else {
            onRequestListChangedListeners.replace(key, onRequestListChangedListener);
        }
    }

    public OnRequestListChangedListener removeOnRecordAddedListener(Object key) {
        return onRequestListChangedListeners.remove(key);
    }

    private void notifyRequestListChanged() {
        onRequestListChangedListeners.forEach((key, onRequestListChangedListener) ->
                onRequestListChangedListener.onRequestListChanged(records)
        );
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

    public interface OnRequestListChangedListener {
        void onRequestListChanged(ArrayList<RequestLogItem> records);
    }

}
