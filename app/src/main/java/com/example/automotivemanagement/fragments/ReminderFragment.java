package com.example.automotivemanagement.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.automotivemanagement.R;
import com.example.automotivemanagement.activities.Inspection;
import com.example.automotivemanagement.activities.Insurance;
import com.example.automotivemanagement.activities.RoadTax;
import com.example.automotivemanagement.activities.Service;
import com.example.automotivemanagement.currentCar.CurrentCarDB;
import com.example.automotivemanagement.vehicle.CarDB;
import com.example.automotivemanagement.vehicle.CarDao;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class ReminderFragment extends Fragment {

    ProgressBar insuranceBar, inspectionBar, roadTaxBar, serviceBar;
    TextView insuranceText, inspectionText, roadTaxText, serviceText;
    View layout;
    CarDao update = null;
    int counter = 0;
    CurrentCarDB currentCarDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reminder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);
        insuranceUpdate();
        inspectionUpdate();
        roadTaxUpdate();
        serviceUpdate();

        View insuranceLayout = view.findViewById(R.id.insuranceLayout);
        insuranceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Insurance.class);
                startActivity(intent);
            }
        });

        View inspectionLayout = view.findViewById(R.id.inspectionLayout);
        inspectionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Inspection.class);
                startActivity(intent);
            }
        });

        View roadTaxLayout = view.findViewById(R.id.roadTaxLayout);
        roadTaxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RoadTax.class);
                startActivity(intent);
            }
        });

        View serviceLayout = view.findViewById(R.id.serviceDatelayout);
        serviceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Service.class);
                startActivity(intent);
            }
        });
    }

    public void insuranceUpdate() {
        currentCarDB = CurrentCarDB.getInstance(getContext());
        int currentCarId = currentCarDB.getCurrentCarDao().getCurrentCar();
        layout = getView().findViewById(R.id.insuranceLayout);
        insuranceText = layout.findViewById(R.id.textView);
        insuranceBar = layout.findViewById(R.id.progressBar3);

        insuranceText.setText("Insurance");

        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                long insuranceCreation = CarDB.getInstance(getContext()).getCarDao().insuranceCreationDate(currentCarId);
                long insuranceExpiry = CarDB.getInstance(getContext()).getCarDao().insuranceDate(currentCarId);

                long startDate = Calendar.getInstance().getTimeInMillis() - insuranceCreation; //current time - insurance creation date
                long endDate = insuranceExpiry - insuranceCreation;//insurance expiry date - insurance creation date

                float progress = (float) startDate / endDate * 100;

                insuranceBar.setProgress((int) progress);

                /*if (progress <= 30)
                    insuranceBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                else if (progress > 30 && progress < 70)
                    insuranceBar.setProgressTintList(ColorStateList.valueOf(Color.BLUE));
                else if (progress >= 70 && progress<100)
                    insuranceBar.setProgressTintList(ColorStateList.valueOf(Color.RED));*/
                if (progress == 100) {
                    //insuranceBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
                    timer.cancel();
                }


            }
        };
        timer.schedule(timerTask, 0, 100);

    }

    public void inspectionUpdate() {
        currentCarDB = CurrentCarDB.getInstance(getContext());
        int currentCarId = currentCarDB.getCurrentCarDao().getCurrentCar();
        layout = getView().findViewById(R.id.inspectionLayout);
        inspectionText = layout.findViewById(R.id.textView);
        inspectionBar = layout.findViewById(R.id.progressBar3);

        inspectionText.setText("Inspection");

        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                long inspectionCreation = CarDB.getInstance(getContext()).getCarDao().inspectionCreationDate(currentCarId);
                long inspectionExpiry = CarDB.getInstance(getContext()).getCarDao().inspectionDate(currentCarId);

                long startDate = Calendar.getInstance().getTimeInMillis() - inspectionCreation; //current time - insurance creation date
                long endDate = inspectionExpiry - inspectionCreation;//insurance expiry date - insurance creation date

                float progress = (float) startDate / endDate * 100;

                inspectionBar.setProgress((int) progress);

                /*if (progress <= 30)
                    inspectionBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                else if (progress > 30 && progress < 70)
                    inspectionBar.setProgressTintList(ColorStateList.valueOf(Color.BLUE));
                else if (progress >= 70 && progress<100)
                    inspectionBar.setProgressTintList(ColorStateList.valueOf(Color.RED));*/
                if (progress == 100) {
                    //inspectionBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
                    timer.cancel();
                }


            }
        };
        timer.schedule(timerTask, 0, 100);
    }

    public void roadTaxUpdate() {
        currentCarDB = CurrentCarDB.getInstance(getContext());
        int currentCarId = currentCarDB.getCurrentCarDao().getCurrentCar();
        layout = getView().findViewById(R.id.roadTaxLayout);
        roadTaxText = layout.findViewById(R.id.textView);
        roadTaxBar = layout.findViewById(R.id.progressBar3);

        roadTaxText.setText("Road Tax");

        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                long roadTaxCreation = CarDB.getInstance(getContext()).getCarDao().roadTaxCreationDate(currentCarId);
                long roadTaxExpiry = CarDB.getInstance(getContext()).getCarDao().roadTaxDate(currentCarId);

                long startDate = Calendar.getInstance().getTimeInMillis() - roadTaxCreation; //current time - insurance creation date
                long endDate = roadTaxExpiry - roadTaxCreation;//insurance expiry date - insurance creation date

                float progress = (float) startDate / endDate * 100;

                roadTaxBar.setProgress((int) progress);

                /*if (progress <= 30)
                    roadTaxBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                else if (progress > 30 && progress < 70)
                    roadTaxBar.setProgressTintList(ColorStateList.valueOf(Color.BLUE));
                else if (progress >= 70 && progress<100)
                    roadTaxBar.setProgressTintList(ColorStateList.valueOf(Color.RED));*/
                if (progress == 100) {
                    //roadTaxBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
                    timer.cancel();
                }


            }
        };
        timer.schedule(timerTask, 0, 100);
    }

    public void serviceUpdate() {
        currentCarDB = CurrentCarDB.getInstance(getContext());
        int currentCarId = currentCarDB.getCurrentCarDao().getCurrentCar();
        layout = getView().findViewById(R.id.serviceDatelayout);
        serviceText = layout.findViewById(R.id.textView);
        serviceBar = layout.findViewById(R.id.progressBar3);

        serviceText.setText("Service");

        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                long serviceCreation = CarDB.getInstance(getContext()).getCarDao().serviceCreationDate(currentCarId);
                long serviceExpiry = CarDB.getInstance(getContext()).getCarDao().serviceDate(currentCarId);

                long startDate = Calendar.getInstance().getTimeInMillis() - serviceCreation; //current time - insurance creation date
                long endDate = serviceExpiry - serviceCreation;//insurance expiry date - insurance creation date

                float progress = (float) startDate / endDate * 100;

                serviceBar.setProgress((int) progress);

                /*if (progress <= 30)
                    serviceBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                else if (progress > 30 && progress < 70)
                    serviceBar.setProgressTintList(ColorStateList.valueOf(Color.BLUE));
                else if (progress >= 70 && progress<100)
                    serviceBar.setProgressTintList(ColorStateList.valueOf(Color.RED));*/
                if (progress == 100) {
                    //serviceBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
                    timer.cancel();
                }


            }
        };
        timer.schedule(timerTask, 0, 100);
    }
}