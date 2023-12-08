package com.example.treewonder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

private const val ARG_TREE = "param1"

class TreeFragment : Fragment() {
    private lateinit var tree: Tree

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tree = it.getSerializable(ARG_TREE) as Tree
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tree, container, false)

        view.findViewById<TextView>(R.id.f_tree_text_name).text = tree.name
        view.findViewById<TextView>(R.id.f_tree_text_commonName).text = tree.commonName
        view.findViewById<TextView>(R.id.f_tree_text_height).text = tree.height.toString()
        view.findViewById<TextView>(R.id.f_tree_text_cirumference).text = tree.circumference.toString()

        view.findViewById<TextView>(R.id.f_tree_text_description).text = tree.description

        // Inflate the layout for this fragment
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(tree: Tree) =
            TreeFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_TREE, tree)
                }
            }
    }
}