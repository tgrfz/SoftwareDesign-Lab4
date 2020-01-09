package com.example.softwaredesign_lab4

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProviders
import com.example.softwaredesign_lab4.postfragment.PostFragment
import com.example.softwaredesign_lab4.viewmodel.PostListViewModel
import com.prof.rssparser.Article
import kotlinx.android.synthetic.main.activity_main.*

private const val MY_SETTINGS = "my_settings"
private const val CURRENT_URL = "curUrl"

class MainActivity : AppCompatActivity(), PostFragment.OnListFragmentInteractionListener {

    private lateinit var sharedPref: SharedPreferences
    private var curUrl: String? = null
    private lateinit var model: PostListViewModel
    private val autoUrl = "https://www.androidauthority.com/feed"

    private val isNetworkAvailable: Boolean
        get() {
            val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = ViewModelProviders.of(this)[PostListViewModel::class.java]

        val toolbar = findViewById<Toolbar>(R.id.app_bar_main)
        setSupportActionBar(toolbar)

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            loadPosts()
        }

        sharedPref = getSharedPreferences(this.packageName + MY_SETTINGS, Context.MODE_PRIVATE)
        curUrl = sharedPref.getString(CURRENT_URL, null)
        curUrl?.let {
            swipeRefreshLayout.isRefreshing = true
            loadPosts()
        } ?: urlAlert()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        urlAlert()
        return true
    }

    private fun urlAlert() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Enter URL")
        val input = EditText(this)
        input.isFocusableInTouchMode = true
        input.requestFocus()
        curUrl?.let { input.setText(it) }
        val alert = builder.setView(input)
            .setCancelable(false)
            .setPositiveButton("Load") { _, _ ->
                val url = input.text.toString()
                curUrl = url
                sharedPref.edit { putString(CURRENT_URL, url) }
                swipeRefreshLayout.isRefreshing = true
                loadPosts()
            }
            .setNeutralButton("Auto", null)
            .create()
        alert.setOnShowListener {
            val btn = (it as AlertDialog).getButton(AlertDialog.BUTTON_NEUTRAL)
            btn.setOnClickListener {
                input.setText(autoUrl)
            }
        }
        alert.show()
    }

    private fun loadPosts() {
        if (!isNetworkAvailable) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show()
            model.loadFromCache(swipeRefreshLayout)
            //TODO change bar title
        } else {
            model.fetchFeed(curUrl ?: "", swipeRefreshLayout, supportActionBar)
        }
    }

    override fun onListFragmentInteraction(item: Article) {
        //TODO on post click
    }
}
