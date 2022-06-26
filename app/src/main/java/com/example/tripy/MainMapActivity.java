package com.example.tripy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tripy.databinding.MainMapActivityBinding;
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

public class MainMapActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallBack {

    GoogleMap map;
    Button btnGetDirection;
    MarkerOptions place1, place2;
    Polyline currentPolyline;

    String FIREBASE_URL = "https://tripy-9cb98-default-rtdb.firebaseio.com/";
    Firebase firebase;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_map_activity);

        //Binding
        com.example.tripy.databinding.MainMapActivityBinding binding = MainMapActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Obtain the SupportMapFragment and get notified when the map is ready to be used
        if(!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);}

        btnGetDirection = findViewById(R.id.btnGetDirection);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(this);

        //initializing 2 places on the map
        place1 = new MarkerOptions().position(new LatLng(32.0133239,34.7479175)).title("Location 1");
        place2 = new MarkerOptions().position(new LatLng(32.0154345,34.7580456)).title("Location 2");

        //Fetching API
        String url = getUrl(place1.getPosition(), place2.getPosition(),"driving");
        new FetchURL(MainMapActivity.this).execute(url, "driving");

    }

    //building the api
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        String str_origin = "origin=" + origin.latitude + ", " + origin.longitude;
        String str_dest = "destination=" + dest.latitude + ", " + dest.longitude;
        String mode = "mode=" + directionMode;
        String parameters = str_origin+ "&" + str_dest + "&" + mode;
        //String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/json?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
    }


    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline!=null)
            currentPolyline.remove();
        currentPolyline = map.addPolyline((PolylineOptions) values[0]);
    }
}
