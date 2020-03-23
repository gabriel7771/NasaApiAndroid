package com.jgba.nasaapiandroid

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jgba.nasaapiandroid.Database.DBHandler
import com.jgba.nasaapiandroid.Graphics.Graphics

class FavouritesActivity : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView

    var favouritesList = ArrayList<Favourites>()
    lateinit var adapter: FavouritesAdapter
    lateinit var recyclewView: RecyclerView
    lateinit var dbHandler: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        this.title = getString(R.string.favourites)
        val toolbar = findViewById(R.id.toolbar_favourite) as androidx.appcompat.widget.Toolbar?
        setSupportActionBar(toolbar)
        toolbar?.subtitle = getString(R.string.search_subtitle)

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
                    false
                }
                R.id.historyNav -> {
                    val intent = Intent(this, RecyclerViewHistoryActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                    finish()
                    false
                }
                else -> false
            }
            true
        }

        dbHandler = DBHandler(this,null,null, 2)

        favouritesList = java.util.ArrayList<Favourites>()
        recyclewView = findViewById(R.id.recycler_view_favourites)

        viewFavourites()

        adapter = FavouritesAdapter(this, favouritesList)
        recyclewView.adapter = adapter
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

    private fun viewFavourites(){
        favouritesList = dbHandler.getFavourites(this)
        adapter = FavouritesAdapter(this, favouritesList)
        recyclewView = findViewById(R.id.recycler_view_favourites)
        recyclewView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager
        recyclewView.adapter = adapter
    }

    override fun onResume() {
        viewFavourites()
        setIconChecked()
        super.onResume()
    }

    private fun alertDialog(){
        val context = this

        var alertDialog = AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.warning))
            .setMessage(context.getString(R.string.warning_text_delete_all_favourites))
            .setPositiveButton(context.getString(R.string.yes), DialogInterface.OnClickListener { dialog, which ->
                if (dbHandler.deleteAllFavourites()){
                    dbHandler.deleteAllFavourites()
                    //Refresh activity to see changes
                    recreate()
                    Toast.makeText(context, context.getString(R.string.deleted_all_favourites),
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
        val menuItem: MenuItem = menu.getItem(1)
        menuItem.isChecked = true
    }
}
