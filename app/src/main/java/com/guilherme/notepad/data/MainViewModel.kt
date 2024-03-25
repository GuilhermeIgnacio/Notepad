package com.guilherme.notepad.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.notepad.models.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MongoRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NoteState())

    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getData(_state.value.selectedChip).collect { results ->
                _state.update {
                    it.copy(
                        notes = results
                    )
                }
            }
        }

    }

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    categories = repository.getCategories()
                )
            }
        }
    }

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
                            noteCategory = event.value,
                            isDropDownMenuOpen = false
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
                            isDeleteDialogOpen = false,
                            categories = repository.getCategories()
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
                                isEditMode = false,
                                categories = repository.getCategories(),
                                selectedChip = null
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

                    repository.getData(event.value).collect { results ->
                        _state.update {
                            it.copy(
                                selectedChip = event.value,
                                notes = results
                            )
                        }
                    }


                }

            }

            NoteEvents.OpenDropDownMenu -> {
                _state.update { it.copy(
                    isDropDownMenuOpen = true
                ) }
            }

            NoteEvents.CloseDropDownMenu -> {
                viewModelScope.launch {
                    _state.update { it.copy(
                        isDropDownMenuOpen = false
                    ) }
                }
            }
        }
    }

}