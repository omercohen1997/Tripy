package com.example.tripy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class MainMapActivityFragment extends Fragment {
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
}
