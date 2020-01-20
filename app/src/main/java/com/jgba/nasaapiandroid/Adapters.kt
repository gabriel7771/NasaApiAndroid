package com.jgba.nasaapiandroid

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
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

        var secondaryCreator = "${item.data[0].secondaryCreator}"
        if (secondaryCreator == "null"){
            secondaryCreator = context.getString(R.string.unknown_creator)
        }

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
    //listHistoryFull copy values from historyList to filter
    var listHistoryFull = ArrayList<History>(historyList)

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

            AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.warning))
                .setMessage(context.getString(R.string.warning_text_1)+" ${history.historySearch} " + context.getString(R.string.warning_text_2))
                .setPositiveButton(context.getString(R.string.yes), DialogInterface.OnClickListener { dialog, which ->
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
                .setNegativeButton(context.getString(R.string.no), DialogInterface.OnClickListener { dialog, which ->  })
                .setIcon(R.drawable.ic_warning_red_24dp)
                .show()
        }

        holder.itemView.setOnClickListener{
            val intent = Intent(context, RecyclerViewActivity::class.java)
            intent.putExtra("FreeSearch",history.historySearch)
            context.startActivity(intent)
        }
    }

    fun getFilter(): Filter {
        return historyFilter
    }
    private val historyFilter = object : Filter() {
        override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
            val filteredList = java.util.ArrayList<History>()

            if (charSequence == null || charSequence.length == 0) {
                filteredList.addAll(listHistoryFull)
            }
            else
            {
                val filterPattern = charSequence.toString().toLowerCase().trim { it <= ' ' }
                for (item in listHistoryFull) {
                    if (item.historySearch.toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(item)
                    }
                }
            }
            val filterResults = Filter.FilterResults()
            filterResults.values = filteredList

            return filterResults
        }

        override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults)
        {
            historyList.clear()
            historyList.addAll(filterResults.values as List<History>)
            notifyDataSetChanged()
        }
    }
}

