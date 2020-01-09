package com.example.softwaredesign_lab4.tasks

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.softwaredesign_lab4.model.Post
import com.example.softwaredesign_lab4.viewmodel.PostListViewModel
import org.w3c.dom.Document
import java.lang.Exception
import java.lang.Integer.min
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory

class RssLoadingTask(
    private val model: PostListViewModel,
    private val mSwipeLayout: SwipeRefreshLayout,
    private val context: Context
) :
    AsyncTask<String, Void, Boolean>() {

    override fun doInBackground(vararg url: String): Boolean {
        return try {
            Looper.prepare()
            getPosts(getXmlFromUrl(url[0]))
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
            Toast.makeText(context, "Something wrong.", Toast.LENGTH_LONG).show()
    }

    private fun getPosts(document: Document?) {
        if (document == null)
            return

        val root = document.documentElement
        val channel = root.childNodes.item(1) ?: root.childNodes.item(0)
        val items = channel.childNodes

        val posts = mutableListOf<Post>()

        for (i in 0 until min(items.length, 10)) {
            val post = Post()
            val curChild = items.item(i)
            Log.println(Log.ERROR, "Tiger", curChild.nodeName)
            if (curChild.nodeName.equals("item", true)) {
                val itemChilds = curChild.childNodes
                for (j in 0 until itemChilds.length) {
                    val curNode = itemChilds.item(j)
                    when (curNode.nodeName.toLowerCase(Locale.ENGLISH)) {
                        "title" -> {
                            post.title = curNode.textContent
                        }
                        "description" -> {
                            val strList = curNode.textContent.split(">")
                            val desc = StringBuilder()
                            for (value in strList) {
                                if (!value.startsWith("<") && value.length > 6 && !value.contains("<img")) {
                                    val subs = value.split('<')
                                    for (sub in subs) {
                                        if (sub.length > 3 && !sub.contains("a href") && !sub.contains(
                                                "br clear"
                                            )
                                        ) {
                                            desc.append(sub)
                                        }
                                    }
                                }
                            }
                            post.content = desc.toString()
                        }
                        "pubDate" -> {
                            post.date = try {
                                val date = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.US)
                                val date1 = SimpleDateFormat("E, d MMM yyyy HH:mm:ss Z", Locale.US)
                                date.format(Objects.requireNonNull(date1.parse(curNode.textContent) as Date))
                            } catch (e: ParseException) {
                                e.printStackTrace()
                                curNode.textContent
                            }
                        }
                        "link" -> {
                            post.link = curNode.textContent
                        }
                        "media:thumbnail", "enclosure" -> {
                            post.image = curNode.attributes.item(0).textContent
                        }
                    }
                }
                posts.add(post)
            }
        }
//        storage.deletePosts()
//        for (i in 0 until min(10, posts.count())) {
//            val img = posts[i].image
//            try {
//                val url = URL(img)
//                val bitmap = BitmapFactory.decodeStream(url.openStream())
//                val baos = ByteArrayOutputStream()
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos)
//                val bytes = baos.toByteArray()
//                posts[i].cachedImage = Base64.encodeToString(bytes, Base64.DEFAULT)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//        storage.insertPosts(posts.take(10))
//        return posts
        model.setPosts(posts)
    }

    private fun getXmlFromUrl(address: String): Document? {
        return try {
            val url = URL(
                if (!address.startsWith("http://") && !address.startsWith("https://"))
                    "http://$address" else address
            )
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            val inputStream = connection.inputStream
            val builderFactory = DocumentBuilderFactory.newInstance()
            val builder = builderFactory.newDocumentBuilder()
            builder.parse(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Can't connect to$address.", Toast.LENGTH_LONG).show()
            null
        }
    }
}