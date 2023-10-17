package ru.forblitz.statistics.dto;

import androidx.annotation.NonNull;

import java.util.Random;

public class RequestLogItem {

    private final long id;
    private final long timestamp;
    @NonNull
    private final RequestType requestType;
    private boolean isCompleted;

    public RequestLogItem(
            long timestampOfSent,
            @NonNull RequestType requestType,
            boolean isCompleted) {
        this.timestamp = timestampOfSent;
        this.requestType = requestType;
        this.isCompleted = isCompleted;
        this.id = timestampOfSent * (new Random()).nextInt(99);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public @NonNull RequestType getRequestType() {
        return requestType;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public long getId() {
        return id;
    }

    public enum RequestType {
        ACCOUNT_ID,
        USER_STATISTICS,
        USER_CLAN_INFO,
        FULL_CLAN_INFO,
        ACHIEVEMENTS,
        TANKOPEDIA,
        TANKS_STATISTICS,
        VERSION,
        TOKENS
    }

    @NonNull
    @Override
    public String toString() {
        return "RequestLogItem{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", requestType=" + requestType +
                ", isCompleted=" + isCompleted +
                '}';
    }

}
