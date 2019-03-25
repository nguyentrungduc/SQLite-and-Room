package com.example.sqlite_and_room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [(Item::class)],version = 1,exportSchema = false)
@TypeConverters(Converters::class)
abstract class ItemDataBase : RoomDatabase() {
    companion object {
        private var INSTANCE: ItemDataBase? = null
        fun getDataBase(context: Context): ItemDataBase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, ItemDataBase::class.java, "items-db")
                    .allowMainThreadQueries().build()
            }
            return INSTANCE as ItemDataBase
        }
    }

    abstract fun daoItem(): DaoItem
}