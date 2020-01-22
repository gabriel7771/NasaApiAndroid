package com.jgba.nasaapiandroid.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.webkit.WebHistoryItem
import android.widget.Toast
import com.jgba.nasaapiandroid.Favourites
import com.jgba.nasaapiandroid.History
import com.jgba.nasaapiandroid.R
import android.content.ContentValues.TAG


class DBHandler (context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION){
    companion object {
        private val DATABASE_NAME = "Data.db"
        private val DATABASE_VERSION = 2

        //model of table history
        val HISTORY_TABLE_NAME = "History"
        val COLUMN_HISTORY_ID = "historyid"
        val COLUMN_HISTORY_SEARCH = "historysearch"
        val COLUMN_HISTORY_DATE = "historydate"

        //model of table favourites
        val FAVOURITE_TABLE_NAME = "Favourite"
        val COLUMN_FAVOURITE_ID = "favid"
        val COLUMN_FAVOURITE_NASAID = "favnasaid"
        val COLUMN_FAVOURITE_IMAGE_LINK = "favimagelink"
        val COLUMN_FAVOURITE_TITLE = "favtitle"
        val COLUMN_FAVOURITE_CREATOR = "favcreator"
        val COLUMN_FAVOURITE_DATE = "favdate"
        val COLUMN_FAVOURITE_DESCRIPTION = "favdescription"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_HISTORY_TABLE = ("CREATE TABLE $HISTORY_TABLE_NAME (" +
                "$COLUMN_HISTORY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_HISTORY_SEARCH TEXT," +
                "$COLUMN_HISTORY_DATE TEXT)")
        db?.execSQL(CREATE_HISTORY_TABLE)

        val CREATE_FAVOURITES_TABLE = ("CREATE TABLE $FAVOURITE_TABLE_NAME (" +
                "$COLUMN_FAVOURITE_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_FAVOURITE_NASAID TEXT," +
                "$COLUMN_FAVOURITE_IMAGE_LINK TEXT," +
                "$COLUMN_FAVOURITE_TITLE TEXT," +
                "$COLUMN_FAVOURITE_CREATOR TEXT," +
                "$COLUMN_FAVOURITE_DATE TEXT," +
                "$COLUMN_FAVOURITE_DESCRIPTION TEXT)")
        db?.execSQL(CREATE_FAVOURITES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion<2) {
            /*val CREATE_FAVOURITES_TABLE = ("CREATE TABLE $FAVOURITE_TABLE_NAME (" +
                    "$COLUMN_FAVOURITE_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_FAVOURITE_NASAID TEXT," +
                    "$COLUMN_FAVOURITE_IMAGE_LINK TEXT," +
                    "$COLUMN_FAVOURITE_TITLE TEXT," +
                    "$COLUMN_FAVOURITE_CREATOR TEXT," +
                    "$COLUMN_FAVOURITE_DATE TEXT," +
                    "$COLUMN_FAVOURITE_DESCRIPTION TEXT)")
            db?.execSQL(CREATE_FAVOURITES_TABLE)*/
        }
    }

    fun getHistory(context: Context) : ArrayList<History>{
        val query = "Select * From $HISTORY_TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query,null)
        val historyList = ArrayList<History>()

        if (cursor.count == 0){
            Toast.makeText(context, context.getString(R.string.no_searches_found),Toast.LENGTH_SHORT).show()
        }
        else{
            while(cursor.moveToNext()){
                val history = History()
                history.historyID = cursor.getInt(cursor.getColumnIndex(COLUMN_HISTORY_ID))
                history.historySearch = cursor.getString(cursor.getColumnIndex(COLUMN_HISTORY_SEARCH))
                history.historyDate = cursor.getString(cursor.getColumnIndex(COLUMN_HISTORY_DATE))
                historyList.add(history)
                historyList.reverse() //Used reverse method to show history from first to last search
            }
        }
        cursor.close()
        db.close()
        return historyList
    }
    //Adding history to database
    fun addHistory(context: Context, history: History){
        val  values = ContentValues()
        values.put(COLUMN_HISTORY_SEARCH, history.historySearch)
        values.put(COLUMN_HISTORY_DATE, history.historyDate)
        val db: SQLiteDatabase = this.writableDatabase
        try{
            db.insert(HISTORY_TABLE_NAME, null, values)
        }catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
        db.close()
    }
    //Delete history from database
    fun deleteHistory(historyID: Int): Boolean {
        val query = "Delete From $HISTORY_TABLE_NAME where $COLUMN_HISTORY_ID = $historyID"
        val db: SQLiteDatabase = this.writableDatabase
        var successDelete: Boolean = false
        try{
            val cursor: Unit = db.execSQL(query)
            successDelete = true
        }
        catch (e: Exception){
            Log.e(ContentValues.TAG, e.toString())
        }
        db.close()
        return successDelete
    }
    //Delete all history from database
    fun deleteAllHistory(): Boolean{
        val query = "Delete From $HISTORY_TABLE_NAME"
        val db: SQLiteDatabase = this.writableDatabase
        var successDelete = false
        try{
            val cursor: Unit = db.execSQL(query)
            successDelete = true
        }
        catch (e: Exception){
            Log.e(ContentValues.TAG, e.toString())
        }
        db.close()
        return successDelete
    }

