package com.example.tripy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.example.tripy.databinding.FragmentKeepUsPostedBinding
import com.google.firebase.database.DatabaseReference


class KeepUsPostedFragment : Fragment() {
    private lateinit var binding:FragmentKeepUsPostedBinding
    private lateinit var database: DatabaseReference
    private lateinit var updates: EditText
    private lateinit var submit:Button
    private lateinit var spinner:Spinner
    private lateinit var back: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentKeepUsPostedBinding.inflate(layoutInflater)
        spinner = binding.dropdown
        updates = binding.upEditText
        back = binding.arrowback
        submit = binding.update
        setBackListener(back)
        val options = arrayOf(R.string.ports,R.string.beaches,R.string.cities,R.string.historical_sites,
            R.string.meu,R.string.national,R.string.nature,R.string.water,R.string.telaviv,R.string.safed,R.string.shows,R.string.zoo,R.string.observ,R.string.parks,R.string.stores)
            .map { getString(it)  }
        val arrayAdapter = context?.let { ArrayAdapter<String>(it,android.R.layout.simple_spinner_dropdown_item,options) }
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(context, R.string.please,Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(context, options[position],Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
    private fun setBackListener(back: ImageView){
        back.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_keep_us_posted_to_mainFragment)
        }
    }


}

