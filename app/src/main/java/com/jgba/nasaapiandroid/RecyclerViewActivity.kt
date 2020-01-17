package com.jgba.nasaapiandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.jgba.nasaapiandroid.model.Item
import com.jgba.nasaapiandroid.model.ModelJSON
import kotlinx.android.synthetic.main.activity_recycler_view.*

class RecyclerViewActivity : AppCompatActivity() {

    private val itemList: MutableList<Item> = mutableListOf()

    private lateinit var myAdapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        //Receive the freeSearch variable from MainActivity
        val freeSearch = intent.getStringExtra("FreeSearch")

        textSearchResult.text =
            """${getString(R.string.results_of_search)} $freeSearch"""

        //Adapter
        myAdapter = MyAdapter(itemList)

        //Recyclerview
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addItemDecoration(DividerItemDecoration(this, OrientationHelper.VERTICAL))
        recycler_view.adapter = myAdapter

        //Networking --> request de HTTP
        AndroidNetworking.initialize(this)
        //API root and the parameter q which allows free search based on it input String
        AndroidNetworking.get("https://images-api.nasa.gov/search?q=$freeSearch&media_type=image")
            .build()
            .getAsObject( ModelJSON::class.java, object : ParsedRequestListener<ModelJSON>{
                override fun onResponse(response: ModelJSON) {
                    //Add items objects from json response to itemList
                    itemList.addAll(response.collection.items)
                    myAdapter.notifyDataSetChanged()
                }

                override fun onError(anError: ANError?) {
                    Log.d("ERROR : ", anError.toString())
                }
            })

    }
}