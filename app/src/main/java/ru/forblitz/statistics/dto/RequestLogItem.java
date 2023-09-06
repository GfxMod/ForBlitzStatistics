package ru.forblitz.statistics.dto;

import ru.forblitz.statistics.service.RequestLogService;

public class RequestLogItem {

    private long timestamp;
    private RequestLogService.RequestType requestType;
    private boolean isCompleted;

    public RequestLogItem(long timestampOfSent, RequestLogService.RequestType requestType, boolean isCompleted) {
        this.timestamp = timestampOfSent;
        this.requestType = requestType;
        this.isCompleted = isCompleted;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public RequestLogService.RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestLogService.RequestType requestType) {
        this.requestType = requestType;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

}
