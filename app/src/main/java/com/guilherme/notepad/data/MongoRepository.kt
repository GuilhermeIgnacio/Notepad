package com.guilherme.notepad.data

import com.guilherme.notepad.models.Note
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface MongoRepository {

    fun getData(selectedChip: String?): Flow<List<Note>>
    fun getCategories(): MutableList<String?>
    suspend fun insertNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(noteId: ObjectId)

}