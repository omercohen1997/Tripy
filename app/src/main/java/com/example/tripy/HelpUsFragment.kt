package com.example.tripy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.tripy.databinding.FragmentHelpUsImproveBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class HelpUsFragment : Fragment() {
    private lateinit var binding:FragmentHelpUsImproveBinding
    private lateinit var database: DatabaseReference
    private lateinit var feedback: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHelpUsImproveBinding.inflate(layoutInflater)
        feedback = binding.subEditText
        setSubmissionListener(binding.submit)
        return binding.root
    }
    private fun setSubmissionListener(submitBtn: Button) {
        binding.submit.setOnClickListener{submitFeedback()}

    }

    private fun submitFeedback() {
        database = FirebaseDatabase.getInstance().getReference("Feedbacks")
        val submition = Feedbacks(feedback.text.toString())
        database.child("feedbacks").push().setValue(submition).addOnSuccessListener {
            Toast.makeText(context,"We saved your feedback, Thank you for your time!", Toast.LENGTH_SHORT).show()
        }
        findNavController().navigate(R.id.action_helpFragment_to_mainFragment)
    }


}