package com.apk.analyzer.cache;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Scan {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "st")
    public long scan_time;

    @ColumnInfo(name = "md5")
    public String md5;

    @ColumnInfo(name = "pkn")
    public String pkn;

}
