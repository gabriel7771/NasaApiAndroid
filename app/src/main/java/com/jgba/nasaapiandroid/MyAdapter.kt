package com.jgba.nasaapiandroid

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
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

        val title = "${item.data[0].title}"
        imageText.text = title

        Picasso.get().load(item.links[0].href).into(nasaImage)

        holder.itemView.setOnClickListener{
            Toast.makeText(context, title, Toast.LENGTH_SHORT).show()
        }
    }
}