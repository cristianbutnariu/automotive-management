package com.example.automotivemanagement.shop;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DealershipDao {

    @Query("select * from dealers")
    List<Dealership> getAll();

    @Query("select * from dealers where make= :carMake")
    List<Dealership> loadDealerships(String carMake);

    @Query("select * from dealers where make= :carMake and :distance<100")
    List<Dealership> loadDealershipsCoordinatesFilter(String carMake,  double distance);
}
