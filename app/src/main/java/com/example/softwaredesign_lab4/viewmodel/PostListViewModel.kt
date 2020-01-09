package com.example.softwaredesign_lab4.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.softwaredesign_lab4.App
import com.prof.rssparser.Channel
import com.prof.rssparser.Parser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class PostListViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val storage by lazy { getApplication<App>().storage }
    private val articleListLive: MutableLiveData<Channel> = MutableLiveData()

    fun getArticleList(): MutableLiveData<Channel> {
        return articleListLive
    }

    fun loadFromCache(swipe: SwipeRefreshLayout) {
        coroutineScope.launch(Dispatchers.Main) {
            try {
                setChannel(Channel(null, null, null, null, storage.getPosts().toMutableList()))
                swipe.isRefreshing = false
            } catch (e: Exception) {
                e.printStackTrace()
                setChannel(Channel(null, null, null, null, mutableListOf()))
            }
        }
    }

    private fun setChannel(channel: Channel) {
        articleListLive.postValue(channel)
        storage.deletePosts()
        storage.insertPosts(channel.articles)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun fetchFeed(url: String, swipe: SwipeRefreshLayout, bar: ActionBar?) {
        coroutineScope.launch(Dispatchers.Main) {
            try {
                val parser = Parser()
                val articleList = parser.getChannel(url)
                setChannel(articleList)
                swipe.isRefreshing = false
                bar?.title = articleList.title
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(getApplication(), e.message, Toast.LENGTH_LONG).show()
                swipe.isRefreshing = false
            }
        }
    }
}