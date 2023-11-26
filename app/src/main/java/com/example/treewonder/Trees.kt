package com.example.treewonder

class Trees {
    private val storage = HashMap<String, Tree>()

    fun addTree(tree: Tree){
        storage[tree.name] = tree
    }

    fun getTree(name: String): Tree {
        return storage[name] ?: throw IllegalAccessException("Unknown name")
    }

    fun getAllTrees():List<Tree>{
        return storage.values
            .sortedBy { tree: Tree -> tree.name }
    }

}