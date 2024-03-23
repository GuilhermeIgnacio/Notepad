package com.guilherme.notepad.data

import com.guilherme.notepad.models.Note
import org.mongodb.kbson.ObjectId

sealed interface NoteEvents {
    data class OnNoteTitleChange(val value: String) : NoteEvents
    data class OnNoteBodyChange(val value: String) : NoteEvents
    data class OnNoteCategoryChange(val value: String) : NoteEvents
    data class OnNoteClick(val note: Note) : NoteEvents
    data object OnCreateNewNoteClick : NoteEvents
    data object OnCloseNoteSheetClick : NoteEvents
    data object OnCategoryDialogClick : NoteEvents
    data object OnCategoryDialogClose : NoteEvents
    data object ClearCategoryField : NoteEvents
    data object DeleteNote : NoteEvents
    data class OpenDeleteDialog(val noteId: ObjectId) : NoteEvents
    data object CloseDeleteDialog : NoteEvents
    data class RichTextEditorSaveNote(val value: String, val verificationValue: String) : NoteEvents
    data object OpenBottomSheetColorPicker : NoteEvents
    data object CloseBottomSheetColorPicker : NoteEvents
    data class OnChipClick(val value: String?) : NoteEvents
    data object OpenDropDownMenu: NoteEvents
    data object CloseDropDownMenu: NoteEvents

}