package com.jgba.nasaapiandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val searchButton = findViewById<Button>(R.id.searchButton)

        searchButton.setOnClickListener{
            //freeSearch is the keyword to input in the http request of the NASA API
            val freeSearch = searchEditText.text.toString()
            val intent = Intent(this, RecyclerViewActivity::class.java)
            //Send the keyword to the RecyclerViewActivity
            intent.putExtra("FreeSearch",freeSearch)
            startActivity(intent)
        }
    }
}