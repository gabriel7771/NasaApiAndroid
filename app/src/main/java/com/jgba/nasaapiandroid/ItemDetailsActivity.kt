package com.jgba.nasaapiandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_item_details.*

class ItemDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        val imageLink = intent.getStringExtra("ImageLink")
        val title = intent.getStringExtra("Title")
        val secondaryCreator = intent.getStringExtra("SecondaryCreator")
        val dateCreated = intent.getStringExtra("DateCreated")
        val description = intent.getStringExtra("Description")

        Picasso.get().load(imageLink).into(imageView)
        titleText.text = "$title"
        secondaryCreatorText.text = "$secondaryCreator"
        dateText.text = "$dateCreated"
        descriptionText.text = "$description"
    }
}
