package com.guilherme.notepad.data

import com.guilherme.notepad.models.Note
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface MongoRepository {

    fun getData(): Flow<List<Note>>
    fun filterData(noteCategory: String?): Flow<List<Note>>

    suspend fun insertNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(noteId: ObjectId)

}