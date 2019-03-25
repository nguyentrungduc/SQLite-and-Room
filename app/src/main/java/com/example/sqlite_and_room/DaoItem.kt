package com.example.sqlite_and_room

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface DaoItem {

    @Query("select * from items")
    fun getAllItems(): Flowable<List<Item>>

    @Query("select * from items where idItem in (:id)")
    fun getItemById(id: Int):Item

    @Query("delete from items")
    fun deleteAllItems()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(Item: Item)

    @Update
    fun updateItem(Item: Item)

    @Delete
    fun deleteItem(Item: Item)

}