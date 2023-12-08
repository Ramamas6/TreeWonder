package com.example.treewonder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class TreeAdapter(private var trees: List<Tree>, private val clickListener: OnTreeClickListener): RecyclerView.Adapter<TreeViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreeViewHolder {
        val rowView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_tree, parent, false)
        return TreeViewHolder(rowView, clickListener)
    }

    override fun onBindViewHolder(holder: TreeViewHolder, position: Int) {
        val tree = trees[position]
        holder.txvName.text = tree.name
        if (!tree.picture.isNullOrBlank()) {
            Picasso.get().load(tree.picture).into(holder.img)
        } else{
            Picasso.get().load(R.drawable.default_img_tree).into(holder.img)
        }
        holder.bind(tree, clickListener)
    }

    override fun getItemCount(): Int {
        return trees.size
    }
}