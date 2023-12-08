package com.example.treewonder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TreeViewHolder(rootView: View): RecyclerView.ViewHolder(rootView) {
    var txvName = rootView.findViewById<TextView>(R.id.r_tree_txv_name)
    var img = rootView.findViewById<ImageView>(R.id.r_tree_img)

    fun bind(tree: Tree, clickListener: OnTreeClickListener) {
        itemView.setOnClickListener {
            clickListener.onTreeClick(tree)
        }
    }

}