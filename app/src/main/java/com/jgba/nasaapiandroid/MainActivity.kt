package com.jgba.nasaapiandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.jgba.nasaapiandroid.Database.DBHandler
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import org.droidparts.widget.ClearableEditText
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import android.view.inputmethod.EditorInfo
import android.view.KeyEvent.KEYCODE_ENTER
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.KeyEvent


class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var dbHandler: DBHandler
    }

    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchEditText = findViewById<ClearableEditText>(R.id.searchEditText)
        val searchButton = findViewById<Button>(R.id.searchButton)

        dbHandler = DBHandler(this,null,null, 1)

        bottomNavigationView = findViewById(R.id.bottomNavigation)
        setIconChecked()

        bottomNavigationView.setOnNavigationItemSelectedListener {item ->
            when (item.itemId){
                R.id.searchNav -> { false }
                R.id.favouritesNav -> {
                    val intent = Intent(this, FavouritesActivity::class.java)
                    startActivity(intent)
                    false
                }
                R.id.historyNav -> {
                    val intent = Intent(this, RecyclerViewHistoryActivity::class.java)
                    startActivity(intent)
                    false
                }
                else -> false
            }
            true
        }

        searchButton.setOnClickListener{
            onStartSearch()
        }

        searchEditText.setOnEditorActionListener { _, actionId, event ->
            if (event != null && event!!.keyCode === KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                onStartSearch()
            }
            false
        }
    }
    fun onStartSearch(){
        //freeSearch is the keyword to input in the http request of the NASA API
        val freeSearch = searchEditText.text.toString()

        //Adding data to the history database
        addHistoryToDatabase(freeSearch, getCurrentDateTime())

        //Opening new activity
        val intent = Intent(this, RecyclerViewActivity::class.java)
        //Send the keyword to the RecyclerViewActivity
        intent.putExtra("FreeSearch",freeSearch)
        startActivity(intent)
    }
    fun addHistoryToDatabase(search: String, date: Date){
        val history = History()
        history.historySearch = search
        history.historyDate = date.toString()

        //dbHandler = DBHandler(this,null,null, 1)

        dbHandler.addHistory(this,history)
    }
    override fun onResume() {
        super.onResume()
        setIconChecked()
    }
    private fun setIconChecked(){
        val menu: Menu = bottomNavigationView.menu
        val menuItem: MenuItem = menu.getItem(0)
        menuItem.isChecked = true
    }
    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}
