package com.example.treewonder

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CreateTreeFragment: Fragment() {

    private lateinit var edtName: EditText
    private lateinit var edtCommonName: EditText
    private lateinit var edtBotanicName: EditText
    private lateinit var edtHeight: EditText
    private lateinit var edtCircumference: EditText
    private lateinit var edtDevelopmentStage: EditText

    private lateinit var btnNext: Button
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

        btnNext = view.findViewById(R.id.f_create_tree_btn_next)
        btnNext.setOnClickListener {
            val nextFragment = CreateTreeFragment2()
            val bundle = Bundle()
            bundle.putString("name", edtName.text.toString())
            bundle.putString("commonName", edtCommonName.text.toString())
            bundle.putString("botanicName", edtBotanicName.text.toString())
            bundle.putInt("height", Integer.parseInt(edtHeight.text.toString()))
            bundle.putInt("circumference", Integer.parseInt(edtCircumference.text.toString()))
            bundle.putString("developmentStage", edtDevelopmentStage.text.toString())
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
