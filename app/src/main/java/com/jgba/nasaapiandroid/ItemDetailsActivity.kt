package com.jgba.nasaapiandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.size
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_item_details.*

class ItemDetailsActivity : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        bottomNavigationView = findViewById(R.id.bottomNavigation)
        setIconChecked()

        bottomNavigationView.setOnNavigationItemSelectedListener {item ->
            when (item.itemId){
                R.id.searchNav -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    false }
                R.id.favouritesNav -> {
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

        val imageLink = intent.getStringExtra("ImageLink")
        val title = intent.getStringExtra("Title")
        val secondaryCreator = intent.getStringExtra("SecondaryCreator")
        val dateCreated = intent.getStringExtra("DateCreated")
        val description = intent.getStringExtra("Description")
        val nasaID = intent.getStringExtra("NasaID")

        Picasso.get().load(imageLink).into(imageView)
        titleText.text = "$title"
        secondaryCreatorText.text = "$secondaryCreator"
        dateText.text = "$dateCreated"
        descriptionText.text = "$description"
    }
    override fun onResume() {
        super.onResume()
        setIconChecked()
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
