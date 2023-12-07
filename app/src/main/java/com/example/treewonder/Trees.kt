package com.example.treewonder

import android.widget.Toast

class Trees {
    private val storage = HashMap<Int, Tree>()

    fun addTree(tree: Tree){
        storage[tree.id] = tree
    }

    fun addTrees(trees: List<Tree>){
        trees.forEach{tree -> addTree(tree)}
    }

    fun getTree(id: Int): Tree {
        return storage[id] ?: throw IllegalAccessException("Unknown name")
    }

    fun getAllTrees(): ArrayList<Tree> {
        return ArrayList(storage.values
            .sortedBy { tree: Tree -> tree.name })

    }

    fun getTrees(favoritesID: List<Int>): ArrayList<Tree> {
        return ArrayList(favoritesID.mapNotNull { id -> storage[id] })
    }

    fun size(): Int {
        return storage.size
    }

}