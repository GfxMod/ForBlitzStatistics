package ru.forblitz.statistics.dto;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "history", primaryKeys = {"player_id", "timestamp"})
public class Record {

    public Record(
            @NonNull String playerId,
            @NonNull String nickname,
            @NonNull String timestamp,
            @NonNull String region
    ) {
        this.playerId = playerId;
        this.nickname = nickname;
        this.timestamp = timestamp;
        this.region = region;
    }

    @NonNull
    @ColumnInfo(name = "player_id")
    public String playerId;

    @NonNull
    @ColumnInfo(name = "nickname")
    public String nickname;

    @NonNull
    @ColumnInfo(name = "timestamp")
    public String timestamp;

    @NonNull
    @ColumnInfo(name = "region")
    public String region;
}