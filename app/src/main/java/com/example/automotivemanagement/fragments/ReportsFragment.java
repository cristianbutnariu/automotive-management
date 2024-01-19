package com.example.automotivemanagement.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.automotivemanagement.R;
import com.example.automotivemanagement.activities.AddExpense;
import com.example.automotivemanagement.reports.FuelReport;
import com.example.automotivemanagement.reports.OtherReport;
import com.example.automotivemanagement.reports.ServiceReport;
import com.example.automotivemanagement.reports.TaxesReport;

public class ReportsFragment extends Fragment {

    public static int idSelected;
    private CardView fuel, service, taxes, other, addExpense;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reports, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fuel = getView().findViewById(R.id.fuelReports);
        service = getView().findViewById(R.id.serviceReports);
        taxes = getView().findViewById(R.id.taxesReports);
        other = getView().findViewById(R.id.otherReports);
        addExpense = getView().findViewById(R.id.addExpense);

        fuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FuelReport.class);
                startActivity(intent);

            }
        });

        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ServiceReport.class);
                idSelected = 2;
                startActivity(intent);
            }
        });

        taxes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TaxesReport.class);
                idSelected = 3;
                startActivity(intent);
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OtherReport.class);
                idSelected = 4;
                startActivity(intent);
            }
        });

        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddExpense.class);
                startActivity(intent);
            }
        });
    }
}