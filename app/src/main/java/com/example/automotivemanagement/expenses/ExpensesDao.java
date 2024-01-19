package com.example.automotivemanagement.expenses;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.List;

@Dao
public interface ExpensesDao {
    @Query("select * from expenses")
    List<Expenses> getAll();

    @Insert
    long insert(Expenses expenses);

    @Query("select * from expenses where type = :type and carId =:id")
    List<Expenses> loadExpenses(String type, int id);

    @Query("select sum(value) from expenses where type=:type and carId =:id")
    float getSum(String type, int id);

    @TypeConverters({ExpensesConverters.class})
    @Query("select * from expenses where type = :type and carId =:id and date >= :startDate and date <= :endDate")
    List<Expenses> loadExpensesFiltered(String type, int id, Date startDate, Date endDate);

    @TypeConverters({ExpensesConverters.class})
    @Query("select sum(value) from expenses where type=:type and carId =:id and date >= :startDate and date <= :endDate")
    float getSumFiltered(String type, int id, Date startDate, Date endDate);

    @TypeConverters({ExpensesConverters.class})
    @Query("delete from expenses where type=:type and value=:value and mileage=:mileage and details=:details and date=:date and time=:time and carId=:carId")
    void deleteExpense(String type, float value, int mileage, String details, Date date, String time, int carId);

    @TypeConverters({ExpensesConverters.class})
    @Query("select expensePhotoPath from expenses where type=:type and value=:value and mileage=:mileage and details=:details and date=:date and time=:time and carId=:carId")
    String getExpensePhotoPath(String type, float value, int mileage, String details, Date date, String time, int carId);

    @Query("delete from expenses where carId=:carId")
    void deleteExpenses(int carId);
}
