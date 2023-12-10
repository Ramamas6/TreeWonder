package com.ismin.treewonder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


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
