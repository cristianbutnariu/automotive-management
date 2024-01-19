package com.example.automotivemanagement.vehicle;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CarConverters {
    @TypeConverter
    public static Long fromDate(Date date)
    {
        return date.getTime();
    }

    @TypeConverter
    public static Date toDate(Long millis) { return new Date(millis);  }
}
