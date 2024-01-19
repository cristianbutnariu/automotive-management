package com.example.automotivemanagement.reports;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.automotivemanagement.R;
import com.example.automotivemanagement.adapters.EmptyListViewAdapter;
import com.example.automotivemanagement.adapters.ReportsListViewAdapter;
import com.example.automotivemanagement.currentCar.CurrentCarDB;
import com.example.automotivemanagement.expenses.Expenses;
import com.example.automotivemanagement.expenses.ExpensesDB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FuelReport extends AppCompatActivity {

    public ReportsListViewAdapter rlva;
    public EmptyListViewAdapter elva;
    public ListView fuelExpenses;
    TextView totalPaid;
    View layout;
    EditText startDate, endDate;
    DatePickerDialog dpd;
    ImageButton setFilterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_report);

        ExpensesDB db = ExpensesDB.getInstance(getApplicationContext());
        CurrentCarDB dbCar = CurrentCarDB.getInstance(getApplicationContext());
        int id = dbCar.getCurrentCarDao().getCurrentCar();
        List<Expenses> expensesList;
        expensesList = db.getExpensesDao().loadExpenses("fuel", id);
        totalPaid = findViewById(R.id.priceTV);
        totalPaid.setText("Total spent: " + db.getExpensesDao().getSum("fuel", id) + " RON");

        fuelExpenses = findViewById(R.id.fuelReportLv);
        startDate = findViewById(R.id.startDateEditText);
        endDate = findViewById(R.id.endDateEditText);

        Expenses p1 = new Expenses("No report added", " ");
        List<Expenses> placeholder = new ArrayList<>();
        placeholder.add(p1);

        rlva = new ReportsListViewAdapter(expensesList, getApplicationContext());
        elva = new EmptyListViewAdapter(placeholder, getApplicationContext());

        if (expensesList.size() >= 1) {
            fuelExpenses.setAdapter(rlva);
        } else {
            fuelExpenses.setAdapter(elva);
        }

        fuelExpenses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        fuelExpenses.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(FuelReport.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                startDate.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();

            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(FuelReport.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                endDate.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

        setFilterButton = findViewById(R.id.setFilter);
        setFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date startingDate = null;
                Date endingDate = null;
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    startingDate = format.parse(String.valueOf(startDate.getText()));
                    endingDate = format.parse(String.valueOf(endDate.getText()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                List<Expenses> expensesListFiltered;
                if (startingDate == null || endingDate == null) {
                    Toast.makeText(getApplicationContext(), "Please set a valid interval", Toast.LENGTH_LONG).show();
                } else {
                    expensesListFiltered = db.getExpensesDao().loadExpensesFiltered("fuel", id, startingDate, endingDate);
                    rlva = new ReportsListViewAdapter(expensesListFiltered, getApplicationContext());
                    fuelExpenses.setAdapter(rlva);
                    totalPaid.setText("Total spent: " + db.getExpensesDao().getSumFiltered("fuel", id, startingDate, endingDate) + " RON");
                }
            }
        });
    }
}