package com.example.softwaredesign_lab4.tasks

import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.softwaredesign_lab4.DataStorage
import com.example.softwaredesign_lab4.model.Post
import com.example.softwaredesign_lab4.viewmodel.PostListViewModel
import java.lang.Exception

class CacheLoadingTask(
    private val model: PostListViewModel,
    private val mSwipeLayout: SwipeRefreshLayout,
    private val context: Context
) :
    AsyncTask<Void, Void, Boolean>() {

    override fun doInBackground(vararg p0: Void?): Boolean {
        return try {
            model.loadPosts()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun onPreExecute() {
        mSwipeLayout.isRefreshing = true
    }

    override fun onPostExecute(result: Boolean) {
        mSwipeLayout.isRefreshing = false
        if (!result)
            Toast.makeText(context, "No cached news. Turn on internet connection.", Toast.LENGTH_LONG).show()
    }

}