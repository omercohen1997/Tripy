package com.example.tripy

import android.os.Bundle
import android.os.PatternMatcher
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.tripy.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpFragment : Fragment(){
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fname:EditText
    private lateinit var lname:EditText
    private lateinit var email:EditText
    private lateinit var username:EditText
    private lateinit var password:EditText
    private lateinit var cnfPassword:EditText
    private lateinit var database:DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        username = binding.user
        fname = binding.fName
        lname = binding.lName
        email = binding.email
        password = binding.password
        cnfPassword = binding.cnfPassword
        firebaseAuth = FirebaseAuth.getInstance()




        setSignUpBtnListener(binding.signBtn)

        return binding.root
    }

    private fun registerUser() {
        val icon = AppCompatResources.getDrawable(requireContext(),R.drawable.ic_baseline_warning)
        icon?.setBounds(0,0,icon.intrinsicWidth,icon.intrinsicHeight)
        when{
            TextUtils.isEmpty(fname.text.toString().trim())->{
                binding.fName.error = "Plaese Enter First name"
            }
            TextUtils.isEmpty(lname.text.toString().trim())->{
                binding.lName.error = "Plaese Enter Last name"
            }
            TextUtils.isEmpty(email.text.toString().trim())->{
                binding.email.error = "Plaese Enter Email Address"
            }
            (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString().trim()).matches())->{
                //invalid email format
                binding.email.error = "Invalid email format"
            }
            TextUtils.isEmpty(username.text.toString().trim())->{
                binding.user.error = "Plaese Enter Username"
            }
            TextUtils.isEmpty(password.text.toString().trim())->{
                binding.password.error = "Plaese Enter Password"
            }
            TextUtils.isEmpty(cnfPassword.text.toString().trim())->{
                binding.cnfPassword.error = "Plaese Enter Password Again"
            }
            fname.text.toString().isNotEmpty() &&
                    lname.text.toString().isNotEmpty() &&
                    username.text.toString().isNotEmpty() &&
                    password.text.toString().isNotEmpty() &&
                    cnfPassword.text.toString().isNotEmpty()->
            {

                if (password.text.toString()==cnfPassword.text.toString()){
                    firebaseSignUp()
                }else{
                   binding.cnfPassword.error="Password didn't match"
                }
            }

        }
    }


    private fun firebaseSignUp() {
        binding.signBtn.isEnabled = false
        binding.signBtn.alpha = 0.5f
        firebaseAuth.createUserWithEmailAndPassword(email.text.toString(),password.text.toString()).addOnCompleteListener {
                task->
            if (task.isSuccessful){
                database = FirebaseDatabase.getInstance().getReference("Users")
                val User = User(fname.text.toString(),lname.text.toString(),email.text.toString(),username.text.toString(),password.text.toString())
                database.child(username.text.toString()).setValue(User).addOnSuccessListener {
                    Toast.makeText(context,"Successful saved",Toast.LENGTH_SHORT).show()
                }
                Navigation.findNavController(binding.root).navigate(R.id.action_signUpFragment_to_mainFragment)

            }else{
                binding.signBtn.isEnabled = true
                binding.signBtn.alpha = 1.0f
                Toast.makeText(context, task.exception?.message,Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun setSignUpBtnListener(signUpBtn: Button) {
        signUpBtn.setOnClickListener {
            registerUser()
        }

//        binding.signBtn.setOnClickListener{
//                v-> Navigation.findNavController(v).navigate(R.id.action_signUpFragment_to_mainFragment)}
    }



}