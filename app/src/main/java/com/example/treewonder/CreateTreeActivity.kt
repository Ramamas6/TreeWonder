package com.example.treewonder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class CreateTreeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_tree)

        displayCreatedTreeFragment()

    }
        private fun displayCreatedTreeFragment(){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.a_main_lyt_fragment,
                CreateTreeFragment()
            )
            val rez = transaction.commit()
        }
}
