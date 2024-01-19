package com.example.automotivemanagement.shop;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Dealership.class}, version = 1)
public abstract class DealershipDB extends RoomDatabase {

    public static final String DB_NAME = "dealerships.db";
    private static DealershipDB instance;

    public static DealershipDB getInstance(Context context)
    {
        if(instance==null)
            instance= Room.databaseBuilder(context, DealershipDB.class, DB_NAME)
                      .createFromAsset("database/dealerships.db")
                      .allowMainThreadQueries()
                      .fallbackToDestructiveMigration()
                      .build();
        return instance;
    }

    public abstract DealershipDao getDealershipDao();
}
