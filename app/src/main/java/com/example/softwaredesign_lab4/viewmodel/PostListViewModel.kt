package com.example.softwaredesign_lab4.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.softwaredesign_lab4.App
import com.example.softwaredesign_lab4.model.Post


class PostListViewModel(application: Application) : AndroidViewModel(application) {
    private val storage by lazy { getApplication<App>().storage }
    private val posts: MutableLiveData<List<Post>> = MutableLiveData()

    fun getPosts(): LiveData<List<Post>> {
        return posts
    }

    fun setPosts(newPosts: List<Post>) {
        posts.postValue(newPosts)
        storage.deletePosts()
        storage.insertPosts(newPosts.take(10))
    }

    fun loadPosts() {
        posts.value = storage.getPosts()
    }
}