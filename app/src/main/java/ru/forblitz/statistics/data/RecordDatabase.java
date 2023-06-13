package ru.forblitz.statistics.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.forblitz.statistics.dto.Record;

@Database(entities = {Record.class}, version = 1, exportSchema = false)
public abstract class RecordDatabase extends RoomDatabase {
    public abstract RecordDao recordDao();
}

