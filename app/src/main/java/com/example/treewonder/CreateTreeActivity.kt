package com.example.treewonder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val TREE_KEY = "tree-key"

class CreateTreeActivity : AppCompatActivity(), TreeCreator {

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
            transaction.commit()
        }

    override fun onTreeCreated(tree: Tree) {
        val intent = intent
        intent.putExtra(TREE_KEY, tree)
        setResult(RESULT_OK, intent)
        finish()
    }


}
