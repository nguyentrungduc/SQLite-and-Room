package com.example.sqlite_and_room

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Person(

    @PrimaryKey var id: Long = 0,

    var name: String = "",

    var age: Int = 0,

    var cats: RealmList<Cat> = RealmList()

) : RealmObject()