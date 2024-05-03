package com.apk.analyzer.cache;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Locale;

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

    public String toString(){
        java.util.Date time = new java.util.Date((long)scan_time*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        return "scan time: " + sdf.format(time) + "\n" + "md5 hash: " + md5 + "\n" + "package name: " + pkn;
    }

}
