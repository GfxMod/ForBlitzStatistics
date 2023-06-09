package ru.forblitz.statistics.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ru.forblitz.statistics.dto.Record;

@Dao
public interface RecordDao {
    @Query("SELECT * FROM history ORDER BY timestamp DESC")
    List<Record> getAllRecords();

    @Query("SELECT * FROM history ORDER BY timestamp DESC LIMIT :count")
    List<Record> getLatestRecords(int count);

    @Query("SELECT * FROM history WHERE region = :region GROUP BY nickname HAVING MAX(timestamp) ORDER BY timestamp DESC LIMIT :count")
    List<Record> getDistinctRecordsByRegion(String region, int count);

    @Insert
    void addRecord(Record record);
}
