package com.example.automotivemanagement.fragments;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.automotivemanagement.R;
import com.example.automotivemanagement.adapters.ListViewAdapter;
import com.example.automotivemanagement.currentCar.CurrentCarDB;
import com.example.automotivemanagement.shop.Dealership;
import com.example.automotivemanagement.shop.DealershipDB;
import com.example.automotivemanagement.vehicle.CarDB;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class ServiceFragment extends Fragment implements OnMapReadyCallback {


    private static final double EARTH_RADIUS = 6371;
    private static final int REQUEST_PERMISSIONS_CODE = 100;
    MapView mMapView;
    View view;
    ListView lvDealers;
    List<Dealership> dealersList;
    double latitude;
    double longitude;
    private FusedLocationProviderClient flpc;
    private ListViewAdapter lva;
    private GoogleMap gMap;
    private CameraPosition camera;

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;
        return distance;
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_service, container, false);
        super.onCreate(savedInstanceState);
        requestPermissions(new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_CODE);

        LocationRequest locationRequest = new LocationRequest.Builder(PRIORITY_HIGH_ACCURACY, 100)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(1000)
                .setMaxUpdateDelayMillis(100)
                .build();

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null)
                    return;
            }
        };

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return view;
        }
        LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates(locationRequest, locationCallback, null);

        Log.e("VIEW", "OnCreateView");


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("VIEW", "OnViewCreated");
        mMapView = view.findViewById(R.id.map_view);
        lvDealers = view.findViewById(R.id.listViewDealers);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

        lvDealers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dealersList.get(i).getPhoneNumber()));
                startActivity(intent);            }
        });


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        Log.e("VIEW", "OnMapReady");

        CurrentCarDB ccdb = CurrentCarDB.getInstance(getContext());
        int currentId = ccdb.getCurrentCarDao().getCurrentCar();

        CarDB carDB = CarDB.getInstance(getContext());
        String currentCarMake = carDB.getCarDao().getCarMake(currentId);
        Cursor cursor = carDB.getCarDao().getAll();
        if (cursor.getCount() == 0)
            Log.e("DATABASE", "database empty");
        else {
            gMap = googleMap;
            gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            gMap.setMyLocationEnabled(true);

            DealershipDB db = DealershipDB.getInstance(getContext());
            dealersList = db.getDealershipDao().loadDealerships(currentCarMake);
            lvDealers = getView().findViewById(R.id.listViewDealers);

        }

        lvDealers.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dealersList.get(i).getPhoneNumber()));
            startActivity(intent);
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e("VIEW", "OnResume");
        CurrentCarDB ccdb = CurrentCarDB.getInstance(getContext());
        int currentId = ccdb.getCurrentCarDao().getCurrentCar();

        CarDB carDB = CarDB.getInstance(getContext());
        String currentCarMake = carDB.getCarDao().getCarMake(currentId);
        Cursor cursor = carDB.getCarDao().getAll();
        DealershipDB db = DealershipDB.getInstance(getContext());
        dealersList = db.getDealershipDao().loadDealerships(currentCarMake);
        lvDealers = getView().findViewById(R.id.listViewDealers);
        lvDealers.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            flpc = LocationServices.getFusedLocationProviderClient(getContext());
            flpc.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Log.e("MSG", "GPS deschis");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();


                        camera = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(10).bearing(0)
                                .tilt(0).build();

                        gMap.moveCamera(CameraUpdateFactory.newCameraPosition(camera));
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        gMap.setMyLocationEnabled(true);
                        if (location != null && location.getLatitude() != 0 && location.getLongitude() != 0) {
                            List<Dealership> filteredDealersList = new ArrayList<>();
                            for (int i = 0; i < dealersList.size(); i++) {
                                if (calculateDistance(dealersList.get(i).getLatitude(), dealersList.get(i).getLongitude(), latitude, longitude) <= 40) {
                                    filteredDealersList.add(dealersList.get(i));
                                    createMarker(dealersList.get(i).getLatitude(), dealersList.get(i).getLongitude(),
                                            dealersList.get(i).getMake() + " " + dealersList.get(i).getName(),
                                            dealersList.get(i).getDescription());
                                }

                            }
                            lva = new ListViewAdapter(filteredDealersList, getContext());
                            lvDealers.setAdapter(lva);

                        } else {

                            gMap.setMyLocationEnabled(true);
                        }
                    } else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        getActivity().finish();

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                        camera = CameraPosition.builder().target(new LatLng(45.843627, 24.968400)).zoom(8).bearing(0)
                                                .tilt(0).build();
                                        for (int i = 0; i < dealersList.size(); i++) {
                                            createMarker(dealersList.get(i).getLatitude(), dealersList.get(i).getLongitude(),
                                                    dealersList.get(i).getMake() + " " + dealersList.get(i).getName(),
                                                    dealersList.get(i).getDescription());
                                        }

                                        gMap.moveCamera(CameraUpdateFactory.newCameraPosition(camera));
                                        lva = new ListViewAdapter(dealersList, getContext());
                                        lvDealers.setAdapter(lva);
                                        dialog.cancel();
                                    }
                                });
                        final AlertDialog alert = builder.create();
                        alert.show();

                    }
                }
            });
        }


        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("VIEW", "OnPause");
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("VIEW", "OnDestroy");
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.e("VIEW", "OnLowMemory");
        mMapView.onLowMemory();
    }

    protected void createMarker(double latitude, double longitude, String title, String
            snippet) {

        gMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.e("DEBUG", "permission granted");
            } else {
                Log.e("DEBUG", "Permission not granted");

                camera = CameraPosition.builder().target(new LatLng(45.843627, 24.968400)).zoom(7).bearing(0)
                        .tilt(0).build();
                for (int i = 0; i < dealersList.size(); i++) {
                    createMarker(dealersList.get(i).getLatitude(), dealersList.get(i).getLongitude(),
                            dealersList.get(i).getMake() + " " + dealersList.get(i).getName(),
                            dealersList.get(i).getDescription());
                }

                gMap.moveCamera(CameraUpdateFactory.newCameraPosition(camera));
                lva = new ListViewAdapter(dealersList, getContext());
                lvDealers.setAdapter(lva);
            }
        }
    }


}




