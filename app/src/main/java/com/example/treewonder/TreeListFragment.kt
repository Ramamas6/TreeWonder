package com.example.treewonder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ARG_TREES = "param1"
private const val ARG_FAVORITES = "param2"
private const val ARG_POSITION = "param3"
class TreeListFragment : Fragment(), OnTreeClickListener {
    private var trees: ArrayList<Tree> = arrayListOf()
    private var favoritesList: ArrayList<Int> = arrayListOf()
    private var initialPosition: Int = 0
    private  lateinit var treeAdapter: TreeAdapter
    private  lateinit var recyclerView: RecyclerView

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            trees = it.getSerializable(ARG_TREES) as ArrayList<Tree>
            favoritesList = it.getSerializable(ARG_FAVORITES) as ArrayList<Int>
            //initialPosition = it.getSerializable(ARG_POSITION) as Int
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tree_list, container, false)

        recyclerView = view.findViewById(R.id.f_tree_list_rcv_trees)
        treeAdapter = TreeAdapter(trees, favoritesList, this)
        recyclerView.adapter = treeAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        recyclerView.scrollToPosition(initialPosition)
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(trees: ArrayList<Tree>, favoritesList: ArrayList<Int>) = //, initialPosition: Int
            TreeListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_TREES, trees)
                    putSerializable(ARG_FAVORITES, favoritesList)
                    //putSerializable(ARG_POSITION, initialPosition)
                }
            }
    }

    override fun onTreeClick(tree: Tree) {
        (activity as MainActivity).displayTreeFragment(tree)
    }
    override fun onFavoriteClick(tree: Tree) {
        val currentPosition = recyclerView.getChildAdapterPosition(recyclerView.getChildAt(0))
        (activity as MainActivity).changeFavorites(tree.id, true, currentPosition)
    }
}