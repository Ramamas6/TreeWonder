package com.ismin.treewonder.mainFragments

import com.ismin.treewonder.Tree

interface OnTreeClickListener {
    fun onTreeClick(tree: Tree)
    fun onFavoriteClick(tree: Tree)
}