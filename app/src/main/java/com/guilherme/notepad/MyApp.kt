package com.guilherme.notepad

import android.app.Application
import com.guilherme.notepad.models.Note
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class MyApp: Application() {

    companion object {
        lateinit var realm: Realm
    }

    override fun onCreate() {
        super.onCreate()

        val config = RealmConfiguration.create(schema = setOf(Note::class))
        realm = Realm.open(config)
    }

}