package com.example.automotivemanagement.vehicle;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "cars")
public class Car implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String make;
    private String model;
    private String registrationPlate;
    private int year;
    @TypeConverters({CarConverters.class})
    private Date insurance;
    private Date insuranceCreation;
    private Date inspection;
    private Date inspectionCreation;
    private Date roadTax;
    private Date roadTaxCreation;
    private Date serviceDate;
    private Date serviceDateCreation;
    private int serviceKm;
    private String insurancePhotoPath;
    private String inspectionPhotoPath;
    private String roadTaxPhotoPath;
    private String servicePhotoPath;
    public Car(String make, String model, String registrationPlate, int year, Date insurance, Date insuranceCreation, Date inspection, Date inspectionCreation, Date roadTax, Date roadTaxCreation, Date serviceDate, Date serviceDateCreation, int serviceKm, String insurancePhotoPath, String inspectionPhotoPath, String roadTaxPhotoPath, String servicePhotoPath) {
        this.make = make;
        this.model = model;
        this.registrationPlate = registrationPlate;
        this.year = year;
        this.insurance = insurance;
        this.insuranceCreation = insuranceCreation;
        this.inspection = inspection;
        this.inspectionCreation = inspectionCreation;
        this.roadTax = roadTax;
        this.roadTaxCreation = roadTaxCreation;
        this.serviceDate = serviceDate;
        this.serviceDateCreation = serviceDateCreation;
        this.serviceKm = serviceKm;
        this.insurancePhotoPath = insurancePhotoPath;
        this.inspectionPhotoPath = inspectionPhotoPath;
        this.roadTaxPhotoPath = roadTaxPhotoPath;
        this.servicePhotoPath = servicePhotoPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getInsuranceCreation() {
        return insuranceCreation;
    }

    public void setInsuranceCreation(Date insuranceCreation) {
        this.insuranceCreation = insuranceCreation;
    }

    public Date getInspectionCreation() {
        return inspectionCreation;
    }

    public void setInspectionCreation(Date inspectionCreation) {
        this.inspectionCreation = inspectionCreation;
    }

    public Date getRoadTaxCreation() {
        return roadTaxCreation;
    }

    public void setRoadTaxCreation(Date roadTaxCreation) {
        this.roadTaxCreation = roadTaxCreation;
    }

    public Date getServiceDateCreation() {
        return serviceDateCreation;
    }

    public void setServiceDateCreation(Date serviceDateCreation) {
        this.serviceDateCreation = serviceDateCreation;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationPlate() {
        return registrationPlate;
    }

    public void setRegistrationPlate(String registrationPlate) {
        this.registrationPlate = registrationPlate;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Date getInsurance() {
        return insurance;
    }

    public void setInsurance(Date insurance) {
        this.insurance = insurance;
    }

    public Date getInspection() {
        return inspection;
    }

    public void setInspection(Date inspection) {
        this.inspection = inspection;
    }

    public Date getRoadTax() {
        return roadTax;
    }

    public void setRoadTax(Date roadTax) {
        this.roadTax = roadTax;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public int getServiceKm() {
        return serviceKm;
    }

    public void setServiceKm(int serviceKm) {
        this.serviceKm = serviceKm;
    }

    public String getInsurancePhotoPath() {
        return insurancePhotoPath;
    }

    public void setInsurancePhotoPath(String insurancePhotoPath) {
        this.insurancePhotoPath = insurancePhotoPath;
    }

    public String getInspectionPhotoPath() {
        return inspectionPhotoPath;
    }

    public void setInspectionPhotoPath(String inspectionPhotoPath) {
        this.inspectionPhotoPath = inspectionPhotoPath;
    }

    public String getRoadTaxPhotoPath() {
        return roadTaxPhotoPath;
    }

    public void setRoadTaxPhotoPath(String roadTaxPhotoPath) {
        this.roadTaxPhotoPath = roadTaxPhotoPath;
    }

    public String getServicePhotoPath() {
        return servicePhotoPath;
    }

    public void setServicePhotoPath(String servicePhotoPath) {
        this.servicePhotoPath = servicePhotoPath;
    }

    @Override
    public String toString() {
        return "Car{" +
                "make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", registrationPlate='" + registrationPlate + '\'' +
                ", year=" + year +
                ", insurance=" + insurance +
                ", insuranceCreation=" + insuranceCreation +
                ", inspection=" + inspection +
                ", inspectionCreation=" + inspectionCreation +
                ", roadTax=" + roadTax +
                ", roadTaxCreation=" + roadTaxCreation +
                ", serviceDate=" + serviceDate +
                ", serviceDateCreation=" + serviceDateCreation +
                ", serviceKm=" + serviceKm +
                '}';
    }
}