    fun getFavourites(context: Context) : ArrayList<Favourites>{
        val query = "Select * From $FAVOURITE_TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query,null)
        val favouritesList = ArrayList<Favourites>()

        if (cursor.count == 0){
            Toast.makeText(context, context.getString(R.string.no_searches_found),Toast.LENGTH_SHORT).show()
        }
        else{
                while (cursor.moveToNext()) {
                    val favourites = Favourites()

                    favourites.favouritesID =
                        cursor.getInt(cursor.getColumnIndex(COLUMN_FAVOURITE_ID))
                    favourites.favouritesNasaID =
                        cursor.getString(cursor.getColumnIndex(COLUMN_FAVOURITE_NASAID))
                    favourites.favouritesImageLink =
                        cursor.getString(cursor.getColumnIndex(COLUMN_FAVOURITE_IMAGE_LINK))
                    favourites.favouritesTitle =
                        cursor.getString(cursor.getColumnIndex(COLUMN_FAVOURITE_TITLE))
                    favourites.favouritesCreator =
                        cursor.getString(cursor.getColumnIndex(COLUMN_FAVOURITE_CREATOR))
                    favourites.favouritesDate =
                        cursor.getString(cursor.getColumnIndex(COLUMN_FAVOURITE_DATE))
                    favourites.favouritesDescription =
                        cursor.getString(cursor.getColumnIndex(COLUMN_FAVOURITE_DESCRIPTION))

                    favouritesList.add(favourites)
                    favouritesList.reverse()
                }
        }
        cursor.close()
        db.close()
        return favouritesList
    }
    //Adding favourite to database
    fun addFavourite(context: Context, favourite: Favourites): Boolean{
        val  values = ContentValues()
        var successAdd = false
        values.put(COLUMN_FAVOURITE_NASAID, favourite.favouritesNasaID)
        values.put(COLUMN_FAVOURITE_IMAGE_LINK, favourite.favouritesImageLink)
        values.put(COLUMN_FAVOURITE_TITLE, favourite.favouritesTitle)
        values.put(COLUMN_FAVOURITE_CREATOR, favourite.favouritesCreator)
        values.put(COLUMN_FAVOURITE_DATE, favourite.favouritesDate)
        values.put(COLUMN_FAVOURITE_DESCRIPTION, favourite.favouritesDescription)
        val db: SQLiteDatabase = this.writableDatabase
        try{
            db.insert(FAVOURITE_TABLE_NAME, null, values)
            successAdd = true
        }catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
        db.close()
        return successAdd
    }
    //Delete favourite from database
    fun deleteFavourite(favouriteID: Int): Boolean {
        val query = "Delete From $FAVOURITE_TABLE_NAME where $COLUMN_FAVOURITE_ID = $favouriteID"
        val db: SQLiteDatabase = this.writableDatabase
        var successDelete = false
        try{
            val cursor: Unit = db.execSQL(query)
            successDelete = true
        }
        catch (e: Exception){
            Log.e(TAG, e.toString())
        }
        db.close()
        return successDelete
    }
    fun customDelete(TableName: String, dbField: String, fieldValue: String): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val query = "Delete From $TableName where $dbField =?"
        var successDelete = false
        try{
            val cursor: Unit = db.execSQL(query, arrayOf(fieldValue))
            successDelete = true
        }
        catch (e: Exception){
            Log.e(TAG, e.toString())
        }
        db.close()
        return successDelete
    }
    //Delete all favourites from database
    fun deleteAllFavourites(): Boolean{
        val query = "Delete From $FAVOURITE_TABLE_NAME"
        val db: SQLiteDatabase = this.writableDatabase
        var successDelete = false
        try{
            val cursor: Unit = db.execSQL(query)
            successDelete = true
        }
        catch (e: Exception){
            Log.e(ContentValues.TAG, e.toString())
        }
        db.close()
        return successDelete
    }
    fun checkIfExistsInDB(TableName: String, dbField: String, fieldValue: String): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val query = "Select * from $TableName where $dbField =?"

        val cursor = db.rawQuery(query, arrayOf(fieldValue))
        var hasObject = false
        if (cursor.moveToFirst()) {
            hasObject = true

            var count = 0
            while (cursor.moveToNext()) {
                count++
            }
        }
        cursor.close()
        db.close()
        return hasObject
    }
}