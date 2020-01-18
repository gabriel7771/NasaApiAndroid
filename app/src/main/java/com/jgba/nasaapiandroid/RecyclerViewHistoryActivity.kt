package com.jgba.nasaapiandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jgba.nasaapiandroid.Database.DBHandler
import kotlinx.android.synthetic.main.activity_recycler_view_history.*

class RecyclerViewHistoryActivity : AppCompatActivity() {

    companion object {
        lateinit var dbHandler: DBHandler
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view_history)

        this.title = getString(R.string.history)
        val toolbar = findViewById(R.id.toolbar_history) as androidx.appcompat.widget.Toolbar?
        setSupportActionBar(toolbar)
        toolbar?.subtitle = getString(R.string.history_subtitle)

        dbHandler = DBHandler(this,null,null, 1)

        viewHistory()
    }
    private fun viewHistory(){
        val historyList = dbHandler.getHistory(this)
        val adapter = HistoryAdapter(this, historyList)
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view_history)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        viewHistory()
        super.onResume()
    }

}
