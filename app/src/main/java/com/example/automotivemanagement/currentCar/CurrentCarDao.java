package com.example.automotivemanagement.currentCar;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface CurrentCarDao {

    @Query("select id from currentCar")
    int getCurrentCar();

    @Query("update currentCar set id = :id")
    void updateCurrentCar(int id);

    @Query("select * from currentCar")
    Cursor checkIfEmpty();

    @Insert
    long insert(CurrentCar currentCar);

}
