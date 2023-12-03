package com.example.treewonder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class TreeAdapter(private var trees: List<Tree>): RecyclerView.Adapter<TreeViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreeViewHolder {
        val rowView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_tree, parent, false)
        return TreeViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: TreeViewHolder, position: Int) {
        val tree = trees[position]
        holder.txvName.text = tree.name
        Picasso.get().load(tree.picture).into(holder.img)
    }
    override fun getItemCount(): Int {
        return trees.size
    }

    fun updateTrees(allTrees: List<Tree>){
        trees = allTrees
        notifyDataSetChanged()
    }
}