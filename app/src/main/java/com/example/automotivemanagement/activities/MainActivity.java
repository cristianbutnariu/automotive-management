package com.example.automotivemanagement.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.automotivemanagement.R;
import com.example.automotivemanagement.currentCar.CurrentCar;
import com.example.automotivemanagement.currentCar.CurrentCarDB;
import com.example.automotivemanagement.expenses.ExpensesDB;
import com.example.automotivemanagement.fragments.ReminderFragment;
import com.example.automotivemanagement.fragments.ReportsFragment;
import com.example.automotivemanagement.fragments.ServiceFragment;
import com.example.automotivemanagement.fragments.WarningFragment;
import com.example.automotivemanagement.vehicle.CarDB;
import com.example.automotivemanagement.vehicle.CarDao;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    private Toolbar appbar;
    private ImageButton addCarButton, deleteCarButton;
    private Spinner carListSpinner;
    private CarDao getCarDao;
    private Cursor cursor;
    private int currentFragment;
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.reminder_menu:
                selectedFragment = new ReminderFragment();
                currentFragment = 0;
                break;
            case R.id.reports_menu:
                selectedFragment = new ReportsFragment();
                currentFragment = 1;
                break;
            case R.id.service_menu:
                selectedFragment = new ServiceFragment();
                currentFragment = 2;
                break;
            case R.id.warning_menu:
                selectedFragment = new WarningFragment();
                currentFragment = 3;
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selectedFragment).commit();
        return true;
    };

    private int idUpdate;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appbar = findViewById(R.id.appToolbar);

        BottomNavigationView bnv = findViewById(R.id.bottomNavbar);
        bnv.setOnNavigationItemSelectedListener(navListener);

        setSupportActionBar(appbar);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ReminderFragment()).commit();

        addCarButton = findViewById(R.id.addCar);
        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCar.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });


        CarDB carDB = CarDB.getInstance(getApplicationContext());
        cursor = carDB.getCarDao().getAll();
        ArrayList<String> carsList = new ArrayList<>();
        ArrayList<String> carTracking = new ArrayList<>();
        String cursorCreation;
        String carNumberPlate;
        if (cursor.moveToFirst()) {
            do {
                cursorCreation = cursor.getString(cursor.getColumnIndex("make")) + " " + cursor.getString(cursor.getColumnIndex("model"));
                carNumberPlate = cursor.getString(cursor.getColumnIndex("registrationPlate"));
                carsList.add(cursorCreation);
                carTracking.add(carNumberPlate);
            } while (cursor.moveToNext());

        }

        carListSpinner = findViewById(R.id.carListSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carListSpinner.setAdapter(spinnerAdapter);
        CurrentCarDB currentCarDB = CurrentCarDB.getInstance(getApplicationContext());
        carListSpinner.setSelection(currentCarDB.getCurrentCarDao().getCurrentCar() - 1);
        spinnerAdapter.addAll(carsList);
        spinnerAdapter.notifyDataSetChanged();


        Cursor check = currentCarDB.getCurrentCarDao().checkIfEmpty();

        if (check.getCount() == 0)
            currentCarDB.getCurrentCarDao().insert(new CurrentCar(1));


        carListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] carSearch = carsList.get(i).split("\\s+", 2);
                idUpdate = carDB.getCarDao().selectId(carSearch[0], carSearch[1], carTracking.get(i));
                Log.d("DATABASE", String.valueOf(idUpdate));
                currentCarDB.getCurrentCarDao().updateCurrentCar(idUpdate);

                ((TextView) view).setTextColor(Color.WHITE);

                Fragment selectedFragment = null;
                switch (currentFragment) {
                    case 0:
                        selectedFragment = new ReminderFragment();
                        break;
                    case 1:
                        selectedFragment = new ReportsFragment();
                        break;
                    case 2:
                        selectedFragment = new ServiceFragment();
                        break;
                    case 3:
                        selectedFragment = new WarningFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selectedFragment).commit();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        deleteCarButton = findViewById(R.id.deleteCarImageButton);
        deleteCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Do you want to delete the current selected car?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                CarDB carDatabase = CarDB.getInstance(getApplicationContext());
                                ExpensesDB expensesDatabase = ExpensesDB.getInstance(getApplicationContext());
                                carDatabase.getCarDao().deleteCar(idUpdate);
                                expensesDatabase.getExpensesDao().deleteExpenses(idUpdate);
                                Toast.makeText(getApplicationContext(), "Car deleted", Toast.LENGTH_LONG).show();
                                //spinnerAdapter.notifyDataSetChanged();
                                cursor = carDB.getCarDao().getAll();
                                ArrayList<String> carsList = new ArrayList<>();
                                ArrayList<String> carTracking = new ArrayList<>();
                                String cursorCreation;
                                String carNumberPlate;
                                if (cursor.moveToFirst()) {
                                    do {
                                        cursorCreation = cursor.getString(cursor.getColumnIndex("make")) + " " + cursor.getString(cursor.getColumnIndex("model"));
                                        carNumberPlate = cursor.getString(cursor.getColumnIndex("registrationPlate"));
                                        carsList.add(cursorCreation);
                                        carTracking.add(carNumberPlate);
                                    } while (cursor.moveToNext());

                                }

                                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, new ArrayList<>());
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                carListSpinner.setAdapter(spinnerAdapter);
                                CurrentCarDB currentCarDB = CurrentCarDB.getInstance(getApplicationContext());
                                carListSpinner.setSelection(currentCarDB.getCurrentCarDao().getCurrentCar() - 1);
                                spinnerAdapter.addAll(carsList);
                                spinnerAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                                Toast.makeText(getApplicationContext(), "No action has been taken",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.setTitle("Delete car");
                alert.show();
            }
        });


    }


}