package com.example.softwaredesign_lab4

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import com.example.softwaredesign_lab4.model.Post
import kotlinx.android.synthetic.main.activity_main.*

private const val MY_SETTINGS = "my_settings"
private const val CURRENT_URL = "curUrl"

class MainActivity : AppCompatActivity(), PostFragment.OnListFragmentInteractionListener {

    private lateinit var sharedPref: SharedPreferences
    private var curUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.app_bar_main)
        setSupportActionBar(toolbar)

        swipeRefreshLayout.setOnRefreshListener { loadPosts() }

        sharedPref = getSharedPreferences(this.packageName + MY_SETTINGS, Context.MODE_PRIVATE)
        curUrl = sharedPref.getString(CURRENT_URL, null)
        curUrl?.let {
            supportActionBar?.title = curUrl
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
        val alert = builder.setView(input)
            .setCancelable(false)
            .setPositiveButton("Load") { _, _ ->
                val url = input.text.toString()
                curUrl = url
                sharedPref.edit { putString(CURRENT_URL, url) }
                supportActionBar?.title = url
                loadPosts()
            }
            .setNeutralButton("Auto", null)
            .create()
        alert.setOnShowListener {
            val btn = (it as AlertDialog).getButton(AlertDialog.BUTTON_NEUTRAL)
            btn.setOnClickListener {
                input.setText(R.string.autoUrl)
            }
        }
        alert.show()
    }

    private fun loadPosts() {
        if (!NetworkState.getNetworkStatus(this)) {
            swipeRefreshLayout.isRefreshing = false
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show()
            //TODO CacheLoadingTask
        } else {
            //TODO RSSFeedControl
        }
    }

    override fun onListFragmentInteraction(item: Post?) {
        //TODO
    }
}
