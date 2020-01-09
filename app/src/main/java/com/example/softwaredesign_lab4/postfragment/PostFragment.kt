package com.example.softwaredesign_lab4.postfragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.softwaredesign_lab4.R

import com.example.softwaredesign_lab4.viewmodel.PostListViewModel
import com.prof.rssparser.Article

class PostFragment : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null
    private var listAdapter: MyPostRecyclerViewAdapter? = null
    private lateinit var model: PostListViewModel
    private lateinit var allPosts: List<Article>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        model = ViewModelProviders.of(requireActivity())[PostListViewModel::class.java]
        allPosts = model.getArticleList().value?.articles ?: emptyList()
        model.getArticleList().observe(this, Observer {
            it?.let {
                allPosts = it.articles
                listAdapter?.update(allPosts)
            }
        })

        val view = inflater.inflate(R.layout.fragment_post_list, container, false)
        allPosts = emptyList()
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                listAdapter =
                    MyPostRecyclerViewAdapter(
                        allPosts.toMutableList(),
                        listener
                    )
                adapter = listAdapter
                addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString().plus(" must implement OnListFragmentInteractionListener"))
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Article)
    }
}
