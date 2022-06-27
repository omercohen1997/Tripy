package com.example.tripy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Checkable
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.example.tripy.databinding.FragmentFiltterAttractionBinding
import androidx.navigation.fragment.findNavController


class FiltterAttraction : Fragment() {
    private lateinit var binding:FragmentFiltterAttractionBinding
    private lateinit var back: ImageView
    private lateinit var enter: Button
    private lateinit var markAll: Button
    private lateinit var clearAll: Button

    //private var checkBoxes: ArrayList<CheckBox> = ArrayList()
   // var myList: ArrayList<Int> = arrayListOf()
    private val TAG = "FilterTest"

    private lateinit var checkbox1: CheckBox
    private lateinit var checkbox2: CheckBox
    private lateinit var checkbox3: CheckBox
    private lateinit var checkbox4: CheckBox
    private lateinit var checkbox5: CheckBox
    private lateinit var checkbox6: CheckBox
    private lateinit var checkbox7: CheckBox
    private lateinit var checkbox8: CheckBox
    private lateinit var checkbox9: CheckBox
    private lateinit var checkbox10: CheckBox
    private lateinit var checkbox11: CheckBox
    private lateinit var checkbox12: CheckBox
    private lateinit var checkbox13: CheckBox
    private lateinit var checkbox14: CheckBox
    private lateinit var checkbox15: CheckBox
    private lateinit var checkbox16: CheckBox
    private var checkboxesArray = arrayOf<CheckBox>()
   // val checkboxes = arrayOf<Checkable>(binding.checkBox1,R.id.checkBox2,R.id.checkBox3,R.id.checkBox4,R.id.checkBox5,R.id.checkBox6,
     //       R.id.checkBox7,R.id.checkBox8,R.id.checkBox9,R.id.checkBox10,R.id.checkBox11,R.id.checkBox12,R.id.checkBox13,R.id.checkBox14
   // ,R.id.checkBox15,R.id.checkBox16)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFiltterAttractionBinding.inflate(layoutInflater)

        checkboxesArray = arrayOf<CheckBox>(binding.checkBox1,binding.checkBox2,binding.checkBox3,binding.checkBox4,binding.checkBox5,
                binding.checkBox6,binding.checkBox7,binding.checkBox8,binding.checkBox9,binding.checkBox10,binding.checkBox11,
        binding.checkBox12,binding.checkBox13,binding.checkBox14,binding.checkBox15,binding.checkBox16)

        enter = binding.filterRes
        markAll = binding.btnCheckAll
        clearAll = binding.btnClearAll
        back = binding.arrowback
        setBackListener(back)
        setMarkAllListener(markAll)
        setClearAllListener(clearAll)
        setEnterListener(enter)
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setBackListener(back:ImageView){
        back.setOnClickListener {
            var chosenAttractionsArraylist = ArrayList<String>()
            // שהמשתמש לוחץ על החץ אחורה אז זה לא שומר את הפליטרים שהוא עשה ומעביר חזרה למיין פרגמנט מערך ריק כדי שהוא ידע שם שהוא צריך להציג את כל האטרקציות
            findNavController(binding.root).navigate(R.id.action_filtterAttraction_to_mainFragment,Bundle().apply {
                putStringArrayList("categoryFilerKey",chosenAttractionsArraylist)
        })
        }
    }

    //the logic to clear all
    private fun setClearAllListener(clearAll: Button) {
        clearAll.setOnClickListener {
            for(i in checkboxesArray.indices)
                checkboxesArray[i].isChecked = false
        }
    }

    //the logic to mark all
    private fun setMarkAllListener(back: Button) {

        back.setOnClickListener {
           for(i in checkboxesArray.indices)
               checkboxesArray[i].isChecked = true
        }
    }

    //after the logic of filtering you need to write the logic to move to the map with the right attraction
    private fun setEnterListener(enter: Button) {
        enter.setOnClickListener {
            var chosenAttractionsArraylist = ArrayList<String>()   // saving the categories that have been chosen
            for(i in checkboxesArray.indices)
                if(checkboxesArray[i].isChecked) {
                    chosenAttractionsArraylist.add(checkboxesArray[i].text.toString())
                    Log.d(TAG,checkboxesArray[i].text.toString())
                }

            findNavController(binding.root).navigate(R.id.action_filtterAttraction_to_mainFragment,Bundle().apply {
               putStringArrayList("categoryFilerKey",chosenAttractionsArraylist)
            })
        }
    }
}