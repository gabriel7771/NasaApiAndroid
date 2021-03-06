package com.jgba.nasaapiandroid

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jgba.nasaapiandroid.Database.DBHandler
import com.jgba.nasaapiandroid.Graphics.Graphics
import kotlinx.android.synthetic.main.activity_recycler_view_history.*

class RecyclerViewHistoryActivity : AppCompatActivity() {

    /*companion object {
        lateinit var dbHandler: DBHandler
    }*/

    lateinit var bottomNavigationView: BottomNavigationView

    var historyList = ArrayList<History>()
    lateinit var adapter: HistoryAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var dbHandler: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view_history)

        this.title = getString(R.string.history)
        val toolbar = findViewById(R.id.toolbar_history) as androidx.appcompat.widget.Toolbar?
        setSupportActionBar(toolbar)
        toolbar?.subtitle = getString(R.string.history_subtitle)

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        setIconChecked()

        bottomNavigationView.setOnNavigationItemSelectedListener {item ->
            when (item.itemId){
                R.id.searchNav -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                    finish()
                    false
                }
                R.id.favouritesNav -> {
                    val intent = Intent(this, FavouritesActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                    finish()
                    false
                }
                R.id.historyNav -> {
                    false
                }
                else -> false
            }
            true
        }

        dbHandler = DBHandler(this,null,null, 1)

        historyList = java.util.ArrayList<History>()
        //recyclerView = findViewById(R.id.recycler_view_history)

        viewHistory()

        //adapter = HistoryAdapter(this, historyList)
        //recyclewView.adapter = adapter
    }
    //TOOLBAR
    override fun onCreateOptionsMenu (menu: Menu): Boolean{
        menuInflater.inflate(R.menu.menu_toolbar_history,menu)

        //Search filter
        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.getFilter().filter(newText)
                return false
            }
        })
        return true
    }
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean{
        when(menuItem.itemId){
            R.id.deleteAll -> alertDialog()

        }
        return true
    }

    private fun viewHistory(){

        historyList = dbHandler.getHistory(this)
        adapter = HistoryAdapter(this, historyList)
        recyclerView = findViewById(R.id.recycler_view_history)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        viewHistory()
        setIconChecked()
        super.onResume()
    }

    private fun alertDialog(){
        val context = this

        var alertDialog = AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.warning))
            .setMessage(context.getString(R.string.warning_text_all))
            .setPositiveButton(context.getString(R.string.yes), DialogInterface.OnClickListener { dialog, which ->
                if (dbHandler.deleteAllHistory()){
                    dbHandler.deleteAllHistory()
                    //Refresh activity to see changes
                    recreate()
                    Toast.makeText(context, context.getString(R.string.deleted_all),
                        Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context, context.getString(R.string.error_deleting), Toast.LENGTH_SHORT).show()
                }
            })
            .setNegativeButton(context.getString(R.string.no), DialogInterface.OnClickListener { dialog, which ->  })
            .setIcon(R.drawable.ic_warning_red_24dp)
            .show()
    }
    private fun setIconChecked(){
        val menu: Menu = bottomNavigationView.menu
        val menuItem: MenuItem = menu.getItem(2)
        menuItem.isChecked = true
    }
}