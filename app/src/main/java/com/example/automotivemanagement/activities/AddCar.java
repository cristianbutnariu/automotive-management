package com.example.automotivemanagement.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.example.automotivemanagement.BuildConfig;
import com.example.automotivemanagement.R;
import com.example.automotivemanagement.currentCar.CurrentCar;
import com.example.automotivemanagement.currentCar.CurrentCarDB;
import com.example.automotivemanagement.expenses.ExpensesDB;
import com.example.automotivemanagement.notifications.ReminderBroadcast;
import com.example.automotivemanagement.vehicle.Car;
import com.example.automotivemanagement.vehicle.CarDB;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddCar extends AppCompatActivity {

    public static final String ADD_CAR = "addCar";

    private Spinner carMake, carModel, carYear;
    private Spinner spinnerInsurance, spinnerInspection, spinnerRoadtax, spinnerService, carListSpinner;
    private TextView carPlate;
    private Button addCar;
    private Toolbar appbar;
    private Cursor cursor;
    private TextView textInsurance, textInspection, textRoadtax, textServiceDate, insurancePhotoButtonText, inspectionPhotoButtonText, roadTaxPhotoButtonText, servicePhotoButtonText;
    private DatePickerDialog.OnDateSetListener insuranceListener, inspectionListener, roadtaxListener, serviceListener;
    private ImageButton addInsurancePhoto, addInspectionPhoto, addRoadTaxPhoto, addServicePhoto, gallery, camera;
    private OutputStream outputStream;
    private String insurancePath = "", inspectionPath = "", roadTaxPath = "", servicePath = "";
    ActivityResultLauncher<Intent> activityResultLauncherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            upload = 1;
            try {
                Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(insurancePath));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
    private int selectionID, upload = 0;
    ActivityResultLauncher<Intent> activityResultLauncherGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            Uri photoUri = result.getData().getData();
            String selection;
            upload = 1;
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(photoUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            switch (selectionID) {
                case 1:
                    selection = "Insurance_";
                    break;
                case 2:
                    selection = "Inspection_";
                    break;
                case 3:
                    selection = "RoadTax_";
                    break;
                default:
                    selection = "Service_";
                    break;
            }

            String filename = selection + System.currentTimeMillis() + ".jpg";
            File imageFile = new File(getApplicationContext().getFilesDir(), filename);
            String path = getApplicationContext().getFilesDir() + "/" + filename;

            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(imageFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            switch (selectionID) {
                case 1:
                    insurancePath = path;
                    break;
                case 2:
                    inspectionPath = path;
                    break;
                case 3:
                    roadTaxPath = path;
                    break;
                default:
                    servicePath = path;
                    break;
            }
        }

    });


    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        createNotificationChannel();

        addInsurancePhoto = findViewById(R.id.imgButtonInsurance);
        addInspectionPhoto = findViewById(R.id.imgButtonInspection);
        addRoadTaxPhoto = findViewById(R.id.imgButtonRoadTax);
        addServicePhoto = findViewById(R.id.imgButtonService);

        insurancePhotoButtonText = findViewById(R.id.textView6);
        inspectionPhotoButtonText = findViewById(R.id.textView14);
        roadTaxPhotoButtonText = findViewById(R.id.textView16);
        servicePhotoButtonText = findViewById(R.id.textView17);

        Intent intent = getIntent();

        appbar = findViewById(R.id.appToolbar);
        setSupportActionBar(appbar);
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

        spinnerAdapter.addAll(carsList);
        spinnerAdapter.notifyDataSetChanged();

        CurrentCarDB currentCarDB = CurrentCarDB.getInstance(getApplicationContext());
        Cursor check = currentCarDB.getCurrentCarDao().checkIfEmpty();

        if (check.getCount() == 0)
            currentCarDB.getCurrentCarDao().insert(new CurrentCar(1));

        carListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] carSearch = carsList.get(i).split("\\s+");
                int idUpdate = carDB.getCarDao().selectId(carSearch[0], carSearch[1], carTracking.get(i));
                currentCarDB.getCurrentCarDao().updateCurrentCar(idUpdate);

                ((TextView) view).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        carMake = findViewById(R.id.carMakeSpinner);
        carModel = findViewById(R.id.carModelSpinner);
        carYear = findViewById(R.id.carYearSpinner);
        carPlate = findViewById(R.id.carRegplate);

        spinnerInsurance = findViewById(R.id.insuranceSpinner);
        spinnerInspection = findViewById(R.id.inspectionSpinner);
        spinnerRoadtax = findViewById(R.id.roadtaxSpinner);
        spinnerService = findViewById(R.id.serviceSpinner);

        addCar = findViewById(R.id.button);

        Integer[] year = new Integer[]{1990, 1991, 1992, 1993, 1994, 1995, 1996, 1997, 1998, 1999, 2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022};
        ArrayAdapter<Integer> years = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, year);
        carYear.setAdapter(years);

        ArrayAdapter<String> carList = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.carlist));
        carMake.setAdapter(carList);
        //region Cars array declaration
        ArrayAdapter<String> abarth = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.abarth));
        ArrayAdapter<String> alfaromeo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.alfaromeo));
        ArrayAdapter<String> astonmartin = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.astonmartin));
        ArrayAdapter<String> audi = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.audi));
        ArrayAdapter<String> bentley = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.bentley));
        ArrayAdapter<String> bmw = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.bmw));
        ArrayAdapter<String> bugatti = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.bugatti));
        ArrayAdapter<String> cadillac = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.cadillac));
        ArrayAdapter<String> chevrolet = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.chevrolet));
        ArrayAdapter<String> chrysler = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.chrysler));
        ArrayAdapter<String> citroen = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.citroen));
        ArrayAdapter<String> cupra = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.cupra));
        ArrayAdapter<String> dacia = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.dacia));
        ArrayAdapter<String> daewoo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.daewoo));
        ArrayAdapter<String> daihatsu = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.daihatsu));
        ArrayAdapter<String> dodge = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.dodge));
        ArrayAdapter<String> ds = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.ds));
        ArrayAdapter<String> ferrari = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.ferrari));
        ArrayAdapter<String> fiat = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.fiat));
        ArrayAdapter<String> ford = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.ford));
        ArrayAdapter<String> honda = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.honda));
        ArrayAdapter<String> hummer = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.hummer));
        ArrayAdapter<String> hyundai = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.hyundai));
        ArrayAdapter<String> infiniti = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.infiniti));
        ArrayAdapter<String> jaguar = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.jaguar));
        ArrayAdapter<String> jeep = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.jeep));
        ArrayAdapter<String> kia = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.kia));
        ArrayAdapter<String> koenigsegg = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.koenigsegg));
        ArrayAdapter<String> lada = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.lada));
        ArrayAdapter<String> lamboghini = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.lamborghini));
        ArrayAdapter<String> lancia = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.lancia));
        ArrayAdapter<String> landrover = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.landrover));
        ArrayAdapter<String> lexus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.lexus));
        ArrayAdapter<String> maserati = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.maserati));
        ArrayAdapter<String> mazda = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.mazda));
        ArrayAdapter<String> mercedesbenz = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.mercedesbenz));
        ArrayAdapter<String> mini = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.mini));
        ArrayAdapter<String> mitsubishi = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.mitsubishi));
        ArrayAdapter<String> nissan = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.nissan));
        ArrayAdapter<String> opel = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.opel));
        ArrayAdapter<String> peugeot = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.peugeot));
        ArrayAdapter<String> porsche = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.porsche));
        ArrayAdapter<String> renault = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.renault));
        ArrayAdapter<String> rollsroyce = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.rollsroyce));
        ArrayAdapter<String> saab = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.saab));
        ArrayAdapter<String> seat = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.seat));
        ArrayAdapter<String> skoda = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.skoda));
        ArrayAdapter<String> smart = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.smart));
        ArrayAdapter<String> ssangyong = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.ssangyong));
        ArrayAdapter<String> subaru = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.subaru));
        ArrayAdapter<String> suzuki = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.suzuki));
        ArrayAdapter<String> tesla = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.tesla));
        ArrayAdapter<String> toyota = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.toyota));
        ArrayAdapter<String> volkswagen = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.volkswagen));
        ArrayAdapter<String> volvo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.volvo));
        //endregion

        ArrayAdapter<String> insurancePeriod = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.insuranceDuration));
        spinnerInsurance.setAdapter(insurancePeriod);

        ArrayAdapter<String> inspectionPeriod = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.inspectionDuration));
        spinnerInspection.setAdapter(inspectionPeriod);

        ArrayAdapter<String> roadtaxPeriod = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.roadTaxDuration));
        spinnerRoadtax.setAdapter(roadtaxPeriod);

        ArrayAdapter<String> servicePeriod = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.serviceDuration));
        spinnerService.setAdapter(servicePeriod);

        carMake.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (carMake.getSelectedItem().toString()) {
                    case "Abarth":
                        carModel.setAdapter(abarth);
                        break;
                    case "Alfa Romeo":
                        carModel.setAdapter(alfaromeo);
                        break;
                    case "Aston Martin":
                        carModel.setAdapter(astonmartin);
                        break;
                    case "Audi":
                        carModel.setAdapter(audi);
                        break;
                    case "Bentley":
                        carModel.setAdapter(bentley);
                        break;
                    case "BMW":
                        carModel.setAdapter(bmw);
                        break;
                    case "Bugatti":
                        carModel.setAdapter(bugatti);
                        break;
                    case "Cadillac":
                        carModel.setAdapter(cadillac);
                        break;
                    case "Chevrolet":
                        carModel.setAdapter(chevrolet);
                        break;
                    case "Chrysler":
                        carModel.setAdapter(chrysler);
                        break;
                    case "Citroen":
                        carModel.setAdapter(citroen);
                        break;
                    case "Cupra":
                        carModel.setAdapter(cupra);
                        break;
                    case "Dacia":
                        carModel.setAdapter(dacia);
                        break;
                    case "Daewoo":
                        carModel.setAdapter(daewoo);
                        break;
                    case "Daihatsu":
                        carModel.setAdapter(daihatsu);
                        break;
                    case "Dodge":
                        carModel.setAdapter(dodge);
                        break;
                    case "DS":
                        carModel.setAdapter(ds);
                        break;
                    case "Ferrari":
                        carModel.setAdapter(ferrari);
                        break;
                    case "Fiat":
                        carModel.setAdapter(fiat);
                        break;
                    case "Ford":
                        carModel.setAdapter(ford);
                        break;
                    case "Honda":
                        carModel.setAdapter(honda);
                        break;
                    case "Hummer":
                        carModel.setAdapter(hummer);
                        break;
                    case "Hyundai":
                        carModel.setAdapter(hyundai);
                        break;
                    case "Infiniti":
                        carModel.setAdapter(infiniti);
                        break;
                    case "Jaguar":
                        carModel.setAdapter(jaguar);
                        break;
                    case "Jeep":
                        carModel.setAdapter(jeep);
                        break;
                    case "Kia":
                        carModel.setAdapter(kia);
                        break;
                    case "Koenigsegg":
                        carModel.setAdapter(koenigsegg);
                        break;
                    case "Lada":
                        carModel.setAdapter(lada);
                        break;
                    case "Lamborghini":
                        carModel.setAdapter(lamboghini);
                        break;
                    case "Lancia":
                        carModel.setAdapter(lancia);
                        break;
                    case "Land Rover":
                        carModel.setAdapter(landrover);
                        break;
                    case "Lexus":
                        carModel.setAdapter(lexus);
                        break;
                    case "Maserati":
                        carModel.setAdapter(maserati);
                        break;
                    case "Mazda":
                        carModel.setAdapter(mazda);
                        break;
                    case "Mercedes-Benz":
                        carModel.setAdapter(mercedesbenz);
                        break;
                    case "Mini":
                        carModel.setAdapter(mini);
                        break;
                    case "Mitsubishi":
                        carModel.setAdapter(mitsubishi);
                        break;
                    case "Nissan":
                        carModel.setAdapter(nissan);
                        break;
                    case "Opel":
                        carModel.setAdapter(opel);
                        break;
                    case "Peugeot":
                        carModel.setAdapter(peugeot);
                        break;
                    case "Porsche":
                        carModel.setAdapter(porsche);
                        break;
                    case "Renault":
                        carModel.setAdapter(renault);
                        break;
                    case "Rolls-Royce":
                        carModel.setAdapter(rollsroyce);
                        break;
                    case "Saab":
                        carModel.setAdapter(saab);
                        break;
                    case "Seat":
                        carModel.setAdapter(seat);
                        break;
                    case "Skoda":
                        carModel.setAdapter(skoda);
                        break;
                    case "Smart":
                        carModel.setAdapter(smart);
                        break;
                    case "SsangYong":
                        carModel.setAdapter(ssangyong);
                        break;
                    case "Subaru":
                        carModel.setAdapter(subaru);
                        break;
                    case "Suzuki":
                        carModel.setAdapter(suzuki);
                        break;
                    case "Tesla":
                        carModel.setAdapter(tesla);
                        break;
                    case "Toyota":
                        carModel.setAdapter(toyota);
                        break;
                    case "Volkswagen":
                        carModel.setAdapter(volkswagen);
                        break;
                    case "Volvo":
                        carModel.setAdapter(volvo);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        textInsurance = findViewById(R.id.tvInsurance);
        textInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddCar.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, insuranceListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        insuranceListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "-" + month + "-" + year;

                textInsurance.setText(date);
            }
        };

        textInspection = findViewById(R.id.tvInspection);
        textInspection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddCar.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, inspectionListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        inspectionListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "-" + month + "-" + year;

                textInspection.setText(date);
            }
        };

        textRoadtax = findViewById(R.id.tvRoadTax);
        textRoadtax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddCar.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, roadtaxListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        roadtaxListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "-" + month + "-" + year;

                textRoadtax.setText(date);
            }
        };

        textServiceDate = findViewById(R.id.tvServiceDate);
        textServiceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddCar.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, serviceListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        serviceListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "-" + month + "-" + year;

                textServiceDate.setText(date);
            }
        };


        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");

                String addCarMake = carMake.getSelectedItem().toString();
                String addCarModel = carModel.getSelectedItem().toString();
                int addCarYear = (int) carYear.getSelectedItem();
                String addCarRegplate = carPlate.getText().toString();
                Date date = new Date(System.currentTimeMillis());
                Date carInsuranceCreation = date, carInspectionCreation = date, carRoadTaxCreation = date, carServiceDateCreation = date;
                Date carInsuranceExpiry, carInspectionExpiry, carRoadtaxExpiry, carServiceExpiry;

                String tvDate = sdf.format(date);
                textInsurance.setText(tvDate);
                textInspection.setText(tvDate);
                textRoadtax.setText(tvDate);
                textServiceDate.setText(tvDate);

                try {
                    carInsuranceCreation = sdf.parse(textInsurance.getText().toString());
                    carInspectionCreation = sdf.parse(textInspection.getText().toString());
                    carRoadTaxCreation = sdf.parse(textRoadtax.getText().toString());
                    carServiceDateCreation = sdf.parse(textServiceDate.getText().toString());
                } catch (ParseException e) {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                    //e.printStackTrace();
                }

                switch (spinnerInsurance.getSelectedItem().toString()) {
                    case "2 Months":
                        carInsuranceExpiry = new Date(carInsuranceCreation.getTime() + 5090400000L - 50400000L);
                        setNotification30Days(addCarMake, addCarModel, "insurance", carInsuranceExpiry.getTime());
                        setNotification7Days(addCarMake, addCarModel, "insurance", carInsuranceExpiry.getTime());
                        setNotification1Day(addCarMake, addCarModel, "insurance", carInsuranceExpiry.getTime());
                        break;
                    case "3 Months":
                        carInsuranceExpiry = new Date(carInsuranceCreation.getTime() + 7768800000L - 50400000L);
                        setNotification30Days(addCarMake, addCarModel, "insurance", carInsuranceExpiry.getTime());
                        setNotification7Days(addCarMake, addCarModel, "insurance", carInsuranceExpiry.getTime());
                        setNotification1Day(addCarMake, addCarModel, "insurance", carInsuranceExpiry.getTime());
                        break;
                    case "6 Months":
                        carInsuranceExpiry = new Date(carInsuranceCreation.getTime() + 15631200000L - 50400000L);
                        setNotification30Days(addCarMake, addCarModel, "insurance", carInsuranceExpiry.getTime());
                        setNotification7Days(addCarMake, addCarModel, "insurance", carInsuranceExpiry.getTime());
                        setNotification1Day(addCarMake, addCarModel, "insurance", carInsuranceExpiry.getTime());
                        break;
                    case "9 Months":
                        carInsuranceExpiry = new Date(carInsuranceCreation.getTime() + 23580000000L - 50400000L);
                        setNotification30Days(addCarMake, addCarModel, "insurance", carInsuranceExpiry.getTime());
                        setNotification7Days(addCarMake, addCarModel, "insurance", carInsuranceExpiry.getTime());
                        setNotification1Day(addCarMake, addCarModel, "insurance", carInsuranceExpiry.getTime());
                        break;
                    case "11 Months":
                        carInsuranceExpiry = new Date(carInsuranceCreation.getTime() + 28850400000L - 50400000L);
                        setNotification30Days(addCarMake, addCarModel, "insurance", carInsuranceExpiry.getTime());
                        setNotification7Days(addCarMake, addCarModel, "insurance", carInsuranceExpiry.getTime());
                        setNotification1Day(addCarMake, addCarModel, "insurance", carInsuranceExpiry.getTime());
                        break;
                    case "12 Months":
                        carInsuranceExpiry = new Date(carInsuranceCreation.getTime() + 31528800000L - 50400000L);
                        setNotification30Days(addCarMake, addCarModel, "insurance", carInsuranceExpiry.getTime());
                        setNotification7Days(addCarMake, addCarModel, "insurance", carInsuranceExpiry.getTime());
                        setNotification1Day(addCarMake, addCarModel, "insurance", carInsuranceExpiry.getTime());
                        break;
                    case "1 Month":
                    default:
                        carInsuranceExpiry = new Date(carInsuranceCreation.getTime() + 2671200000L - 50400000L);
                        setNotification7Days(addCarMake, addCarModel, "insurance", carInsuranceExpiry.getTime());
                        setNotification1Day(addCarMake, addCarModel, "insurance", carInsuranceExpiry.getTime());

                }

                switch (spinnerInspection.getSelectedItem().toString()) {
                    case "1 Year":
                        carInspectionExpiry = new Date(carInspectionCreation.getTime() + 31528800000L - 50400000L);
                        setNotification30Days(addCarMake, addCarModel, "inspection", carInspectionExpiry.getTime());
                        setNotification7Days(addCarMake, addCarModel, "inspection", carInspectionExpiry.getTime());
                        setNotification1Day(addCarMake, addCarModel, "inspection", carInspectionExpiry.getTime());
                        break;
                    case "2 Years":
                        carInspectionExpiry = new Date(carInspectionCreation.getTime() + 63057600000L - 50400000L);
                        setNotification30Days(addCarMake, addCarModel, "inspection", carInspectionExpiry.getTime());
                        setNotification7Days(addCarMake, addCarModel, "inspection", carInspectionExpiry.getTime());
                        setNotification1Day(addCarMake, addCarModel, "inspection", carInspectionExpiry.getTime());
                        break;
                    case "3 Years":
                        carInspectionExpiry = new Date(carInspectionCreation.getTime() + 94672800000L - 50400000L);
                        setNotification30Days(addCarMake, addCarModel, "inspection", carInspectionExpiry.getTime());
                        setNotification7Days(addCarMake, addCarModel, "inspection", carInspectionExpiry.getTime());
                        setNotification1Day(addCarMake, addCarModel, "inspection", carInspectionExpiry.getTime());
                        break;
                    default:
                    case "6 Months":
                        carInspectionExpiry = new Date(carInspectionCreation.getTime() + 15631200000L - 50400000L);
                        setNotification30Days(addCarMake, addCarModel, "inspection", carInspectionExpiry.getTime());
                        setNotification7Days(addCarMake, addCarModel, "inspection", carInspectionExpiry.getTime());
                        setNotification1Day(addCarMake, addCarModel, "inspection", carInspectionExpiry.getTime());
                        break;

                }

                switch (spinnerRoadtax.getSelectedItem().toString()) {
                    case "1 Month":
                        carRoadtaxExpiry = new Date(carRoadTaxCreation.getTime() + 2671200000L - 50400000L);
                        setNotification7Days(addCarMake, addCarModel, "road tax", carRoadtaxExpiry.getTime());
                        setNotification1Day(addCarMake, addCarModel, "road tax", carRoadtaxExpiry.getTime());
                        break;
                    case "3 Months":
                        carRoadtaxExpiry = new Date(carRoadTaxCreation.getTime() + 7768800000L - 50400000L);
                        setNotification30Days(addCarMake, addCarModel, "road tax", carRoadtaxExpiry.getTime());
                        setNotification7Days(addCarMake, addCarModel, "road tax", carRoadtaxExpiry.getTime());
                        setNotification1Day(addCarMake, addCarModel, "road tax", carRoadtaxExpiry.getTime());
                        break;
                    case "12 Months":
                        carRoadtaxExpiry = new Date(carRoadTaxCreation.getTime() + 31528800000L - 50400000L);
                        setNotification30Days(addCarMake, addCarModel, "road tax", carRoadtaxExpiry.getTime());
                        setNotification7Days(addCarMake, addCarModel, "road tax", carRoadtaxExpiry.getTime());
                        setNotification1Day(addCarMake, addCarModel, "road tax", carRoadtaxExpiry.getTime());
                        break;
                    case "7 Days":
                    default:
                        carRoadtaxExpiry = new Date(carRoadTaxCreation.getTime() + 604800000L - 50400000L);
                        setNotification1Day(addCarMake, addCarModel, "road tax", carRoadtaxExpiry.getTime());
                }

                switch (spinnerService.getSelectedItem().toString()) {
                    case "1 Year":
                        carServiceExpiry = new Date(carServiceDateCreation.getTime() + 31528800000L - 50400000L);
                        setNotification30Days(addCarMake, addCarModel, "service", carServiceExpiry.getTime());
                        setNotification7Days(addCarMake, addCarModel, "service", carServiceExpiry.getTime());
                        setNotification1Day(addCarMake, addCarModel, "service", carServiceExpiry.getTime());
                        break;
                    case "2 Years":
                        carServiceExpiry = new Date(carServiceDateCreation.getTime() + 63072000000L - 50400000L);
                        setNotification30Days(addCarMake, addCarModel, "service", carServiceExpiry.getTime());
                        setNotification7Days(addCarMake, addCarModel, "service", carServiceExpiry.getTime());
                        setNotification1Day(addCarMake, addCarModel, "service", carServiceExpiry.getTime());
                        break;
                    case "6 Months":
                    default:
                        carServiceExpiry = new Date(carServiceDateCreation.getTime() + 15778476000L - 50400000L);
                        setNotification30Days(addCarMake, addCarModel, "service", carServiceExpiry.getTime());
                        setNotification7Days(addCarMake, addCarModel, "service", carServiceExpiry.getTime());
                        setNotification1Day(addCarMake, addCarModel, "service", carServiceExpiry.getTime());
                }


                int addCarServiceKm = 124365;

                Car car = new Car(addCarMake, addCarModel, addCarRegplate, addCarYear, carInsuranceExpiry, carInsuranceCreation, carInspectionExpiry, carInspectionCreation, carRoadtaxExpiry, carRoadTaxCreation, carServiceExpiry, carServiceDateCreation, addCarServiceKm, insurancePath, inspectionPath, roadTaxPath, servicePath);

                try {
                    CarDB database = CarDB.getInstance(getApplicationContext());
                    ExpensesDB edb = ExpensesDB.getInstance(getApplicationContext());
                    database.getCarDao().insert(car);
                    CurrentCarDB dbCar = CurrentCarDB.getInstance(getApplicationContext());
                    Cursor check = dbCar.getCurrentCarDao().checkIfEmpty();
                    if (check.getCount() == 0)
                        dbCar.getCurrentCarDao().insert(new CurrentCar(1));
                    Toast.makeText(getApplicationContext(), "Record successfully inserted!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddCar.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
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
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    carListSpinner.setAdapter(spinnerAdapter);

                    spinnerAdapter.addAll(carsList);
                    spinnerAdapter.notifyDataSetChanged();

                    CurrentCarDB currentCarDB = CurrentCarDB.getInstance(getApplicationContext());
                    check = currentCarDB.getCurrentCarDao().checkIfEmpty();

                    if (check.getCount() == 0)
                        currentCarDB.getCurrentCarDao().insert(new CurrentCar(1));

                    carListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String[] carSearch = carsList.get(i).split("\\s+");
                            int idUpdate = carDB.getCarDao().selectId(carSearch[0], carSearch[1], carTracking.get(i));
                            currentCarDB.getCurrentCarDao().updateCurrentCar(idUpdate);

                            ((TextView) carListSpinner.getSelectedView()).setTextColor(Color.WHITE);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                } catch (IOError e) {
                    e.printStackTrace();
                }

            }
        });

        addInsurancePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("TESTING", "It works");
                selectionID = 1;
                Dialog dialog = new Dialog(AddCar.this);
                dialog.setContentView(R.layout.alert_dialog_choice);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                gallery = dialog.findViewById(R.id.galleryImgButton);
                camera = dialog.findViewById(R.id.cameraImgButton);
                dialog.show();

                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        upload = 0;
                        //Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        activityResultLauncherGallery.launch(intent);
                        dialog.hide();

                    }
                });

                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        upload = 0;
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getPackageManager()) != null) {

                            String selection;

                            switch (selectionID) {
                                case 1:
                                    selection = "Insurance_";
                                    break;
                                case 2:
                                    selection = "Inspection_";
                                    break;
                                case 3:
                                    selection = "RoadTax_";
                                    break;
                                default:
                                    selection = "Service_";
                                    break;
                            }

                            String filename = selection + System.currentTimeMillis() + ".jpg";
                            File file = new File(getApplicationContext().getFilesDir(), filename);
                            Uri getUriFromFile = FileProvider.getUriForFile(AddCar.this, BuildConfig.APPLICATION_ID + ".provider", file);
                            String path = getApplicationContext().getFilesDir() + "/" + filename;

                            switch (selectionID) {
                                case 1:
                                    insurancePath = path;
                                    break;
                                case 2:
                                    inspectionPath = path;
                                    break;
                                case 3:
                                    roadTaxPath = path;
                                    break;
                                default:
                                    servicePath = path;
                                    break;
                            }

                            try {
                                outputStream = new FileOutputStream(file);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            if (file != null) {
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriFromFile);
                                activityResultLauncherCamera.launch(intent);

                            }

                            dialog.dismiss();

                        }

                    }
                });
            }
        });


        addInspectionPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionID = 2;
                //Log.d("TESTING", "It works");
                Dialog dialog = new Dialog(AddCar.this);
                dialog.setContentView(R.layout.alert_dialog_choice);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                gallery = dialog.findViewById(R.id.galleryImgButton);
                camera = dialog.findViewById(R.id.cameraImgButton);
                dialog.show();

                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        upload = 0;
                        //Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        activityResultLauncherGallery.launch(intent);
                        dialog.hide();

                    }
                });

                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        upload = 0;
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getPackageManager()) != null) {

                            String selection;

                            switch (selectionID) {
                                case 1:
                                    selection = "Insurance_";
                                    break;
                                case 2:
                                    selection = "Inspection_";
                                    break;
                                case 3:
                                    selection = "RoadTax_";
                                    break;
                                default:
                                    selection = "Service_";
                                    break;
                            }

                            String filename = selection + System.currentTimeMillis() + ".jpg";
                            File file = new File(getApplicationContext().getFilesDir(), filename);
                            Uri getUriFromFile = FileProvider.getUriForFile(AddCar.this, BuildConfig.APPLICATION_ID + ".provider", file);
                            String path = getApplicationContext().getFilesDir() + "/" + filename;

                            switch (selectionID) {
                                case 1:
                                    insurancePath = path;
                                    break;
                                case 2:
                                    inspectionPath = path;
                                    break;
                                case 3:
                                    roadTaxPath = path;
                                    break;
                                default:
                                    servicePath = path;
                                    break;
                            }

                            try {
                                outputStream = new FileOutputStream(file);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            if (file != null) {
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriFromFile);
                                activityResultLauncherCamera.launch(intent);

                            }

                            dialog.dismiss();

                        }

                    }
                });
            }
        });

        addRoadTaxPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionID = 3;
                //Log.d("TESTING", "It works");
                Dialog dialog = new Dialog(AddCar.this);
                dialog.setContentView(R.layout.alert_dialog_choice);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                gallery = dialog.findViewById(R.id.galleryImgButton);
                camera = dialog.findViewById(R.id.cameraImgButton);
                dialog.show();

                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        upload = 0;
                        //Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        activityResultLauncherGallery.launch(intent);
                        dialog.hide();

                    }
                });

                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        upload = 0;
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getPackageManager()) != null) {

                            String selection;

                            switch (selectionID) {
                                case 1:
                                    selection = "Insurance_";
                                    break;
                                case 2:
                                    selection = "Inspection_";
                                    break;
                                case 3:
                                    selection = "RoadTax_";
                                    break;
                                default:
                                    selection = "Service_";
                                    break;
                            }

                            String filename = selection + System.currentTimeMillis() + ".jpg";
                            File file = new File(getApplicationContext().getFilesDir(), filename);
                            Uri getUriFromFile = FileProvider.getUriForFile(AddCar.this, BuildConfig.APPLICATION_ID + ".provider", file);
                            String path = getApplicationContext().getFilesDir() + "/" + filename;

                            switch (selectionID) {
                                case 1:
                                    insurancePath = path;
                                    break;
                                case 2:
                                    inspectionPath = path;
                                    break;
                                case 3:
                                    roadTaxPath = path;
                                    break;
                                default:
                                    servicePath = path;
                                    break;
                            }

                            try {
                                outputStream = new FileOutputStream(file);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            if (file != null) {
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriFromFile);
                                activityResultLauncherCamera.launch(intent);

                            }

                            dialog.dismiss();

                        }

                    }
                });
            }
        });

        addServicePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionID = 4;
                //Log.d("TESTING", "It works");
                Dialog dialog = new Dialog(AddCar.this);
                dialog.setContentView(R.layout.alert_dialog_choice);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                gallery = dialog.findViewById(R.id.galleryImgButton);
                camera = dialog.findViewById(R.id.cameraImgButton);
                dialog.show();

                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        upload = 0;
                        //Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        activityResultLauncherGallery.launch(intent);
                        dialog.dismiss();
                    }
                });

                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        upload = 0;
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getPackageManager()) != null) {

                            String selection;

                            switch (selectionID) {
                                case 1:
                                    selection = "Insurance_";
                                    break;
                                case 2:
                                    selection = "Inspection_";
                                    break;
                                case 3:
                                    selection = "RoadTax_";
                                    break;
                                default:
                                    selection = "Service_";
                                    break;
                            }

                            String filename = selection + System.currentTimeMillis() + ".jpg";
                            File file = new File(getApplicationContext().getFilesDir(), filename);
                            Uri getUriFromFile = FileProvider.getUriForFile(AddCar.this, BuildConfig.APPLICATION_ID + ".provider", file);
                            String path = getApplicationContext().getFilesDir() + "/" + filename;

                            switch (selectionID) {
                                case 1:
                                    insurancePath = path;
                                    break;
                                case 2:
                                    inspectionPath = path;
                                    break;
                                case 3:
                                    roadTaxPath = path;
                                    break;
                                default:
                                    servicePath = path;
                                    break;
                            }

                            try {
                                outputStream = new FileOutputStream(file);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            if (file != null) {
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriFromFile);
                                activityResultLauncherCamera.launch(intent);

                            }

                            dialog.dismiss();

                        }

                    }
                });
            }
        });

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "AutomotiveManagementNotificationsChannel";
            String description = "Channel for car reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("reminderAlarm", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setNotification30Days(String make, String model, String type, Long date) {
        Intent intent = new Intent(AddCar.this, ReminderBroadcast.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int requestCode = 0;
        intent.putExtra("make", make);
        intent.putExtra("model", model);
        intent.putExtra("type", type);
        intent.putExtra("duration", "30 days");
        sendBroadcast(intent);
        switch (type) {
            case "insurance":
                requestCode = 1;
                break;
            case "inspection":
                requestCode = 2;
                break;
            case "road tax":
                requestCode = 3;
                break;
            case "service":
                requestCode = 4;
                break;
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddCar.this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, date - 2592000000L, pendingIntent);

        SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yyyy");
        String dateLog = sdf.format(date - 2592000000L);

        Log.d("DEBUGGING", "Alarm set for "+type+" 30 days "+dateLog);
    }

    private void setNotification7Days(String make, String model, String type, Long date) {
        Intent intent = new Intent(AddCar.this, ReminderBroadcast.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int requestCode = 0;
        intent.putExtra("make", make);
        intent.putExtra("model", model);
        intent.putExtra("type", type);
        intent.putExtra("duration", "7 days");
        sendBroadcast(intent);
        switch (type) {
            case "insurance":
                requestCode = 1;
                break;
            case "inspection":
                requestCode = 2;
                break;
            case "road tax":
                requestCode = 3;
                break;
            case "service":
                requestCode = 4;
                break;
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddCar.this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, date - 604800000L, pendingIntent);

        SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yyyy");
        String dateLog = sdf.format(date - 604800000L);

        Log.d("DEBUGGING", "Alarm set for "+type+" 7 day "+dateLog);
    }

    private void setNotification1Day(String make, String model, String type, Long date) {
        Intent intent = new Intent(AddCar.this, ReminderBroadcast.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int requestCode = 0;

        intent.putExtra("make", make);
        intent.putExtra("model", model);
        intent.putExtra("type", type);
        intent.putExtra("duration", "1 day");
        sendBroadcast(intent);
        switch (type) {
            case "insurance":
                requestCode = 1;
                break;
            case "inspection":
                requestCode = 2;
                break;
            case "road tax":
                requestCode = 3;
                break;
            case "service":
                requestCode = 4;
                break;
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddCar.this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis(), pendingIntent);
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yyyy");
        String dateLog = sdf.format(date - 86400000L);

        Log.d("DEBUGGING", "Alarm set for "+type+" 1 day "+dateLog);
    }


    @Override
    protected void onResume() {
        super.onResume();
        switch (selectionID) {
            case 1:
                if (upload == 1)
                    insurancePhotoButtonText.setTextColor(Color.GREEN);
                else
                    insurancePhotoButtonText.setTextColor(Color.RED);
                break;
            case 2:
                if (upload == 1)
                    inspectionPhotoButtonText.setTextColor(Color.GREEN);
                else
                    inspectionPhotoButtonText.setTextColor(Color.RED);
                break;
            case 3:
                if (upload == 1)
                    roadTaxPhotoButtonText.setTextColor(Color.GREEN);
                else
                    roadTaxPhotoButtonText.setTextColor(Color.RED);
                break;
            case 4:
                if (upload == 1)
                    servicePhotoButtonText.setTextColor(Color.GREEN);
                else
                    servicePhotoButtonText.setTextColor(Color.RED);
                break;
        }
    }
}