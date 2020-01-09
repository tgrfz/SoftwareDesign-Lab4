package com.example.softwaredesign_lab4.postfragment

import android.text.Html
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.softwaredesign_lab4.postfragment.PostFragment.OnListFragmentInteractionListener
import com.example.softwaredesign_lab4.R
import com.prof.rssparser.Article
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_post.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MyPostRecyclerViewAdapter(
    private val mValues: MutableList<Article>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyPostRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Article
            mListener?.onListFragmentInteraction(item)
        }
    }

    fun update(posts: List<Article>) {
        mValues.clear()
        mValues.addAll(posts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(mValues[position])

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(article: Article) {

            with(this.itemView) {
                tag = article
                setOnClickListener(mOnClickListener)
            }

            val pubDateString = try {
                val sourceDateString = article.pubDate ?: ""

                val sourceSdf = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
                val date = sourceSdf.parse(sourceDateString)

                val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                sdf.format(date ?: Date())

            } catch (e: ParseException) {
                e.printStackTrace()
                article.pubDate
            }

            itemView.title.text = article.title

            Picasso.get()
                .load(article.image)
                .placeholder(R.drawable.placeholder)
                .into(itemView.image)

            itemView.pubDate.text = pubDateString

            itemView.content.text = article.description?.let {
                Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT)
                    .toString().replace("￼", "").trim()
            } ?: ""
        }
    }
}
