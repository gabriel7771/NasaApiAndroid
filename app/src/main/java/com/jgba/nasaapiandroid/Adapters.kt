package com.jgba.nasaapiandroid

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jgba.nasaapiandroid.model.Item
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.history_item_view_.view.*
import kotlinx.android.synthetic.main.item_view.view.*

//Adapter for RecyclerViewAtivity which contains data from the Nasa API
class SearchAdapter(context: Context, private val itemList: MutableList<Item>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    val context = context

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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
//Adapter for RecyclerViewHistoryActivity which contains the searches history
class HistoryAdapter(context: Context, val historyList: ArrayList<History>): RecyclerView.Adapter<HistoryAdapter.ViewHolder>(){

    val context = context

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val searchedText = itemView.searchedText
        val dateText = itemView.dateText
        val btnDelete = itemView.btnDelete
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): HistoryAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.history_item_view_, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
         return historyList.size
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
        val history: History = historyList[position]
        holder.searchedText.text = history.historySearch
        holder.dateText.text = history.historyDate

        holder.btnDelete.setOnClickListener {
             val historySearch = history.historySearch

            var alertDialog = AlertDialog.Builder(context)
                .setTitle("Warning")
                .setMessage("Do you want to delete ${history.historySearch} from the history?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    if (RecyclerViewHistoryActivity.dbHandler.deleteHistory(history.historyID)){
                        historyList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, historyList.size)
                        Toast.makeText(context, "$historySearch " + context.getString(R.string.deleted),Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(context, context.getString(R.string.error_deleting),Toast.LENGTH_SHORT).show()
                    }
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->  })
                .setIcon(R.drawable.ic_warning_red_24dp)
                .show()
        }

    }
}

