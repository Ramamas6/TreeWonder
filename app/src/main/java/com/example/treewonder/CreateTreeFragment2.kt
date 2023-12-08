package com.example.treewonder

import android.icu.number.IntegerWidth
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction

class CreateTreeFragment2 : Fragment() {
    private var name: String = ""
    private var commonName: String = ""
    private var botanicName: String = ""
    private var height: Int = 0
    private var circumference: Int = 0
    private var developmentStage: String  =""
    private var plantationYear = 1800

    private lateinit var edtPlantationYear: EditText
    private lateinit var edtOutstandingQualification: EditText
    private lateinit var edtSummary: EditText
    private lateinit var edtDescription: EditText
    private lateinit var edtType: EditText
    private lateinit var edtSpecies: EditText
    private lateinit var edtVariety: EditText
    private lateinit var edtSign: EditText
    private lateinit var btnNext: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_tree2, container, false)

        //Get data from the parent fragment
        val treeData: Bundle? = arguments
        if(treeData != null){
            name = treeData.getString("name", "")
            commonName = treeData.getString("commonName", "")
            botanicName = treeData.getString("botanicName", "")
            height = treeData.getInt("height", 0)
            circumference = treeData.getInt("circumference", 0)
            developmentStage = treeData.getString("developmentStage", "")

        }

        edtPlantationYear = view.findViewById(R.id.f_create_tree_edt_plantation_year)
        edtOutstandingQualification = view.findViewById(R.id.f_create_tree_edt_outstanding_qualification)
        edtSummary = view.findViewById(R.id.f_create_tree_edt_summary)
        edtDescription = view.findViewById(R.id.f_create_tree_edt_description)
        edtType = view.findViewById(R.id.f_create_tree_edt_type)
        edtSpecies = view.findViewById(R.id.f_create_tree_edt_species)
        edtVariety = view.findViewById(R.id.f_create_tree_edt_variety)
        edtSign = view.findViewById(R.id.f_create_tree_edt_sign)
        btnNext = view.findViewById(R.id.f_create_tree_btn_next)

        btnNext.setOnClickListener {

            if (edtPlantationYear.text.isNotEmpty()) {
                plantationYear = Integer.parseInt(edtPlantationYear.text.toString())
            }
            val nextFragment = CreateTreeFragment3()
            val bundle = Bundle()
            bundle.putString("name", name)
            bundle.putString("commonName", commonName)
            bundle.putString("botanicName", botanicName)
            bundle.putInt("height", height)
            bundle.putInt("circumference", circumference)
            bundle.putString("developmentStage", developmentStage)
            bundle.putInt("plantationYear", plantationYear)
            bundle.putString("outstandingQualification", edtOutstandingQualification.text.toString())
            bundle.putString("summary", edtSummary.text.toString())
            bundle.putString("description", edtDescription.text.toString())
            bundle.putString("type", edtType.text.toString())
            bundle.putString("species", edtSpecies.text.toString())
            bundle.putString("variety", edtVariety.text.toString())
            bundle.putString("sign", edtSign.text.toString())
            nextFragment.arguments = bundle
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction
                .replace(
                    R.id.a_main_lyt_fragment,
                    nextFragment
            )
            transaction.commit()
        }

        return view
    }
}


