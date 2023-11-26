package com.example.treewonder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TreeViewHolder(rootView: View): RecyclerView.ViewHolder(rootView) {
    var txvName = rootView.findViewById<TextView>(R.id.r_tree_txv_name)
}