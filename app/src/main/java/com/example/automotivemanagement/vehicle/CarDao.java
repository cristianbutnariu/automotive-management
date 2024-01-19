package com.example.automotivemanagement.vehicle;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;

@Dao
public interface CarDao {
    @Insert
    long insert(Car car);

    @Query("select * from cars")
    Cursor getAll();

    @Query("select id from cars where make=:make and model=:model and registrationPlate =:registrationPlate")
    int selectId(String make, String model, String registrationPlate);

    @Query("delete from cars")
    void deleteAll();

    @Delete
    void delete(Car car);

    @Query("select make from cars where id=:id")
    String getCarMake(int id);

    @Query("select make, model from cars")
    Cursor getAllCars();

    @Query("select insurance from cars where id = :carId")
    long insuranceDate(int carId);

    @Query("select insuranceCreation from cars where id = :carId")
    long insuranceCreationDate(int carId);

    @Query("select inspection from cars where id = :carId")
    long inspectionDate(int carId);

    @Query("select inspectionCreation from cars where id = :carId")
    long inspectionCreationDate(int carId);

    @Query("select roadTax from cars where id = :carId")
    long roadTaxDate(int carId);

    @Query("select roadTaxCreation from cars where id = :carId")
    long roadTaxCreationDate(int carId);

    @Query("select serviceDate from cars where id = :carId")
    long serviceDate(int carId);

    @Query("select serviceDateCreation from cars where id = :carId")
    long serviceCreationDate(int carId);

    @Query("update cars set insuranceCreation= :newInsuranceCreation where id = :carId")
    void updateInsuranceCreation(Date newInsuranceCreation, int carId);

    @Query("update cars set insurance= :newInsuranceExpiry where id = :carId")
    void updateInsuranceExpiry(Date newInsuranceExpiry, int carId);

    @Query("update cars set inspectionCreation= :newInspectionCreation where id = :carId")
    void updateInspectionCreation(Date newInspectionCreation, int carId);

    @Query("update cars set inspection= :newInspectionExpiry where id = :carId")
    void updateInspectionExpiry(Date newInspectionExpiry, int carId);

    @Query("update cars set roadTaxCreation= :newRoadTaxCreation where id = :carId")
    void updateRoadTaxCreation(Date newRoadTaxCreation, int carId);

    @Query("update cars set roadTax= :newRoadTaxExpiry where id = :carId")
    void updateRoadTaxExpiry(Date newRoadTaxExpiry, int carId);

    @Query("update cars set serviceDateCreation= :newServiceCreation where id = :carId")
    void updateServiceCreation(Date newServiceCreation, int carId);

    @Query("update cars set serviceDate= :newServiceExpiry where id = :carId")
    void updateServiceExpiry(Date newServiceExpiry, int carId);

    @Query("select insurancePhotoPath from cars where id = :carId")
    String getInsurancePicturePath(int carId);

    @Query("select inspectionPhotoPath from cars where id = :carId")
    String getInspectionPicturePath(int carId);

    @Query("select roadTaxPhotoPath from cars where id = :carId")
    String getRoadTaxPicturePath(int carId);

    @Query("select servicePhotoPath from cars where id = :carId")
    String getServicePicturePath(int carId);

    @Query("update cars set insurancePhotoPath= :newInsurancePhotoPath where id = :carId")
    void updateInsurancePhotoPath(String newInsurancePhotoPath, int carId);

    @Query("update cars set inspectionPhotoPath= :newInspectionPhotoPath where id = :carId")
    void updateInspectionPhotoPath(String newInspectionPhotoPath, int carId);

    @Query("update cars set roadTaxPhotoPath= :newRoadTaxPhotoPath where id = :carId")
    void updateRoadTaxPhotoPath(String newRoadTaxPhotoPath, int carId);

    @Query("update cars set insurancePhotoPath= :newServicePhotoPath where id = :carId")
    void updateServicePhotoPath(String newServicePhotoPath, int carId);

    @Query("delete from cars where id=:carId")
    void deleteCar(int carId);



}
