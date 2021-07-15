package com.example.fetchexercise

data class Item(val id: Int, val listId: Int, val name: String){
    override fun toString(): String {
        return "\nid: $id\nlistId: $listId\nname: $name\n"
    }
}
