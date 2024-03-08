package com.guilherme.notepad.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Note(): RealmObject {

    @PrimaryKey var _id: ObjectId = ObjectId()
    var noteTitle: String = ""
    var noteBody: String = ""
    var noteLastChange: String = ""
    var noteCategory: String = ""

}