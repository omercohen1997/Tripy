package com.example.tripy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.example.tripy.databinding.FragmentFiltterAttractionBinding


class FiltterAttraction : Fragment() {
    private lateinit var binding:FragmentFiltterAttractionBinding
    private lateinit var back: ImageView
    private lateinit var enter: Button
    private lateinit var markAll: Button
    private lateinit var clearAll: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFiltterAttractionBinding.inflate(layoutInflater)

        enter = binding.filterRes
        markAll = binding.btnCheckAll
        clearAll = binding.btnClearAll
        setMarkAllListener(markAll)
        setClearAllListener(clearAll)
        setEnterListener(enter)
        // Inflate the layout for this fragment
        return binding.root
    }

    //the logic to clear all
    private fun setClearAllListener(clearAll: Button) {

    }
    //the logic to mark all
    private fun setMarkAllListener(back: Button) {

    }

    //after the logic of filttering you need to write the logic to move to the map with the right attraction
    private fun setEnterListener(enter: Button) {

    }



}