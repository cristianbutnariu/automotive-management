package com.example.automotivemanagement.expenses;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Expenses.class}, version = 2)
public abstract class ExpensesDB extends RoomDatabase {

    public static final String DB_NAME = "expenses.db";
    private static ExpensesDB instance;

    public static ExpensesDB getInstance(Context context)
    {
        if(instance==null)
            instance= Room.databaseBuilder(context, ExpensesDB.class, DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        return instance;
    }

    public abstract ExpensesDao getExpensesDao();
}
