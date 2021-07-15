package com.example.fetchexercise

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.*

const val DATABASE_NAME = "fetch.db"
const val DATABASE_VERSION = 1
const val TABLE_NAME = "items"
const val COLUMN_ID = "id"
const val COLUMN_LISTID = "listId"
const val COLUMN_NAME = "name"

class MyDatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER, $COLUMN_LISTID INTEGER, $COLUMN_NAME TEXT NOT NULL);"
        /*val query = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER, " + COLUMN_LISTID + " INTEGER, " + COLUMN_NAME + " TEXT);"*/
        db?.execSQL(query)
        Log.i("database onCreate()", "table is created")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Send a sql query and return List of Items to be displayed
    fun getItems(): List<Item> {
        val itemsList: MutableList<Item> = ArrayList()
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_NAME != 'null' AND $COLUMN_NAME != '' ORDER BY $COLUMN_LISTID, $COLUMN_NAME;"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val item = Item(cursor.getInt(0), cursor.getInt(1), cursor.getString(2))
                itemsList.add(item)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return itemsList
    }
}