package com.example.treewonder

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
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

        // Handle button map
        val btnMap = view.findViewById<FloatingActionButton>(R.id.f_tree_btn_map)
        btnMap.setOnClickListener{
            if(tree.latitude == 0.0 && tree.longitude == 0.0)
                Toast.makeText(context, "Impossible to show on map : latitude and longitude undefined", Toast.LENGTH_SHORT).show()
            else (activity as MainActivity).teleportToPosition(LatLng(tree.latitude, tree.longitude))
        }

        // Handle button edit
        val btnEdit = view.findViewById<FloatingActionButton>(R.id.f_tree_btn_edit)
        btnEdit.setOnClickListener{
            Toast.makeText(context, "Not implemented", Toast.LENGTH_SHORT).show()
        }

        // Handle general texts
        view.findViewById<TextView>(R.id.f_tree_text_name).text = tree.name
        view.findViewById<TextView>(R.id.f_tree_text_commonName).text =
            if(tree.commonName.isNullOrBlank()) ""
            else tree.commonName
        view.findViewById<TextView>(R.id.f_tree_text_height).text =
            if(tree.height > 0) "Height: ${tree.height} meters"
            else "Unknown height"
        view.findViewById<TextView>(R.id.f_tree_text_age).text =
            if(tree.plantationYear > 0) "Age: ${2023 - tree.plantationYear} years old (${tree.plantationYear})"
            else "Unknown age"
        view.findViewById<TextView>(R.id.f_tree_text_circumference).text =
            if(tree.circumference > 0) "Circumference: ${tree.circumference} centimeters"
            else "Unknown circumference"

        // Handle description
        if(tree.description.isNullOrBlank()) {
            view.findViewById<TextView>(R.id.f_tree_text_description).visibility = View.GONE
            view.findViewById<View>(R.id.f_tree_divider1).visibility = View.GONE
        }
        else view.findViewById<TextView>(R.id.f_tree_text_description).text = tree.description

        // Handle scientific texts
        var displaySeparator = false
        if(tree.botanicName.isNullOrBlank()) view.findViewById<TextView>(R.id.f_tree_text_botanic_name).visibility = View.GONE
        else {
            view.findViewById<TextView>(R.id.f_tree_text_botanic_name).text = "Botanic name: ${tree.botanicName}"
            displaySeparator = true
        }
        if(tree.type.isNullOrBlank()) view.findViewById<TextView>(R.id.f_tree_text_type).visibility = View.GONE
        else {
            view.findViewById<TextView>(R.id.f_tree_text_type).text = "Type: ${tree.type}"
            displaySeparator = true
        }

        if(tree.species.isNullOrBlank()) view.findViewById<TextView>(R.id.f_tree_text_species).visibility = View.GONE
        else {
            view.findViewById<TextView>(R.id.f_tree_text_species).text ="Species: ${tree.species}"
            displaySeparator = true
        }

        if(tree.variety.isNullOrBlank()) view.findViewById<TextView>(R.id.f_tree_text_variety).visibility = View.GONE
        else {
            view.findViewById<TextView>(R.id.f_tree_text_variety).text = "Variety: ${tree.variety}"
            displaySeparator = true
        }
        if(!displaySeparator) view.findViewById<View>(R.id.f_tree_divider2).visibility = View.GONE

        // Handle position
        if(tree.address.isNullOrBlank()) view.findViewById<TextView>(R.id.f_tree_text_address).visibility = View.GONE
        else {
            view.findViewById<TextView>(R.id.f_tree_text_address).text = "Address: ${tree.address}"
            view.findViewById<View>(R.id.f_tree_divider3).visibility = View.VISIBLE
        }
        if(tree.latitude == 0.0 && tree.longitude == 0.0) {
            view.findViewById<TextView>(R.id.f_tree_text_latitude).visibility = View.GONE
            view.findViewById<TextView>(R.id.f_tree_text_longitude).visibility = View.GONE
        }
        else {
            view.findViewById<TextView>(R.id.f_tree_text_latitude).text = "Latitude: ${tree.latitude}"
            view.findViewById<TextView>(R.id.f_tree_text_longitude).text = "Longitude: ${tree.longitude}"
            view.findViewById<View>(R.id.f_tree_divider3).visibility = View.VISIBLE
        }

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