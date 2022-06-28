package com.example.tripy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainMapActivityFragment extends Fragment {
    GoogleMap mMap;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;

    // private ResultProfileBinding binding; //////////////////////////////////////////////////////////////////

    public MainMapActivityFragment(){
        super(R.layout.main_map_activity);
    }
    //public View onCreateView
    //public void onCreate (Bundle savedInstanceState) {
    //  super.onCreate(savedInstanceState);
    //}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // binding = ResultProfileBinding.inflate(getLayoutInflater()); ///////////////////////////////////////

        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_map_activity, container, false);
    }


//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        mMap = googleMap;
//        mDatabaseReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                LatLng newLocation = new LatLng(
//                        dataSnapshot.child("latitude").getValue(double.class),
//                        dataSnapshot.child("Longitude").getValue(double.class)
//                );
//                mMap.addMarker(new MarkerOptions().position(newLocation).title(dataSnapshot.getKey()));
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                throw databaseError.toException();
//            }
//        });
//    }

}
