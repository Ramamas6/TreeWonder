package com.example.treewonder

class Trees {
    private val storage = HashMap<Int, Tree>()

    fun addTree(tree: Tree){
        storage[tree.id] = tree
    }

    fun getTree(id: Int): Tree {
        return storage[id] ?: throw IllegalAccessException("Unknown id")
    }

    fun getAllTrees():List<Tree>{
        return storage.values
            .sortedBy { tree: Tree -> tree.name }
    }

}