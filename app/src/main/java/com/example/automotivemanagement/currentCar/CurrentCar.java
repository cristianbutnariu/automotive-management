package com.example.automotivemanagement.currentCar;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity (tableName = "currentCar")
public class CurrentCar implements Serializable {
    @PrimaryKey
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CurrentCar(int id) {
        this.id = id;
    }
}
