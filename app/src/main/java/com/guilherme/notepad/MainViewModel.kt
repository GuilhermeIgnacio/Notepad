package com.guilherme.notepad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NoteState(
    val noteTitle: String = "",
    val noteBody: String = "",
    val lastChange: String = "",
    val isNoteSheetOpen: Boolean = false

)

sealed interface NoteEvents {
    data class OnNoteTitleChange(val value: String) : NoteEvents
    data class OnNoteBodyChange(val value: String) : NoteEvents
    data object OnCreateNewNoteClick : NoteEvents
    data object OnCloseNoteSheetClick : NoteEvents
    data object OnSaveNote : NoteEvents
}

class MainViewModel : ViewModel() {

    private val realm = MyApp.realm
    private val _state = MutableStateFlow(NoteState())

    val state = _state.asStateFlow()


    fun onEvent(event: NoteEvents) {
        when (event) {

            is NoteEvents.OnNoteTitleChange -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            noteTitle = event.value
                        )
                    }
                }
            }

            is NoteEvents.OnNoteBodyChange -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            noteBody = event.value
                        )
                    }
                }
            }

            NoteEvents.OnCreateNewNoteClick -> {
                _state.update {
                    it.copy(
                        isNoteSheetOpen = true
                    )
                }
            }

            NoteEvents.OnCloseNoteSheetClick -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isNoteSheetOpen = false
                        )
                    }
                }
            }

            NoteEvents.OnSaveNote -> {

                viewModelScope.launch {
                    realm.write {

                    }
                }
            }
        }
    }

}