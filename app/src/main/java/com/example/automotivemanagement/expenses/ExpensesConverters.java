package com.example.automotivemanagement.expenses;

import androidx.room.TypeConverter;

import java.util.Date;

public class ExpensesConverters {

    @TypeConverter
    public static Long fromDate(Date date)
    {
        return date.getTime();
    }

    @TypeConverter
    public static Date toDate(Long millis) { return new Date(millis);  }
}
