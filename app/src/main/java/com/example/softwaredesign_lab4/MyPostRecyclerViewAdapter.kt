package com.example.softwaredesign_lab4

import android.media.Image
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.example.softwaredesign_lab4.PostFragment.OnListFragmentInteractionListener
import com.example.softwaredesign_lab4.model.Post

import kotlinx.android.synthetic.main.fragment_post.view.*

class MyPostRecyclerViewAdapter(
    private val mValues: List<Post>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyPostRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Post
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mTitleView.text = item.title
        holder.mContentView.text = item.content
        holder.mDateView.text = item.date

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mTitleView: TextView = mView.postTitle
        val mContentView: TextView = mView.postContent
        val mDateView: TextView = mView.postDate
        val mImageView: ImageView = mView.postImage
    }
}
