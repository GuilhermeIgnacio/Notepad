package com.guilherme.notepad.data

import android.provider.ContactsContract
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.notepad.MyApp
import com.guilherme.notepad.models.Note
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NoteState(
    val noteTitle: String? = null,
    val noteBody: String? = null,
    val noteCategory: String? = null,
    val lastChange: String? = null,
    val isNoteSheetOpen: Boolean = false,
    val isCategoryDialogOpen: Boolean = false,
    val snackbar: SnackbarHostState = SnackbarHostState()
)

sealed interface NoteEvents {
    data class OnNoteTitleChange(val value: String) : NoteEvents
    data class OnNoteBodyChange(val value: String) : NoteEvents
    data class OnNoteCategoryChange(val value: String) : NoteEvents
    data object OnCreateNewNoteClick : NoteEvents
    data object OnCloseNoteSheetClick : NoteEvents
    data object OnCategoryDialogClick : NoteEvents
    data object OnCategoryDialogClose : NoteEvents
    data object ClearCategoryField : NoteEvents
    data object OnSaveNote : NoteEvents
}

class MainViewModel : ViewModel() {

    private val realm = MyApp.realm
    private val _state = MutableStateFlow(NoteState())

    val state = _state.asStateFlow()

    val notes = realm.query<Note>()
        .asFlow()
        .map { results ->
            results.list.toList()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )


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

                val stateNoteTitle = _state.value.noteTitle
                val stateNoteBody = _state.value.noteBody
                val stateNoteCategory = _state.value.noteCategory

                viewModelScope.launch {

                    if (stateNoteTitle.isNullOrEmpty() && stateNoteBody.isNullOrEmpty()) {
                        _state.value.snackbar.showSnackbar("Both Title and Body fields should not be left empty")
                    } else {
                        realm.write {
                            val newNote = Note().apply {
                                noteTitle = stateNoteTitle
                                noteBody = stateNoteBody
                                noteCategory = stateNoteCategory
                            }
                            copyToRealm(newNote, updatePolicy = UpdatePolicy.ALL)
                        }

                        _state.update {
                            it.copy(
                                noteTitle = null,
                                noteBody = null,
                                noteCategory = null,
                                isNoteSheetOpen = false
                            )
                        }
                    }


                }
            }

            NoteEvents.OnCategoryDialogClick -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isCategoryDialogOpen = true
                        )
                    }
                }
            }

            NoteEvents.OnCategoryDialogClose -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isCategoryDialogOpen = false,
                        )
                    }
                }
            }

            NoteEvents.ClearCategoryField -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            noteCategory = null
                        )
                    }
                }
            }

            is NoteEvents.OnNoteCategoryChange -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            noteCategory = event.value
                        )
                    }
                }
            }


        }
    }

}