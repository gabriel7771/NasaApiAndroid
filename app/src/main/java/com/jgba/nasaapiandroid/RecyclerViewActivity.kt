package com.jgba.nasaapiandroid

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.size
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jgba.nasaapiandroid.model.Item
import com.jgba.nasaapiandroid.model.ModelJSON
import kotlinx.android.synthetic.main.activity_recycler_view.*

class RecyclerViewActivity : AppCompatActivity() {

    private val itemList: MutableList<Item> = mutableListOf()

    private lateinit var myAdapter: SearchAdapter

    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var progressBar: ProgressBar

    lateinit var freeSearch: String
    private var loaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        //Receive the freeSearch variable from MainActivity
        freeSearch = intent.getStringExtra("FreeSearch")

        this.title = """${getString(R.string.results_of_search)} $freeSearch"""
        val toolbar = findViewById(R.id.toolbar_favourite) as androidx.appcompat.widget.Toolbar?
        setSupportActionBar(toolbar)
        toolbar?.subtitle = getString(R.string.search_subtitle)

        progressBar = findViewById(R.id.progressBar) as ProgressBar

        bottomNavigationView = findViewById(R.id.bottomNavigation)
        setIconChecked()

        bottomNavigationView.setOnNavigationItemSelectedListener {item ->
            when (item.itemId){
                R.id.searchNav -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    false }
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

        //Adapter
        myAdapter = SearchAdapter(applicationContext,itemList)

        //Recyclerview
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addItemDecoration(DividerItemDecoration(this, OrientationHelper.VERTICAL))
        recycler_view.adapter = myAdapter

        httpRequest()
        /*
        //Networking --> request de HTTP
        AndroidNetworking.initialize(this)
        //API root and the parameter q which allows free search based on it input String
        AndroidNetworking.get("https://images-api.nasa.gov/search?q=$freeSearch&media_type=image")
            .build()
            .getAsObject( ModelJSON::class.java, object : ParsedRequestListener<ModelJSON>{
                override fun onResponse(response: ModelJSON) {
                    //Add items objects from json response to itemList
                    recycler_view.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    itemList.addAll(response.collection.items)
                    myAdapter.notifyDataSetChanged()
                }

                override fun onError(anError: ANError?) {
                    Log.d("ERROR : ", anError.toString())
                    Toast.makeText(applicationContext, resources.getString(R.string.no_internet), Toast.LENGTH_LONG).show()
                }
            })
*/
    }
    override fun onResume() {
        super.onResume()
        setIconChecked()

        if (!loaded){
            httpRequest()
        }
    }
    private fun httpRequest(){
        //Networking --> request de HTTP
        AndroidNetworking.initialize(this)
        //API root and the parameter q which allows free search based on it input String
        AndroidNetworking.get("https://images-api.nasa.gov/search?q=$freeSearch&media_type=image")
            .build()
            .getAsObject( ModelJSON::class.java, object : ParsedRequestListener<ModelJSON>{
                override fun onResponse(response: ModelJSON) {
                    //Add items objects from json response to itemList
                    recycler_view.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    itemList.addAll(response.collection.items)
                    myAdapter.notifyDataSetChanged()
                    loaded = true
                }

                override fun onError(anError: ANError?) {
                    Log.d("ERROR : ", anError.toString())
                    Toast.makeText(applicationContext, resources.getString(R.string.no_internet), Toast.LENGTH_LONG).show()
                    loaded = false
                }
            })
    }
    private fun setIconChecked(){
        val menu: Menu = bottomNavigationView.menu
        var menuItem: MenuItem
        var i=0
        while (i<menu.size){
            menuItem = menu.getItem(i)
            menuItem.isChecked = false
            menuItem.isCheckable = false
            i++
        }
    }
}