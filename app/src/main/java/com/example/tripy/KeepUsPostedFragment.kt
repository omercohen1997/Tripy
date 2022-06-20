package com.example.tripy

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.fragment.findNavController
import com.example.tripy.databinding.FragmentKeepUsPostedBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class KeepUsPostedFragment : Fragment() {
    private lateinit var binding:FragmentKeepUsPostedBinding
    private lateinit var database: DatabaseReference
    private lateinit var updates: EditText
    private lateinit var submit:Button
    private lateinit var spinner:Spinner
    private lateinit var back: ImageView
    private lateinit var textUpdate:EditText
    private lateinit var textNameAtt:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentKeepUsPostedBinding.inflate(layoutInflater)
        spinner = binding.dropdown
        back = binding.arrowback
        submit = binding.update
        textUpdate= binding.upEditText
        textNameAtt = binding.editText
        setBackListener(back)
        setSubmissionListener(submit)
        val options = arrayOf(R.string.agri,R.string.ports,R.string.beaches,R.string.cities,R.string.historical_sites,
            R.string.meu,R.string.national,R.string.nature,R.string.water,R.string.telaviv,R.string.safed,R.string.shows,R.string.zoo,R.string.observ,R.string.parks,R.string.stores)
            .map { getString(it)  }
        val arrayAdapter = context?.let { ArrayAdapter<String>(it,android.R.layout.simple_spinner_dropdown_item,options) }
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }
        }

        return binding.root
    }
    private fun setBackListener(back: ImageView){
        back.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_keep_us_posted_to_mainFragment)
        }
    }
    private fun setSubmissionListener(submitBtn: Button) {
        submit.setOnClickListener {
            val selectionSpinner = spinner.selectedItem.toString()
            val txt = textNameAtt.text.toString()
            val icon = AppCompatResources.getDrawable(requireContext(),R.drawable.ic_baseline_warning)
            icon?.setBounds(0,0,icon.intrinsicWidth,icon.intrinsicHeight)
            when{
                TextUtils.isEmpty(txt.trim())->{
                    textNameAtt.error =  R.string.please2.toString()
                }
            }
            database = FirebaseDatabase.getInstance().getReference("Updates")
            val submition = textUpdate.text.toString()
            database.child(selectionSpinner).child(txt).push().setValue(submition).addOnSuccessListener {
                Toast.makeText(context,R.string.thank2, Toast.LENGTH_SHORT).show()
            }
                findNavController().navigate(R.id.action_fragment_keep_us_posted_to_mainFragment)
            }

        }

    }
//    private fun submitUpdates() {
//
//
//            }



