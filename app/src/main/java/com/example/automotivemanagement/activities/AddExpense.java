package com.example.automotivemanagement.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.automotivemanagement.BuildConfig;
import com.example.automotivemanagement.R;
import com.example.automotivemanagement.currentCar.CurrentCarDB;
import com.example.automotivemanagement.expenses.Expenses;
import com.example.automotivemanagement.expenses.ExpensesDB;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddExpense extends AppCompatActivity {

    private RadioGroup options;
    private RadioButton fuel, service, taxes, other;
    private EditText price, mileage, details;
    private EditText date, time;
    private DatePickerDialog.OnDateSetListener dateListener;
    private TimePickerDialog.OnTimeSetListener timeListener;
    private Button addExpense;
    private ImageButton gallery, camera;
    private OutputStream outputStream;
    private String expensePath = "";
    private Uri imageUri;
    private View layout;
    private TextView uploadExpensePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        options = findViewById(R.id.expensesOptionsGroup);

        fuel = findViewById(R.id.fuelBtn);
        service = findViewById(R.id.serviceBtn);
        taxes = findViewById(R.id.taxesBtn);
        other = findViewById(R.id.otherBtn);

        price = findViewById(R.id.priceValue);
        mileage = findViewById(R.id.mileageValue);
        details = findViewById(R.id.detailsValue);

        date = findViewById(R.id.dateText);
        time = findViewById(R.id.timeText);

        addExpense = findViewById(R.id.addExpenseButton);

        date.setText(new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date()));
        time.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(Calendar.getInstance().getTime()));

        layout = findViewById(R.id.checkRoadTaxPhotoButton);
        uploadExpensePhoto = layout.findViewById(R.id.textView);
        uploadExpensePhoto.setText("Upload a photo");


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddExpense.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String pickedDate = day + "-" + month + "-" + year;
                Date textBoxDate = new Date();
                SimpleDateFormat input = new SimpleDateFormat("dd-M-yyyy");
                SimpleDateFormat output = new SimpleDateFormat("MMM dd, yyyy");
                try {
                    textBoxDate = input.parse(pickedDate);
                    date.setText(output.format(textBoxDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar time = Calendar.getInstance();
                int hours = time.get(Calendar.HOUR_OF_DAY);
                int minutes = time.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(AddExpense.this, timeListener, hours, minutes, true);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        timeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                String addTime = hours + ":" + minutes;

                time.setText(addTime);
            }
        };

        addExpense.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Date addDate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                String typeValue = "";
                int selected = options.getCheckedRadioButtonId();

                switch (selected) {
                    case R.id.fuelBtn:
                        typeValue = "fuel";
                        break;
                    case R.id.serviceBtn:
                        typeValue = "service";
                        break;
                    case R.id.taxesBtn:
                        typeValue = "taxes";
                        break;
                    case R.id.otherBtn:
                        typeValue = "other";
                        break;
                }
                float priceValue = Float.valueOf(price.getText().toString());
                int mileageValue = Integer.valueOf(mileage.getText().toString());
                String detailsValue = details.getText().toString();

                String timeValue = time.getText().toString();

                try {
                    addDate = sdf.parse(date.getText().toString());
                    Log.e("DEBUG", addDate.toString());

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                CurrentCarDB dbCar = CurrentCarDB.getInstance(getApplicationContext());
                int id = dbCar.getCurrentCarDao().getCurrentCar();
                Expenses expense = new Expenses(typeValue, priceValue, mileageValue, detailsValue, addDate, timeValue, id, expensePath);

                try {
                    ExpensesDB database = ExpensesDB.getInstance(getApplicationContext());
                    database.getExpensesDao().insert(expense);
                    Toast.makeText(getApplicationContext(), "Record successfully inserted!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddExpense.this, MainActivity.class);
                    startActivity(intent);
                } catch (IOError e) {
                    e.printStackTrace();
                }

            }
        });
        ActivityResultLauncher<Intent> activityResultLauncherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                try {
                    Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(expensePath));
                } catch (Exception e) {

                }
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncherGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri photoUri = result.getData().getData();
                String selection;
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

                int selected = options.getCheckedRadioButtonId();

                switch (selected) {
                    case R.id.serviceBtn:
                        selection = "Service";
                        break;
                    case R.id.taxesBtn:
                        selection = "Taxes";
                        break;
                    case R.id.otherBtn:
                        selection = "Other";
                        break;
                    default:
                        selection = "Fuel";
                        break;
                }

                String filename = selection + System.currentTimeMillis() + ".jpg";
                File imageFile = new File(getApplicationContext().getFilesDir(), filename);
                expensePath = getApplicationContext().getFilesDir() + "/" + filename;

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
            }

        });


        View addPhotoView = findViewById(R.id.checkRoadTaxPhotoButton);
        addPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(AddExpense.this);
                dialog.setContentView(R.layout.alert_dialog_choice);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                gallery = dialog.findViewById(R.id.galleryImgButton);
                camera = dialog.findViewById(R.id.cameraImgButton);
                dialog.show();

                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        activityResultLauncherGallery.launch(intent);
                        //dialog.hide();
                        dialog.dismiss();


                    }
                });

                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getPackageManager()) != null) {

                            String selection;
                            int selected = options.getCheckedRadioButtonId();

                            switch (selected) {
                                case R.id.serviceBtn:
                                    selection = "Service";
                                    break;
                                case R.id.taxesBtn:
                                    selection = "Taxes";
                                    break;
                                case R.id.otherBtn:
                                    selection = "Other";
                                    break;
                                default:
                                    selection = "Fuel";
                                    break;
                            }

                            String filename = selection + System.currentTimeMillis() + ".jpg";
                            File file = new File(getApplicationContext().getFilesDir(), filename);
                            Uri getUriFromFile = FileProvider.getUriForFile(AddExpense.this, BuildConfig.APPLICATION_ID + ".provider", file);
                            expensePath = getApplicationContext().getFilesDir() + "/" + filename;

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
}
