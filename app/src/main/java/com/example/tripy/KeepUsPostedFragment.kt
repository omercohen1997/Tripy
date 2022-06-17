package com.example.tripy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.example.tripy.databinding.FragmentKeepUsPostedBinding
import com.google.firebase.database.DatabaseReference


class KeepUsPostedFragment : Fragment() {
    private lateinit var binding:FragmentKeepUsPostedBinding
    private lateinit var database: DatabaseReference
    private lateinit var feedback: EditText
    private lateinit var back: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentKeepUsPostedBinding.inflate(layoutInflater)
        back = binding.arrowback
        // Inflate the layout for this fragment
        return binding.root
    }


}