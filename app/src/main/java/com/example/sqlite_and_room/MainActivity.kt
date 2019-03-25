package com.example.sqlite_and_room

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        realm = Realm.getDefaultInstance()

        ItemDataBase.getDataBase(this).daoItem().getAllItems()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("Tag", it.toString())

            }
    }

    fun addItem(name:String, price:Int){
        val item = Item(0,name,price, Date())
        Single.fromCallable {
            ItemDataBase.getDataBase(this).daoItem().insertItem(item)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe()

    }

    fun add () {
        realm.executeTransaction { realm ->
            // Add a person
            val person = realm.createObject<Person>(0)
            person.name = "D"
            person.age = 21
        }
    }

    private fun query(realm: Realm) {


        val ageCriteria = 21
        val results = realm.where<Person>().equalTo("age", ageCriteria).findAll()

        Log.d("TAg", results.size.toString())
    }


}
