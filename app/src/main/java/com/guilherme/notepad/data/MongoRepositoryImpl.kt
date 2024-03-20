package com.guilherme.notepad.data

import com.guilherme.notepad.models.Note
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class MongoRepositoryImpl(val realm: Realm): MongoRepository {
    override fun getData(): Flow<List<Note>> {
        return realm.query<Note>().asFlow().map { it.list }
    }

    override fun filterData(noteCategory: String?): Flow<List<Note>> {
        return realm.query<Note>("noteCategory == $0", noteCategory).asFlow().map { it.list }
    }

    override suspend fun insertNote(note: Note) {
        realm.write { copyToRealm(note) }
    }

    override suspend fun updateNote(note: Note) {
        realm.write {
            val queriedNote = query<Note>("_id == $0", note._id).first().find()
            queriedNote?.noteTitle = note.noteTitle
            queriedNote?.noteBody = note.noteBody
            queriedNote?.noteCategory = note.noteCategory
            queriedNote?.noteLastChange = note.noteLastChange
        }
    }

    override suspend fun deleteNote(noteId: ObjectId) {
        realm.write {
            val deleteNote = query<Note>("_id == $0", noteId).first().find()
            deleteNote?.let { delete(it) }
        }
    }
}