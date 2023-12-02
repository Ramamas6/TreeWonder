package com.example.treewonder

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

    fun size(): Int {
        return storage.size
    }

}