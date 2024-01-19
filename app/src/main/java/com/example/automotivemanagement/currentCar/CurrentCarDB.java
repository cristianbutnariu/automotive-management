package com.example.automotivemanagement.currentCar;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {CurrentCar.class}, version = 1, exportSchema = false)
public abstract class CurrentCarDB extends RoomDatabase {

    public static final String DB_NAME = "currentCar.db";
    private static CurrentCarDB instance;

    public static CurrentCarDB getInstance(Context context)
    {
        if(instance==null)
            instance = Room.databaseBuilder(context, CurrentCarDB.class, DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        return instance;
    }

    public abstract CurrentCarDao getCurrentCarDao();
}