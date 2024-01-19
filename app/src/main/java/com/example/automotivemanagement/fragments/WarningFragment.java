package com.example.automotivemanagement.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.automotivemanagement.R;

public class WarningFragment extends Fragment {

    private CardView roadtax, fuel, insurance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_warning,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        roadtax=getView().findViewById(R.id.rovinieta);
        fuel=getView().findViewById(R.id.fuelprices);
        insurance=getView().findViewById(R.id.rca);

        roadtax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink("https://www.erovinieta.ro/vignettes-portal-web/#/login");
            }
        });

        fuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink("https://www.peco-online.ro/");
            }
        });

        insurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink("https://www.rcalatineacasa.ro/cel-mai-ieftin-rca-online.php");
            }
        });

    }

    private void openLink(String str){
        Uri url = Uri.parse(str);
        startActivity(new Intent(Intent.ACTION_VIEW, url));
    }
}