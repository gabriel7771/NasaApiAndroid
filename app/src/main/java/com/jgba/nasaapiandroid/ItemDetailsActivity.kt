package com.jgba.nasaapiandroid

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.webkit.URLUtil
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.core.view.size
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jgba.nasaapiandroid.Database.DBHandler
import com.jgba.nasaapiandroid.Database.DBHandler.Companion.COLUMN_FAVOURITE_NASAID
import com.jgba.nasaapiandroid.Database.DBHandler.Companion.FAVOURITE_TABLE_NAME
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_item_details.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ItemDetailsActivity : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView

    lateinit var dbHandler: DBHandler

    var STORAGE_PERMISSION_CODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        dbHandler = DBHandler(this,null,null, 1)

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

        if(dbHandler.checkIfExistsInDB(FAVOURITE_TABLE_NAME, COLUMN_FAVOURITE_NASAID, nasaID)){
            addFavouriteButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_star_yellow_24dp,0 )
            addFavouriteButton.text = getString(R.string.added_to_favourites)
        }

        addFavouriteButton.setOnClickListener {
            if (!dbHandler.checkIfExistsInDB(FAVOURITE_TABLE_NAME, COLUMN_FAVOURITE_NASAID, nasaID))
            {
                val f = Favourites()
                f.favouritesTitle = title
                f.favouritesImageLink = imageLink
                f.favouritesCreator = secondaryCreator
                f.favouritesDate = dateCreated
                f.favouritesDescription = description
                f.favouritesNasaID = nasaID

                if (dbHandler.addFavourite(this, f)) {
                    addFavouriteButton.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_star_yellow_24dp,
                        0
                    )
                    addFavouriteButton.text = getString(R.string.added_to_favourites)
                    Toast.makeText(this, getString(R.string.added_to_favourites), Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this, getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                        .show()
                }
            }
            else{
                if(dbHandler.customDelete(FAVOURITE_TABLE_NAME, COLUMN_FAVOURITE_NASAID, nasaID)){
                    addFavouriteButton.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_star_border_yellow_24dp,
                        0
                    )
                    addFavouriteButton.text = getString(R.string.add_to_favourites)
                    Toast.makeText(this, getString(R.string.removed_favourite), Toast.LENGTH_SHORT)
                        .show()
                }
                else{
                    Toast.makeText(this, getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        //Setting OnLongClickListener to image view
        imageView.setOnLongClickListener {
            alertDialogImageOptions()
            true
        }
    }
    override fun onResume() {
        super.onResume()
        setIconChecked()
    }
    override fun onRestart() {
        super.onRestart()
        recreate()
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
    private fun alertDialogImageOptions(){
        val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
        builder.setTitle(getString(R.string.alert_dialog_image_title))

        builder.setItems(arrayOf<CharSequence>(
            getString(R.string.save_image),
            getString(R.string.share_image)
        ),
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    0 -> {
                        //Save on local storage
                        saveImage()
                    }
                    1 ->{
                        //Share
                        shareImage()
                    }
                }
            })
        builder.create()
        builder.show()
    }
    private fun saveImage(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            val bitmap = (imageView.getDrawable() as BitmapDrawable).bitmap

            val cr = contentResolver
            val title = "Nasa Image"
            val description = "Nasa image saved from Nasa Image Browser app"
            val savedURL = MediaStore.Images.Media
                .insertImage(cr, bitmap, title, description)

            Toast.makeText(this, getString(R.string.saved_to)+" $savedURL", Toast.LENGTH_LONG).show()
        } else {
            requestStoragePermission()
        }
    }
    private fun shareImage(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val share = Intent(Intent.ACTION_SEND)
            share.type = "image/jpeg"
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(
                contentResolver,
                bitmap, "Nasa Image", null
            )
            val imageUri = Uri.parse(path)
            share.putExtra(Intent.EXTRA_STREAM, imageUri)
            startActivity(Intent.createChooser(share, "Select"))
        }
        else{
            requestStoragePermission()
        }
    }
    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {
           /*
            AlertDialog.Builder(this)
                .setTitle(resources.getString(R.string.permission_needed))
                .setMessage(resources.getString(R.string.permission_message))
                .setPositiveButton("Ok")
                { dialogInterface, i ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        STORAGE_PERMISSION_CODE
                    )
                }
                .setNegativeButton(resources.getString(R.string.cancel)) { dialogInterface, i -> dialogInterface.dismiss() }
                .create().show()
                */
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
        else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.permission_granted),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
