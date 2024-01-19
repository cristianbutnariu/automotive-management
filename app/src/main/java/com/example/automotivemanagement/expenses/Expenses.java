package com.example.automotivemanagement.expenses;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "expenses")
public class Expenses implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String type;
    private float value;
    private int mileage;
    private String details;
    @TypeConverters({ExpensesConverters.class})
    private Date date;
    private String time;
    private int carId;
    private String expensePhotoPath;

    public Expenses(String type, float value, int mileage, String details, Date date, String time, int carId, String expensePhotoPath) {
        this.type = type;
        this.value = value;
        this.mileage = mileage;
        this.details = details;
        this.date = date;
        this.time = time;
        this.carId = carId;
        this.expensePhotoPath = expensePhotoPath;
    }

    @Ignore
    public Expenses(String details, String time) {
        this.details = details;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getExpensePhotoPath() {
        return expensePhotoPath;
    }

    public void setExpensePhotoPath(String expensePhotoPath) {
        this.expensePhotoPath = expensePhotoPath;
    }

    @Override
    public String toString() {
        return "Expenses{" +
                "type='" + type + '\'' +
                ", value=" + value +
                ", mileage=" + mileage +
                ", details='" + details + '\'' +
                ", date=" + date +
                ", time='" + time + '\'' +
                ", carId=" + carId +
                '}';
    }
}
