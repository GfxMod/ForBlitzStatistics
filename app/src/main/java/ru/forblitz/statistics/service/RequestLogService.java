package ru.forblitz.statistics.service;

import android.app.Activity;

import java.util.ArrayList;

import ru.forblitz.statistics.dto.RequestLogItem;

/**
 * The {@link RequestLogService} keeps a log of network requests
 */
public class RequestLogService {

    private final Activity activity;

    private final ArrayList<RequestLogItem> records = new ArrayList<>();

    private OnRecordAddedListener onRecordAddedListener;

    public RequestLogService(Activity activity) {
        this.activity = activity;
    }

    public void addRecord(RequestLogItem requestLogItem) {
        if (!requestLogItem.isCompleted()) {
            activity.runOnUiThread(() -> records.add(requestLogItem));
        }

        if (onRecordAddedListener != null) { onRecordAddedListener.onRecordAdded(requestLogItem); }
    }

    public void setOnRecordAddedListener(OnRecordAddedListener onRecordAddedListener) {
        this.onRecordAddedListener = onRecordAddedListener;
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
        void onRecordAdded(RequestLogItem requestLogItem);
    }

}
