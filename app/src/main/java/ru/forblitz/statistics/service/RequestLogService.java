package ru.forblitz.statistics.service;

import java.util.ArrayList;

import ru.forblitz.statistics.dto.RequestLogItem;

public class RequestLogService {

    private final ArrayList<RequestLogItem> records = new ArrayList<>();

    private OnRecordAddedListener onRecordAddedListener;

    public void addRecord(long timestampOfSent, RequestType requestType, boolean completed) {
        records.add(new RequestLogItem(timestampOfSent, requestType, completed));
        if (onRecordAddedListener != null) {
            onRecordAddedListener.onRecordAdded(
                    new RequestLogItem(
                            timestampOfSent,
                            requestType,
                            completed
                    )
            );
        }
    }

    public void setOnRecordAddedListener(OnRecordAddedListener onRecordAddedListener) {
        this.onRecordAddedListener = onRecordAddedListener;
    }

    public ArrayList<RequestLogItem> getRecords() {
        return records;
    }

    public enum RequestType {
        ACCOUNT_ID,
        USER_STATISTICS,
        USER_CLAN_INFO,
        FULL_CLAN_INFO,
        ACHIEVEMENTS,
        TANKOPEDIA,
        TANKS_STATISTICS
    }

    public interface OnRecordAddedListener {
        void onRecordAdded(RequestLogItem requestLogItem);
    }

}
