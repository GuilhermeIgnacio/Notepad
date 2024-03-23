package com.guilherme.notepad.data

import androidx.compose.material3.SnackbarHostState
import com.guilherme.notepad.models.Note
import org.mongodb.kbson.ObjectId

data class NoteState(
    val notes: List<Note> = emptyList(),
    val categories: MutableList<String> = mutableListOf(),
    val noteTitle: String? = null,
    val noteBody: String? = null,
    val noteId: ObjectId? = null,
    val noteCategory: String? = null,
    val lastChange: String? = null,
    val isNoteSheetOpen: Boolean = false,
    val isCategoryDialogOpen: Boolean = false,
    val isEditMode: Boolean = false,
    val isDeleteDialogOpen: Boolean = false,
    val snackbar: SnackbarHostState = SnackbarHostState(),
    val isBottomSheetColorPickerOpen: Boolean = false,
    val selectedChip: String? = null,
    val isDropDownMenuOpen: Boolean = false
)