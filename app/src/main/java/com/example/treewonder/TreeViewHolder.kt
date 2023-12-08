package com.example.treewonder

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TreeViewHolder(rootView: View, private val listener: OnTreeClickListener): RecyclerView.ViewHolder(rootView) {
    var txvName = rootView.findViewById<TextView>(R.id.r_tree_txv_name)
    var img = rootView.findViewById<ImageView>(R.id.r_tree_img)
    var btn = rootView.findViewById<FloatingActionButton>(R.id.r_tree_btn_favorites)

    fun bind(tree: Tree, clickListener: OnTreeClickListener) {
        txvName.setOnClickListener {
            listener.onTreeClick(tree)
        }
        img.setOnClickListener {
            listener.onTreeClick(tree)
        }
        btn.setOnClickListener {
            listener.onFavoriteClick(tree)
        }
    }

}