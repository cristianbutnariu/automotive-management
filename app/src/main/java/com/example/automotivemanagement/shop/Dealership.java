package com.example.automotivemanagement.shop;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName  = "dealers")
public class Dealership {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String make;
    private String town;
    private String name;
    private String phoneNumber;
    private String address;
    private String description;
    private double longitude;
    private double latitude;

    @Ignore
    public Dealership(int id, String make, String town, String name, String phoneNumber, String address, String description, double longitude, double latitude) {
        this.id = id;
        this.make = make;
        this.town = town;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Ignore
    public Dealership(String name, String phoneNumber, String address, String description) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.description = description;
    }

    public Dealership()
    {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Dealership{" +
                "id=" + id +
                ", make='" + make + '\'' +
                ", town='" + town + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
