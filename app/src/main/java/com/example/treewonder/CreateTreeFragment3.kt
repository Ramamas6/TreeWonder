package com.example.treewonder

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import java.text.DecimalFormat


class CreateTreeFragment3 : Fragment() {

    private lateinit var listener: TreeCreator

    private var name: String = ""
    private var commonName: String = ""
    private var botanicName: String = ""
    private var height: Int = 0
    private var circumference: Int = 0
    private var developmentStage: String = ""
    private var plantationYear: Int = 0
    private var outstandingQualification: String = ""
    private var summary: String = ""
    private var description: String = ""
    private var type: String = ""
    private var species: String = ""
    private var variety: String = ""
    private var sign: String = ""

    private lateinit var edtPicture: EditText
    private lateinit var edtLongitude: EditText
    private lateinit var edtLatitude: EditText
    private lateinit var edtAddress: EditText
    private lateinit var btnSave: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_tree3, container, false)

        //Get data from the parent fragment
        val treeData: Bundle? = arguments
        if (treeData != null) {
            name = treeData.getString("name", "")
            commonName = treeData.getString("commonName", "")
            botanicName = treeData.getString("botanicName", "")
            height = treeData.getInt("height", 0)
            circumference = treeData.getInt("circumference", 0)
            developmentStage = treeData.getString("developmentStage", "")
            plantationYear = treeData.getInt("plantationYear", 0)
            outstandingQualification = treeData.getString("outstandingQualification", "")
            summary = treeData.getString("summary", "")
            description = treeData.getString("description", "")
            type = treeData.getString("type", "")
            species = treeData.getString("species", "")
            variety = treeData.getString("variety", "")
            sign = treeData.getString("sign", "")
        }
        edtPicture = view.findViewById(R.id.f_create_tree_edt_picture)
        edtLongitude = view.findViewById(R.id.f_create_tree_edt_longitude)
        edtLatitude = view.findViewById(R.id.f_create_tree_edt_latitude)
        edtAddress = view.findViewById(R.id.f_create_tree_edt_address)
        btnSave = view.findViewById(R.id.f_create_tree_btn_save)

        btnSave.setOnClickListener {
            val picture = edtPicture.text.toString()

            val longitude = edtLongitude.text.toString().toDouble()
            val latitude = edtLatitude.text.toString().toDouble()
            val address = edtAddress.text.toString()

            val tree = Tree(id,
                            name,
                            commonName,
                            botanicName,
                            height,
                            circumference,
                            developmentStage,
                            plantationYear,
                            outstandingQualification,
                            summary,
                            description,
                            type,
                            species,
                            variety,
                            sign,
                            picture,
                            longitude,
                            latitude,
                            address
            )

            this.listener.onTreeCreated(tree)
        }
        return view
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is TreeCreator) {
            this.listener = context
        }else{
            throw IllegalStateException("$context must implement TreeCreator")
        }
    }
}