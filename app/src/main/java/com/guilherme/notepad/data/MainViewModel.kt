package com.guilherme.notepad.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.notepad.MyApp
import com.guilherme.notepad.models.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import java.time.LocalDateTime
import javax.inject.Inject

data class NoteState(
    val notes: List<Note> = emptyList(),
    val filteredNotes: List<Note> = emptyList(),
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
    val selectedChip: String? = null
)

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
    data object OnSaveNote : NoteEvents
    data object DeleteNote : NoteEvents
    data class OpenDeleteDialog(val noteId: ObjectId) : NoteEvents
    data object CloseDeleteDialog : NoteEvents
    data class RichTextEditorSaveNote(val value: String, val verificationValue: String) : NoteEvents
    data object OpenBottomSheetColorPicker : NoteEvents
    data object CloseBottomSheetColorPicker : NoteEvents
    data class OnChipClick(val value: String?) : NoteEvents

}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MongoRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NoteState())

    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getData().collect { results ->
                _state.update {
                    it.copy(
                        notes = results
                    )
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
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
                            isNoteSheetOpen = false,
                            noteId = null,
                            noteTitle = null,
                            noteBody = null,
                            noteCategory = null,
                            isEditMode = false
                        )
                    }
                }
            }

            NoteEvents.OnSaveNote -> {

                val stateNoteTitle = _state.value.noteTitle
                val stateNoteBody = _state.value.noteBody
                val stateNoteCategory = _state.value.noteCategory

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

            is NoteEvents.OnNoteClick -> {

                val note = event.note

                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isNoteSheetOpen = true,
                            isEditMode = true,
                            noteId = note._id,
                            noteTitle = note.noteTitle,
                            noteBody = note.noteBody,
                            noteCategory = note.noteCategory,

                            )
                    }
                }

            }

            NoteEvents.DeleteNote -> {
                viewModelScope.launch {
                    _state.value.noteId?.let { repository.deleteNote(it) }
                    _state.update {
                        it.copy(
                            noteId = null,
                            isDeleteDialogOpen = false
                        )
                    }
                }

            }

            is NoteEvents.OpenDeleteDialog -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            noteId = event.noteId,
                            isDeleteDialogOpen = true
                        )
                    }
                }
            }

            NoteEvents.CloseDeleteDialog -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            noteId = null,
                            isDeleteDialogOpen = false
                        )
                    }
                }
            }

            is NoteEvents.RichTextEditorSaveNote -> {
                println("Value ${event.value}")
                println("Verification Value ${event.verificationValue}")

                viewModelScope.launch {

                    _state.update {
                        it.copy(
                            noteBody = event.value,
                            lastChange = LocalDateTime.now().toString()
                        )
                    }

                    if (_state.value.noteTitle.isNullOrEmpty() && event.verificationValue.isEmpty()) {
                        _state.value.snackbar.showSnackbar("Both Title and Body fields should not be left empty")
                    } else {

                        repository.insertNote(note = Note().apply {
                            if (_state.value.isEditMode) {
                                _id = _state.value.noteId!!
                            }
                            noteTitle = _state.value.noteTitle
                            noteBody = _state.value.noteBody
                            noteCategory = _state.value.noteCategory
                            noteLastChange = _state.value.lastChange
                        })

                        _state.update {
                            it.copy(
                                noteId = null,
                                noteTitle = null,
                                noteBody = null,
                                noteCategory = null,
                                isNoteSheetOpen = false,
                                isEditMode = false
                            )
                        }

                    }
                }

            }

            NoteEvents.OpenBottomSheetColorPicker -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isBottomSheetColorPickerOpen = true
                        )
                    }
                }
            }

            NoteEvents.CloseBottomSheetColorPicker -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isBottomSheetColorPickerOpen = false
                        )
                    }
                }
            }

            is NoteEvents.OnChipClick -> {

                viewModelScope.launch {
                    repository.filterData(event.value).collect { results ->
                        _state.update {
                            it.copy(
                                selectedChip = event.value,
                                filteredNotes = results
                            )
                        }
                    }

                }

            }
        }
    }

}