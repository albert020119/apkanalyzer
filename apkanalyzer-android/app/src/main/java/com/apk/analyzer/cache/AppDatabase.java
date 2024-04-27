package com.apk.analyzer.cache;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {Scan.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ScansCache scansCache();
}