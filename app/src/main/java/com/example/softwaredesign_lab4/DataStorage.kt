package com.example.softwaredesign_lab4

import android.content.Context
import androidx.core.content.edit
import com.example.softwaredesign_lab4.model.Post
import org.json.JSONArray
import org.json.JSONObject

private const val PREF_NAME = "tiger"
private const val POSTS_KEY = "notes"

class DataStorage(context: Context) {
    private val manager by lazy {
        context.getSharedPreferences(context.packageName + PREF_NAME, Context.MODE_PRIVATE)
    }

    fun insertPosts(posts: List<Post>) {
        saveString(POSTS_KEY, JSONArray().apply {
            posts.forEach {
                put(JSONObject().apply {
                    put("title", it.title)
                    put("content", it.content)
                    put("date", it.date)
                    put("image", it.image)
                    put("link", it.link)
                })
            }
        }.toString())
    }

    fun getPosts(): List<Post> {
        val postsJson = JSONArray(getString(POSTS_KEY) ?: "[]")
        val posts = mutableListOf<Post>()
        repeat(postsJson.length()) { i ->
            val noteJson = postsJson.getJSONObject(i)
            posts.add(Post(
                noteJson.getString("title"),
                noteJson.getString("content"),
                noteJson.getString("date"),
                noteJson.getString("image"),
                noteJson.getString("link")
            ))
        }
        return posts
    }

    fun deletePosts() {
        saveString(POSTS_KEY, JSONArray().toString())
    }

    private fun getString(key: String, defValue: String? = null) = manager.getString(key, defValue)

    private fun saveString(key: String, value: String?) {
        manager.edit { putString(key, value) }
    }
}