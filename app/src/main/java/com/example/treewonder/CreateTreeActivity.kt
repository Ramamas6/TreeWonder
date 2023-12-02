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

        val btnExit = findViewById<Button>(R.id.a_create_tree_exit)
        // Click on "Exit" button : switch to main activity and send it the tree
        btnExit.setOnClickListener {
            val tree = Tree(1, "Name", "commonName", "botanicName", 10, 10, "", 0, "", "", "", "", "", "", "", "", 40.0, 40.0, "")
            val returnIntent = Intent()
            returnIntent.putExtra(TREE_KEY, tree)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

    }
        private fun displayCreatedTreeFragment(){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.a_main_toolbal_fragment,
                CreateTreeFragment()
            )
            transaction.commit()
            //floatingActionButton.visibility = View.GONE
        }
}
