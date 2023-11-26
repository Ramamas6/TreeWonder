package com.example.treewonder

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import java.lang.IllegalStateException

class CreateTreeFragment: Fragment() {

    private lateinit var listener: TreeCreator
    private lateinit var edtName: EditText
    private lateinit var edtCommonName: EditText
    private lateinit var edtBotanicName: EditText
    private lateinit var edtHeight: EditText
    private lateinit var edtCircumference: EditText
    private lateinit var edtDevelopmentStage: EditText

    private lateinit var btnSave:Button;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savaedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_tree, container, false)

        edtName = view.findViewById(R.id.f_create_tree_edt_name)
        edtCommonName = view.findViewById(R.id.f_create_tree_edt_common_name)
        edtBotanicName = view.findViewById(R.id.f_create_tree_edt_botanic_name)
        edtHeight = view.findViewById(R.id.f_create_tree_edt_height)
        edtCircumference = view.findViewById(R.id.f_create_tree_edt_circumference)
        edtDevelopmentStage = view.findViewById(R.id.f_create_tree_edt_development_stage)

        btnSave = view.findViewById(R.id.f_create_tree_btn_save)
        btnSave.setOnClickListener {
            val name = edtName.text.toString()
            val commonName = edtCommonName.text.toString()
            val botanicName = edtBotanicName.text.toString()
            val height =  Integer.parseInt(edtHeight.text.toString())
            val circumference = Integer.parseInt(edtCircumference.text.toString())
            val developmentStage = edtDevelopmentStage.text.toString()


            val tree = Tree(name,
                            commonName,
                            botanicName,
                            height,
                            circumference,
                            developmentStage)
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