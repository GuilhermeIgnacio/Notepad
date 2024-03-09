package com.guilherme.notepad.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.guilherme.notepad.data.NoteEvents
import com.guilherme.notepad.data.NoteState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteNoteScreen(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    state: NoteState,
    onEvent: (NoteEvents) -> Unit
) {

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = 300),
            initialOffsetY = { it }
        ),

        exit = slideOutVertically(
            animationSpec = tween(durationMillis = 300),
            targetOffsetY = { it }
        )
    ) {


        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "") },
                    navigationIcon = {
                        IconButton(onClick = { onEvent(NoteEvents.OnCloseNoteSheetClick) }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Return Button"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { onEvent(NoteEvents.OnCategoryDialogClick) }) {
                            Icon(
                                imageVector = Icons.Filled.Bookmark,
                                contentDescription = "Set Category Button"
                            )
                        }
                        TextButton(
                            onClick = { onEvent(NoteEvents.OnSaveNote) },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(text = "Save Note")
                        }
                    }

                )
            },
        ) { paddingValues ->
            Column(
                modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
            ) {
                TextField(
                    value = state.noteTitle ?: "",
                    onValueChange = { onEvent(NoteEvents.OnNoteTitleChange(value = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(text = "Title", fontWeight = FontWeight.Bold)
                    },
                    colors = TextFieldDefaults.colors(
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    ),
                    singleLine = true,
                )

                Divider(
                    thickness = 1.dp,
                    color = Color.Gray
                )

                TextField(
                    value = state.noteBody ?: "",
                    onValueChange = { onEvent(NoteEvents.OnNoteBodyChange(value = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(text = "Body")
                    },
                    colors = TextFieldDefaults.colors(
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    )
                )

            }

        }

        if (state.isCategoryDialogOpen) {
            NoteDialog(state = state, onEvent = onEvent)
        }

    }

}

@Composable
fun NoteDialog(
    state: NoteState,
    onEvent: (NoteEvents) -> Unit
) {
    AlertDialog(
        /* TODO: Talvez Definir um limite de caracteres para o campo de categoria */
        onDismissRequest = { onEvent(NoteEvents.OnCategoryDialogClose) },
        confirmButton = {
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = { onEvent(NoteEvents.OnCategoryDialogClose) }) {
                Text(text = "Close")
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Filled.Bookmark,
                contentDescription = "Bookmark icon for dialog"
            )
        },
        title = { Text(text = "Set a Category") },
        text = {
            /*TODO: Trocar a fonte de input desse textfield*/
            OutlinedTextField(
                value = state.noteCategory ?: "",
                onValueChange = { onEvent(NoteEvents.OnNoteCategoryChange(it)) },
                label = { Text(text = "Note Category") })
        }
    )
}
