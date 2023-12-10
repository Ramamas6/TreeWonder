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

    /**
     * Get the list of all the tree matching the term text in one of their parameter, order by name
     * If text is blank or null, get the list of all the trees
     * @param text term to research. Default: null
     */
    fun getAllTrees(text: String? = null): ArrayList<Tree> {
        if(text.isNullOrBlank())
            return ArrayList(storage.values
                .sortedBy { tree: Tree -> tree.name })
        else
            return ArrayList(storage.values
                .filter { tree -> tree.name.contains(text, ignoreCase = true) ||
                        (tree.commonName?.contains(text, ignoreCase = true))?: false ||
                        (tree.botanicName?.contains(text, ignoreCase = true))?: false ||
                        (tree.height.toString()?.contains(text, ignoreCase = true))?: false ||
                        (tree.circumference.toString()?.contains(text, ignoreCase = true))?: false ||
                        (tree.plantationYear.toString()?.contains(text, ignoreCase = true))?: false ||
                        (tree.outstandingQualification?.contains(text, ignoreCase = true))?: false ||
                        (tree.summary?.contains(text, ignoreCase = true))?: false ||
                        (tree.description?.contains(text, ignoreCase = true))?: false ||
                        (tree.type?.contains(text, ignoreCase = true))?: false ||
                        (tree.species?.contains(text, ignoreCase = true))?: false ||
                        (tree.variety?.contains(text, ignoreCase = true))?: false ||
                        (tree.latitude.toString()?.contains(text, ignoreCase = true))?: false ||
                        (tree.longitude.toString()?.contains(text, ignoreCase = true))?: false ||
                        (tree.address?.contains(text, ignoreCase = true))?: false
                }
                .sortedBy { tree: Tree -> tree.name })
    }

    fun getTrees(favoritesID: List<Int>): ArrayList<Tree> {
        return ArrayList(favoritesID.mapNotNull { id -> storage[id] })
    }

    fun size(): Int {
        return storage.size
    }

    fun remove(id: Int) {
        storage.remove(id)
    }

    fun clear() {
        storage.clear()
    }

}