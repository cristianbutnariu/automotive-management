package com.example.automotivemanagement.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.automotivemanagement.BuildConfig;
import com.example.automotivemanagement.R;
import com.example.automotivemanagement.currentCar.CurrentCarDB;
import com.example.automotivemanagement.vehicle.CarDB;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Inspection extends AppCompatActivity {

    private EditText inspectionDate, inspectionNewDate;
    private TextView newInspectionDate;
    private Spinner newInspectionPeriod;
    private DatePickerDialog.OnDateSetListener inspectionListener;
    private Button updateInspection, inspectionPhoto;
    private ImageButton inspectionDuration, inspectionCheck, inspectionUpload, gallery, camera;
    private FileOutputStream outputStream;
    private String inspectionPath = "";
    private View layout, layout2;
    private TextView checkInspectionPhoto, uploadInspectionPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection);

        inspectionDate = findViewById(R.id.inspectionExpiryDate);
        inspectionNewDate = findViewById(R.id.inspectionNewDate);
        CurrentCarDB cdb = CurrentCarDB.getInstance(getApplicationContext());
        long inspectionExpiry = CarDB.getInstance(getApplicationContext()).getCarDao().inspectionDate(cdb.getCurrentCarDao().getCurrentCar());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
        String dateString = formatter.format(new Date(inspectionExpiry));
        inspectionDate.setText(dateString);

        newInspectionPeriod = findViewById(R.id.newInspectionPeriod);
        ArrayAdapter<String> inspectionPeriod = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.inspectionDuration));
        newInspectionPeriod.setAdapter(inspectionPeriod);

        layout=findViewById(R.id.checkInspectionPhotoButton);
        checkInspectionPhoto=layout.findViewById(R.id.textView);
        checkInspectionPhoto.setText("Check current inspection");

        layout2=findViewById(R.id.uploadInspectionPhotoButton);
        uploadInspectionPhoto=layout2.findViewById(R.id.textView);
        uploadInspectionPhoto.setText("Upload new inspection");

        inspectionNewDate = (EditText) findViewById(R.id.inspectionNewDate);
        inspectionNewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Inspection.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, inspectionListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        inspectionListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "-" + month + "-" + year;
                SimpleDateFormat oldFormat = new SimpleDateFormat("dd-M-yyyy");
                SimpleDateFormat newFormat = new SimpleDateFormat("dd-MMMM-yyyy");
                Date originalDate = null;
                try {
                    originalDate = oldFormat.parse(date);
                    inspectionNewDate.setText(newFormat.format(originalDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        updateInspection = findViewById(R.id.updateInspectionButton);
        inspectionDuration = findViewById(R.id.inspectionDurationSelection);
        inspectionDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newInspectionPeriod.performClick();
            }
        });
        updateInspection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy");
                Date newCarInspectionCreation = null, newCarInspectionExpiry = null;
                try {
                    newCarInspectionCreation = sdf.parse(inspectionNewDate.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                switch (newInspectionPeriod.getSelectedItem().toString()) {
                    case "2 Months":
                        newCarInspectionExpiry = new Date(newCarInspectionCreation.getTime() + 5090400000L - 50400000L);
                        break;
                    case "3 Months":
                        newCarInspectionExpiry = new Date(newCarInspectionCreation.getTime() + 7768800000L - 50400000L);
                        break;
                    case "6 Months":
                        newCarInspectionExpiry = new Date(newCarInspectionCreation.getTime() + 15631200000L - 50400000L);
                        break;
                    case "9 Months":
                        newCarInspectionExpiry = new Date(newCarInspectionCreation.getTime() + 23580000000L - 50400000L);
                        break;
                    case "11 Months":
                        newCarInspectionExpiry = new Date(newCarInspectionCreation.getTime() + 28850400000L - 50400000L);
                        break;
                    case "12 Months":
                        newCarInspectionExpiry = new Date(newCarInspectionCreation.getTime() + 31536000000L /*31528800000L - 50400000L*/);
                        break;
                    case "1 Month":
                    default:
                        newCarInspectionExpiry = new Date(newCarInspectionCreation.getTime() + 2671200000L - 50400000L);
                }

                CarDB database = CarDB.getInstance(getApplicationContext());
                CurrentCarDB cdb = CurrentCarDB.getInstance(getApplicationContext());
                int currentId = cdb.getCurrentCarDao().getCurrentCar();
                database.getCarDao().updateInspectionCreation(newCarInspectionCreation, currentId);
                database.getCarDao().updateInspectionExpiry(newCarInspectionExpiry, currentId);
                database.getCarDao().updateInspectionPhotoPath(inspectionPath, currentId);
                long inspectionExpiry = CarDB.getInstance(getApplicationContext()).getCarDao().inspectionDate(currentId);
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
                String dateString = formatter.format(new Date(inspectionExpiry));
                inspectionDate.setText(dateString);
            }
        });

        View checkInspectionPhotoView = findViewById(R.id.checkInspectionPhotoButton);
        checkInspectionPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarDB database = CarDB.getInstance(getApplicationContext());
                CurrentCarDB cdb = CurrentCarDB.getInstance(getApplicationContext());
                int currentId = cdb.getCurrentCarDao().getCurrentCar();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                String path = database.getCarDao().getInspectionPicturePath(currentId);
                if (path.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "There is no photo uploaded for this car", Toast.LENGTH_LONG).show();
                } else {
                    File file = new File(path);
                    Uri dirUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);
                    intent.setData(dirUri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                }
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                try {
                    Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(inspectionPath));
                } catch (Exception e) {

                }
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncherGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri photoUri = result.getData().getData();

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


                String filename = "Inspection" + System.currentTimeMillis() + ".jpg";
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
                inspectionPath = path;
            }

        });

        View uploadInspectionPhotoView = findViewById(R.id.uploadInspectionPhotoButton);
        uploadInspectionPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(Inspection.this);
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
                        dialog.dismiss();

                    }
                });

                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getPackageManager()) != null) {

                            String filename = "Inspection_" + System.currentTimeMillis() + ".jpg";
                            File file = new File(getApplicationContext().getFilesDir(), filename);
                            Uri getUriFromFile = FileProvider.getUriForFile(Inspection.this, BuildConfig.APPLICATION_ID + ".provider", file);
                            inspectionPath = getApplicationContext().getFilesDir() + "/" + filename;

                            try {
                                outputStream = new FileOutputStream(file);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            // Continue only if the File was successfully created
                            if (file != null) {
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriFromFile);
                                activityResultLauncherCamera.launch(intent);
                                Log.d("DEBUG", "File created");
                            }
                        }
                        Log.d("DEBUG", "Is null");
                        dialog.dismiss();

                    }
                });

                    }
                });
            }


    }
