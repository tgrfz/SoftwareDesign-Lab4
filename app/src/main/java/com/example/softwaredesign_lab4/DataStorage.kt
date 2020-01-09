package com.example.softwaredesign_lab4

import android.content.Context
import androidx.core.content.edit
import com.prof.rssparser.Article
import org.json.JSONArray
import org.json.JSONObject

private const val PREF_NAME = "tiger"
private const val POSTS_KEY = "posts"
private const val TITLE_KEY = "channel_title"

class DataStorage(context: Context) {
    private val manager by lazy {
        context.getSharedPreferences(context.packageName + PREF_NAME, Context.MODE_PRIVATE)
    }

    fun insertPosts(posts: List<Article>) {
        saveString(POSTS_KEY, JSONArray().apply {
            posts.forEach {
                put(JSONObject().apply {
                    put("guid", it.guid)
                    put("title", it.title)
                    put("author", it.author)
                    put("link", it.link)
                    put("pubDate", it.pubDate)
                    put("description", it.description ?: it.content)
                    put("image", it.image)
                })
            }
        }.toString())
    }

    fun getPosts(): List<Article> {
        val postsJson = JSONArray(getString(POSTS_KEY) ?: "[]")
        val posts = mutableListOf<Article>()
        repeat(postsJson.length()) { i ->
            val noteJson = postsJson.getJSONObject(i)
            posts.add(
                Article(
                    noteJson.getString("guid"),
                    noteJson.getString("title"),
                    noteJson.getString("author"),
                    noteJson.getString("link"),
                    noteJson.getString("pubDate"),
                    noteJson.getString("description"),
                    null,
                    noteJson.getString("image")
                )
            )
        }
        return posts
    }

    fun getChannelTitle(): String? {
        return getString(TITLE_KEY)
    }

    fun setChannelTitle(title: String?) {
        saveString(TITLE_KEY, title)
    }

    fun deletePosts() {
        saveString(POSTS_KEY, JSONArray().toString())
    }

    private fun getString(key: String, defValue: String? = null) = manager.getString(key, defValue)

    private fun saveString(key: String, value: String?) {
        manager.edit { putString(key, value) }
    }
}