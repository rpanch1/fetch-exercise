package com.example.fetchexercise

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ItemsActivity : AppCompatActivity() {

    private lateinit var myDB: MyDatabaseHelper
    lateinit var itemsListview: ListView
    lateinit var itemsList: List<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)
        supportActionBar?.title = "Fetch Exercise"

        itemsListview = findViewById<ListView>(R.id.items_listview)
        myDB = MyDatabaseHelper(this@ItemsActivity)

        displayList()
    }

    private fun displayList() {
        itemsList = myDB.getItems()
        val customArrayAdapter: ArrayAdapter<Item> = ArrayAdapter<Item>(
            this@ItemsActivity,
            android.R.layout.simple_list_item_1,
            itemsList
        )
        itemsListview.setAdapter(customArrayAdapter)
    }
}