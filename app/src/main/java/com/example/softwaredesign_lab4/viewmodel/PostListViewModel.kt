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

    fun loadFromCache() {
        coroutineScope.launch(Dispatchers.Main) {
            try {
                articleListLive.postValue(Channel(storage.getChannelTitle(), null, null, null, storage.getPosts().toMutableList()))
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(getApplication(), e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setChannel(channel: Channel) {
        articleListLive.postValue(channel)
        storage.setPosts(channel.articles.take(10))
        storage.saveWebViews(channel.articles.take(10))
        storage.setChannelTitle(channel.title)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun fetchFeed(url: String) {
        coroutineScope.launch(Dispatchers.Main) {
            try {
                val parser = Parser()
                val articleList = parser.getChannel(url)
                setChannel(articleList)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(getApplication(), e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}