package com.guilherme.notepad.data

import com.guilherme.notepad.models.Note
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class MongoRepositoryImpl(val realm: Realm) : MongoRepository {
    override fun getData(selectedChip: String?): Flow<List<Note>> {
        if (selectedChip == null) {
            return realm.query<Note>().asFlow().map { it.list }
        } else {
            return realm.query<Note>("noteCategory == $0", selectedChip).asFlow().map { it.list }
        }
    }

    override fun getCategories(): MutableList<String> {
        val categories: MutableList<String> = mutableListOf()

        realm.query<Note>().find()
            .forEach { note ->
                note.noteCategory?.let { categories.add(it) }
            }

        return categories

    }

    override suspend fun insertNote(note: Note) {
        realm.write { copyToRealm(note, updatePolicy = UpdatePolicy.ALL) }
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