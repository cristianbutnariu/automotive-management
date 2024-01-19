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

public class Service extends AppCompatActivity {

    private EditText serviceDate, serviceNewDate;
    private TextView newServiceDate;
    private Spinner newServicePeriod;
    private DatePickerDialog.OnDateSetListener serviceListener;
    private Button updateService, servicePhoto;
    private ImageButton serviceDuration, serviceCheck, serviceUpload, gallery, camera;
    private FileOutputStream outputStream;
    private String servicePath = "";
    private View layout, layout2;
    private TextView checkServicePhoto, uploadServicePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        serviceDate = findViewById(R.id.serviceExpiryDate);
        serviceNewDate = findViewById(R.id.serviceNewDate);
        CurrentCarDB cdb = CurrentCarDB.getInstance(getApplicationContext());
        long serviceExpiry = CarDB.getInstance(getApplicationContext()).getCarDao().serviceDate(cdb.getCurrentCarDao().getCurrentCar());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
        String dateString = formatter.format(new Date(serviceExpiry));
        serviceDate.setText(dateString);

        layout=findViewById(R.id.checkServicePhotoButton);
        checkServicePhoto=layout.findViewById(R.id.textView);
        checkServicePhoto.setText("Check current service");

        layout2=findViewById(R.id.uploadServicePhotoButton);
        uploadServicePhoto=layout2.findViewById(R.id.textView);
        uploadServicePhoto.setText("Upload new service");

        newServicePeriod = findViewById(R.id.newServicePeriod);
        ArrayAdapter<String> servicePeriod = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.serviceDuration));
        newServicePeriod.setAdapter(servicePeriod);

        serviceNewDate = (EditText) findViewById(R.id.serviceNewDate);
        serviceNewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Service.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, serviceListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        serviceListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "-" + month + "-" + year;
                SimpleDateFormat oldFormat = new SimpleDateFormat("dd-M-yyyy");
                SimpleDateFormat newFormat = new SimpleDateFormat("dd-MMMM-yyyy");
                Date originalDate = null;
                try {
                    originalDate = oldFormat.parse(date);
                    serviceNewDate.setText(newFormat.format(originalDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        updateService = findViewById(R.id.updateServiceButton);
        serviceDuration = findViewById(R.id.serviceDurationSelection);
        serviceDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newServicePeriod.performClick();
            }
        });
        updateService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy");
                Date newCarServiceCreation = null, newCarServiceExpiry = null;
                try {
                    newCarServiceCreation = sdf.parse(serviceNewDate.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                switch (newServicePeriod.getSelectedItem().toString()) {
                    case "2 Months":
                        newCarServiceExpiry = new Date(newCarServiceCreation.getTime() + 5090400000L - 50400000L);
                        break;
                    case "3 Months":
                        newCarServiceExpiry = new Date(newCarServiceCreation.getTime() + 7768800000L - 50400000L);
                        break;
                    case "6 Months":
                        newCarServiceExpiry = new Date(newCarServiceCreation.getTime() + 15631200000L - 50400000L);
                        break;
                    case "9 Months":
                        newCarServiceExpiry = new Date(newCarServiceCreation.getTime() + 23580000000L - 50400000L);
                        break;
                    case "11 Months":
                        newCarServiceExpiry = new Date(newCarServiceCreation.getTime() + 28850400000L - 50400000L);
                        break;
                    case "1 Year":
                        newCarServiceExpiry = new Date(newCarServiceCreation.getTime() + 31536000000L /*31528800000L - 50400000L*/);
                        break;
                    case "1 Month":
                    default:
                        newCarServiceExpiry = new Date(newCarServiceCreation.getTime() + 2671200000L - 50400000L);
                }

                CarDB database = CarDB.getInstance(getApplicationContext());
                CurrentCarDB cdb = CurrentCarDB.getInstance(getApplicationContext());
                int currentId = cdb.getCurrentCarDao().getCurrentCar();
                database.getCarDao().updateServiceCreation(newCarServiceCreation, currentId);
                database.getCarDao().updateServiceExpiry(newCarServiceExpiry, currentId);
                database.getCarDao().updateServicePhotoPath(servicePath, currentId);
                long serviceExpiry = CarDB.getInstance(getApplicationContext()).getCarDao().serviceDate(currentId);
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
                String dateString = formatter.format(new Date(serviceExpiry));
                serviceDate.setText(dateString);
            }
        });

        View checkServicePhotoView = findViewById(R.id.checkServicePhotoButton);
        checkServicePhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarDB database = CarDB.getInstance(getApplicationContext());
                CurrentCarDB cdb = CurrentCarDB.getInstance(getApplicationContext());
                int currentId = cdb.getCurrentCarDao().getCurrentCar();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                String path = database.getCarDao().getServicePicturePath(currentId);
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
                    Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(servicePath));
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


                String filename = "Service" + System.currentTimeMillis() + ".jpg";
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
                servicePath = path;
            }

        });

        View uploadServicePhotoView = findViewById(R.id.uploadServicePhotoButton);
        uploadServicePhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(Service.this);
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

                            String filename = "Service_" + System.currentTimeMillis() + ".jpg";
                            File file = new File(getApplicationContext().getFilesDir(), filename);
                            Uri getUriFromFile = FileProvider.getUriForFile(Service.this, BuildConfig.APPLICATION_ID + ".provider", file);
                            servicePath = getApplicationContext().getFilesDir() + "/" + filename;

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
                            } else {
                                Log.d("DEBUG", "File not created");
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