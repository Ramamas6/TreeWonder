package com.example.treewonder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ARG_TREES = "param1"
class TreeListFragment : Fragment(){
    private var trees: ArrayList<Tree> = arrayListOf()
    private  lateinit var treeAdapter: TreeAdapter
    private  lateinit var recyclerView: RecyclerView

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            trees = it.getSerializable(ARG_TREES) as ArrayList<Tree>
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tree_list, container, false)

        recyclerView = view.findViewById(R.id.f_tree_list_rcv_trees)
        treeAdapter = TreeAdapter(trees)
        recyclerView.adapter = treeAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(trees: ArrayList<Tree>) =
            TreeListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_TREES, trees)
                }
            }
    }
}