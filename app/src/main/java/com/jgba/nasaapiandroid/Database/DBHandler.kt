package com.jgba.nasaapiandroid.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.webkit.WebHistoryItem
import android.widget.Toast
import com.jgba.nasaapiandroid.History
import com.jgba.nasaapiandroid.R

class DBHandler (context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION){
    companion object {
        private val DATABASE_NAME = "Data.db"
        private val DATABASE_VERSION = 1

        //model of table history
        val HISTORY_TABLE_NAME = "History"
        val COLUMN_HISTORY_ID = "historyid"
        val COLUMN_HISTORY_SEARCH = "historysearch"
        val COLUMN_HISTORY_DATE = "historydate"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_HISTORY_TABLE = ("CREATE TABLE $HISTORY_TABLE_NAME (" +
                "$COLUMN_HISTORY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_HISTORY_SEARCH TEXT," +
                "$COLUMN_HISTORY_DATE TEXT)")
        db?.execSQL(CREATE_HISTORY_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
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
}