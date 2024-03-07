package com.guilherme.notepad

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class NoteState(
    val noteTitle: String = "",
    val noteBody: String = "",
    val lastChange: String = "",
    val isCreateNewNoteSheetOpen: Boolean = false
)

sealed interface NoteEvents {
    data class OnNoteTitleChange(val value: String) : NoteEvents
    data class OnNoteBodyChange(val value: String) : NoteEvents
    data object OnCreateNewNoteClick : NoteEvents
    data object OnCloseNoteSheetClick: NoteEvents
}

class MainViewModel : ViewModel() {

    private val _state = MutableStateFlow(NoteState())
    val state = _state.asStateFlow()

    fun onEvent(events: NoteEvents) {
        when (events) {
            is NoteEvents.OnNoteBodyChange -> {
                TODO()
            }

            is NoteEvents.OnNoteTitleChange -> {
                TODO()
            }

            NoteEvents.OnCreateNewNoteClick -> {
                TODO()
            }

            NoteEvents.OnCloseNoteSheetClick -> {
                TODO()
            }
        }
    }

}