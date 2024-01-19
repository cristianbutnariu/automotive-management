package com.example.automotivemanagement.vehicle;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Car.class}, version = 2)
@TypeConverters({CarConverters.class})
public abstract class CarDB extends RoomDatabase {

    public static final String DB_NAME = "cars.db";
    private static CarDB instance;

    public static CarDB getInstance(Context context)
    {
        if(instance==null)
            instance = Room.databaseBuilder(context, CarDB.class, DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        return instance;
    }

    public abstract CarDao getCarDao();
}