package com.jgba.nasaapiandroid

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.jgba.nasaapiandroid.model.Item
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_view.view.*

class MyAdapter(private val itemList: MutableList<Item>) : RecyclerView.Adapter<MyHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        context = parent.context
        return MyHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent,false))
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = itemList[position]

        val imageText = holder.itemView.image_text
        val nasaImage = holder.itemView.nasa_image

        val secondaryCreator = "${item.data[0].secondaryCreator}"
        val dateCreated = "${item.data[0].dateCreated}"
        val description = "${item.data[0].description}"
        val imageLink = "${item.links[0].href}"

        val title = "${item.data[0].title}"
        imageText.text = title

        Picasso.get().load(imageLink).into(nasaImage)

        holder.itemView.setOnClickListener{
            Toast.makeText(context, title, Toast.LENGTH_SHORT).show()

            val intent = Intent(context, ItemDetailsActivity::class.java)
            //Sending detailed info
            intent.putExtra("ImageLink", imageLink)
            intent.putExtra("Title", title)
            intent.putExtra("SecondaryCreator",secondaryCreator)
            intent.putExtra("DateCreated",dateCreated)
            intent.putExtra("Description",description)
            context.startActivity(intent)

        }
    }
}