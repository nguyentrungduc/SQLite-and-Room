package com.example.sqlite_and_room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "items")
data class Item (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idItem")
    var id : Int,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "price")
    var price : Int,
    @ColumnInfo(name = "date")
    var date: Date

)