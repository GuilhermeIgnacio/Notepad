package com.guilherme.notepad.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Note : RealmObject {

    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var noteTitle: String? = null
    var noteBody: String? = null
    var noteLastChange: String? = null
    var noteCategory: String? = null

}