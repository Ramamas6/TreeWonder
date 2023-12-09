package com.example.treewonder

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso


private const val ARG_TREE = "param1"
private const val ARG_FAVORITE = "param2"

class TreeFragment : Fragment() {
    private lateinit var tree: Tree
    private var isFavorite = false
    private lateinit var img: ImageView

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tree = it.getSerializable(ARG_TREE) as Tree
            isFavorite = it.getSerializable(ARG_FAVORITE) as Boolean
        }

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tree, container, false)

        // Handle button favorite
        val btnFavorite = view.findViewById<FloatingActionButton>(R.id.f_tree_btn_favorite)
        if(isFavorite) btnFavorite.setImageResource(R.drawable.baseline_star_24)
        else btnFavorite.setImageResource(R.drawable.baseline_star_border_24)
        btnFavorite.setOnClickListener{
            isFavorite = !isFavorite
            if(isFavorite) btnFavorite.setImageResource(R.drawable.baseline_star_24)
            else btnFavorite.setImageResource(R.drawable.baseline_star_border_24)
            (activity as MainActivity).changeFavorites(tree.id, false)
        }

        // Handle texts
        view.findViewById<TextView>(R.id.f_tree_text_name).text = tree.name
        view.findViewById<TextView>(R.id.f_tree_text_commonName).text = tree.commonName
        view.findViewById<TextView>(R.id.f_tree_text_height).text = tree.height.toString()
        view.findViewById<TextView>(R.id.f_tree_text_age).text = tree.plantationYear.toString()
        view.findViewById<TextView>(R.id.f_tree_text_circumference).text = tree.circumference.toString()
        view.findViewById<TextView>(R.id.f_tree_text_description).text = tree.description

        // Handle image
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
        fun newInstance(tree: Tree, isFavorite: Boolean) =
            TreeFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_TREE, tree)
                    putSerializable(ARG_FAVORITE, isFavorite)
                }
            }
    }
}