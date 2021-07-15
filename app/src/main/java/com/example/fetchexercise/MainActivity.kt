package com.example.fetchexercise

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONArray
import java.io.IOException
import java.util.*

const val URL = "https://fetch-hiring.s3.amazonaws.com/hiring.json"

class MainActivity : AppCompatActivity() {

    private lateinit var myDB: MyDatabaseHelper
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Fetch Exercise"

        findViewById<Button>(R.id.continue_button).setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@MainActivity, ItemsActivity::class.java))
        })

        myDB = MyDatabaseHelper(this@MainActivity)
        val appNetwork = BasicNetwork(HurlStack())
        val appCache = DiskBasedCache(cacheDir, 1024 * 1024)
        requestQueue = RequestQueue(appCache, appNetwork).apply { start() }


        // check if table is empty. if so then get Json data and fill table
        if (checkIfTableEmpty(myDB.readableDatabase)) {
            Log.i("database", "table not empty")
        } else {
            Log.i("database", "table empty")
            getJsonData()
        }
    }


    // create json request and handle the response
    private fun getJsonData() {
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, URL, null, {
                response -> addToDatabase(response)
        }, {
                error -> Log.i("Error", error.toString())
        })
        requestQueue.add(jsonArrayRequest)
    }


    // add json data to local database
    private fun addToDatabase(response: JSONArray){
        val writableDB = myDB.writableDatabase

        try{
            for (i in 0 until response.length()) {
                val item = response.getJSONObject(i)

                val cv = ContentValues()
                cv.put(COLUMN_ID, item.getInt("id"))
                cv.put(COLUMN_LISTID, item.getInt("listId"))
                cv.put(COLUMN_NAME, item.getString("name"))

                val result = writableDB.insert(TABLE_NAME, null, cv)
                if (result == -1L && cv.get(COLUMN_NAME) === null) {
                    Log.i(cv.get(COLUMN_ID).toString(), "Name is NULL")
                } else {
                    Log.i(cv.get(COLUMN_ID).toString(), "Success")
                }
            }

        } catch (e: IOException){
            Log.i("reading JSONArray", "Error reading JSONArray");
        }
    }


    // checks if table is empty, returns boolean
    private fun checkIfTableEmpty(myDatabase: SQLiteDatabase): Boolean {
        val cursor = myDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val checkTable = cursor.moveToFirst()
        cursor.close()
        return checkTable
    }

}
