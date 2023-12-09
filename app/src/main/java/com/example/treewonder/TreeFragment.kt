package com.example.treewonder

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


private const val ARG_TREE = "param1"

class TreeFragment : Fragment() {
    private lateinit var tree: Tree

    private lateinit var img: ImageView

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tree = it.getSerializable(ARG_TREE) as Tree
        }

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tree, container, false)



        view.findViewById<TextView>(R.id.f_tree_text_name).text = tree.name
        view.findViewById<TextView>(R.id.f_tree_text_commonName).text = tree.commonName
        view.findViewById<TextView>(R.id.f_tree_text_height).text = tree.height.toString()
        view.findViewById<TextView>(R.id.f_tree_text_circumference).text = tree.circumference.toString()

        view.findViewById<TextView>(R.id.f_tree_text_description).text = tree.description

        img = view.findViewById(R.id.f_tree_picture)
        if (!tree.picture.isNullOrBlank()) {
            Picasso.get().load(tree.picture).into(img)
        } else{
            Picasso.get().load(R.drawable.default_img_tree).into(img)
        }

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