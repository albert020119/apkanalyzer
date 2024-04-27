package com.apk.analyzer.cache;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScansCache {
    @Query("SELECT * FROM scan")
    List<Scan> getAll();


    @Insert
    void insertAll(Scan... scans);


}
