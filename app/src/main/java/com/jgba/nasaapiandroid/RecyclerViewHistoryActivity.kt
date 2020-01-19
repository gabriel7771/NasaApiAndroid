package com.jgba.nasaapiandroid

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
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
    //TOOLBAR
    override fun onCreateOptionsMenu (menu: Menu): Boolean{
        menuInflater.inflate(R.menu.menu_toolbar_history,menu)

        return true
    }
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean{
        when(menuItem.itemId){
            R.id.deleteAll -> alertDialog()

        }
        return true
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

    private fun alertDialog(){
        val context = this

        var alertDialog = AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.warning))
            .setMessage(context.getString(R.string.warning_text_all))
            .setPositiveButton(context.getString(R.string.yes), DialogInterface.OnClickListener { dialog, which ->
                if (RecyclerViewHistoryActivity.dbHandler.deleteAllHistory()){
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
}
