package com.example.tripy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tripy.helpers.FetchURL;
import com.example.tripy.helpers.TaskLoadedCallBack;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

public class MainMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    Button btnGetDirection;
    MarkerOptions place1, place2;
    Polyline currentPolyline;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_map_activity);
        btnGetDirection = findViewById(R.id.btnGetDirection);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(this);
        //initializing 2 places on the map
        place1 = new MarkerOptions().position(new LatLng(32.0133239,34.7479175)).title("Location 1");
        place2 = new MarkerOptions().position(new LatLng(32.0154345,34.7580456)).title("Location 2");

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
    }


}
