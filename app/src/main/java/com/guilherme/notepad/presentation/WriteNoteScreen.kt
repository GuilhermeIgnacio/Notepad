package com.guilherme.notepad.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.dp
import com.guilherme.notepad.R
import com.guilherme.notepad.data.NoteEvents
import com.guilherme.notepad.data.NoteState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorColors
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteNoteScreen(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    state: NoteState,
    onEvent: (NoteEvents) -> Unit
) {

    var isOrderedListChecked = rememberSaveable {
        mutableStateOf(false)
    }

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

        val richTextState = rememberRichTextState()

        Scaffold(

            snackbarHost = { SnackbarHost(hostState = state.snackbar) },

            topBar = {
                TopAppBar(
                    title = {
                        TextField(
                            value = state.noteTitle ?: "",
                            onValueChange = { onEvent(NoteEvents.OnNoteTitleChange(it)) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(text = "Title", fontWeight = FontWeight.Bold) },
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
                    },
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
                            onClick = {
                                onEvent(NoteEvents.RichTextEditorSaveNote(richTextState.toHtml()))
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(text = "Save Note")
                        }
                    }

                )
            },

            bottomBar = {
                BottomAppBar(
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(imageVector = Icons.Filled.FormatBold, contentDescription = "")
                        }
                        IconButton(onClick = {  }) {
                            Icon(imageVector = Icons.Filled.FormatItalic, contentDescription = "")
                        }


                        IconToggleButton(checked = isOrderedListChecked.value, onCheckedChange = {

                            /*TODO: Checar se modo estiver ativado, pode ser que usuário desative sem usar o botão*/

                            richTextState.toggleOrderedList()
                            isOrderedListChecked.value = !isOrderedListChecked.value
                        }) {
                            Icon(
                                imageVector = Icons.Default.FormatListNumbered,
                                contentDescription = null
                            )
                        }

                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
            ) {
//                TextField(
//                    value = state.noteBody ?: "",
//                    onValueChange = { onEvent(NoteEvents.OnNoteBodyChange(value = it)) },
//                    modifier = Modifier.fillMaxWidth(),
//                    placeholder = {
//                        Text(text = "Body")
//                    },
//                    colors = TextFieldDefaults.colors(
//                        disabledTextColor = Color.Transparent,
//                        focusedIndicatorColor = Color.Transparent,
//                        unfocusedIndicatorColor = Color.Transparent,
//                        disabledIndicatorColor = Color.Transparent,
//                        unfocusedContainerColor = Color.Transparent,
//                        focusedContainerColor = Color.Transparent
//                    )
//                )

                val provider = GoogleFont.Provider(
                    providerAuthority = "com.google.android.gms.fonts",
                    providerPackage = "com.google.android.gms",
                    certificates = R.array.com_google_android_gms_fonts_certs
                )

                RichTextEditor(
                    state = richTextState,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = {
                        Text(text = "Body")
                    },
                    textStyle = LocalTextStyle.current.copy(
                        fontFamily = FontFamily(Font(googleFont = GoogleFont("Poppins"), fontProvider = provider))
                    ),
                    colors = RichTextEditorDefaults.richTextEditorColors(
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        containerColor = Color.Transparent
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
            TextButton(onClick = { onEvent(NoteEvents.OnCategoryDialogClose) }) {
                Text(text = "Confirm")
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
            /*TODO: Category -> Trocar a fonte de input desse textfield (Esta usando a fonte padrão do Compose)*/
            OutlinedTextField(
                value = state.noteCategory ?: "",
                onValueChange = { onEvent(NoteEvents.OnNoteCategoryChange(it)) },
                label = { Text(text = "Note Category") },
                trailingIcon = {
                    if (!state.noteCategory.isNullOrEmpty()) {
                        IconButton(onClick = { onEvent(NoteEvents.ClearCategoryField) }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear category text-field"
                            )
                        }
                    }
                },

                )
        }
    )
}
