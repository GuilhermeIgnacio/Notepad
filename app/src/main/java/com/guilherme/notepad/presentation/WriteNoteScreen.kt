package com.guilherme.notepad.presentation

import android.widget.ToggleButton
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.guilherme.notepad.R
import com.guilherme.notepad.data.NoteEvents
import com.guilherme.notepad.data.NoteState
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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

    var isLeft = rememberSaveable {
        mutableStateOf(true)
    }
    var isCenter = rememberSaveable {
        mutableStateOf(false)
    }
    var isRight = rememberSaveable {
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
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    actions = {

                        val currentSpanStyle = richTextState.currentSpanStyle
                        val isBold = currentSpanStyle.fontWeight == FontWeight.ExtraBold
                        val isItalic = currentSpanStyle.fontStyle == FontStyle.Italic
                        val isUnderline =
                            currentSpanStyle.textDecoration == TextDecoration.Underline
                        val isCrossedOut =
                            currentSpanStyle.textDecoration == TextDecoration.LineThrough



                        IconToggleButton(checked = isBold, onCheckedChange = {
                            richTextState.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.ExtraBold))
                        }) {
                            Icon(
                                imageVector = Icons.Default.FormatBold,
                                contentDescription = "Toggle Bold Text",
                                modifier = if (isBold) Modifier
                                    .background(
                                        Color(0x340091EA),
                                        CircleShape
                                    )
                                    .padding(4.dp) else Modifier.background(Color.Transparent)
                            )
                        }

                        IconToggleButton(checked = isItalic, onCheckedChange = {
                            richTextState.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
                        }) {
                            Icon(
                                imageVector = Icons.Default.FormatItalic,
                                contentDescription = "Toggle Italic Text",
                                modifier = if (isItalic) Modifier
                                    .background(
                                        Color(0x340091EA),
                                        CircleShape
                                    )
                                    .padding(4.dp)
                                else Modifier.background(Color.Transparent)
                            )
                        }

                        IconToggleButton(
                            checked = isUnderline,
                            onCheckedChange = {
                                richTextState.toggleSpanStyle(
                                    SpanStyle(
                                        textDecoration = TextDecoration.Underline
                                    )
                                )
                            }) {
                            Icon(
                                imageVector = Icons.Default.FormatUnderlined,
                                contentDescription = "Toggle Code Text",
                                modifier = if (isUnderline) Modifier
                                    .background(
                                        Color(0x340091EA),
                                        CircleShape
                                    )
                                    .padding(4.dp) else Modifier.background(Color.Transparent)
                            )
                        }

                        IconToggleButton(checked = isCrossedOut, onCheckedChange = {
                            richTextState.toggleSpanStyle(
                                SpanStyle(
                                    textDecoration = TextDecoration.LineThrough
                                )
                            )
                        }) {
                            Icon(
                                painterResource(id = R.drawable.strikethrough),
                                contentDescription = "Toggle Crossed Out Text",
                                modifier = if (isCrossedOut) Modifier
                                    .background(
                                        Color(0x340091EA),
                                        CircleShape
                                    )
                                    .width(24.dp)
                                    .padding(4.dp)
                                else Modifier
                                    .background(Color.Transparent)
                                    .width(24.dp)
                                    .padding(4.dp)
                            )
                        }

                        IconToggleButton(
                            checked = richTextState.isUnorderedList,
                            onCheckedChange = { richTextState.toggleUnorderedList() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.FormatListBulleted,
                                contentDescription = "Toggle Unordered List",
                                modifier = if (richTextState.isUnorderedList) Modifier
                                    .background(
                                        Color(0x340091EA),
                                        CircleShape
                                    )
                                    .padding(4.dp) else Modifier.background(Color.Transparent)
                            )

                        }

                        IconToggleButton(
                            checked = richTextState.isOrderedList,
                            onCheckedChange = { richTextState.toggleOrderedList() }) {
                            Icon(
                                imageVector = Icons.Default.FormatListNumbered,
                                contentDescription = "Toggle Numbered List",
                                modifier = if (richTextState.isOrderedList) Modifier
                                    .background(
                                        Color(0x340091EA),
                                        CircleShape
                                    )
                                    .padding(4.dp) else Modifier.background(Color.Transparent)
                            )
                        }

                        IconToggleButton(
                            checked = isLeft.value,
                            onCheckedChange = {
                                richTextState.toggleParagraphStyle(
                                    ParagraphStyle(
                                        textAlign = TextAlign.Left
                                    )
                                )

                                isLeft.value = true
                                isCenter.value = false
                                isRight.value = false

                            }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.FormatAlignLeft,
                                contentDescription = "",
                                modifier = if (isLeft.value) Modifier
                                    .background(
                                        Color(0x340091EA),
                                        CircleShape
                                    )
                                    .padding(6.dp) else Modifier.background(Color.Transparent)
                            )
                        }

                        IconToggleButton(
                            checked = isCenter.value,
                            onCheckedChange = {

                                richTextState.toggleParagraphStyle(
                                    ParagraphStyle(
                                        textAlign = TextAlign.Center
                                    )
                                )

                                isLeft.value = false
                                isCenter.value = true
                                isRight.value = false

                            }) {
                            Icon(
                                imageVector = Icons.Default.FormatAlignCenter,
                                contentDescription = "",
                                modifier = if (isCenter.value) Modifier
                                    .background(
                                        Color(0x340091EA),
                                        CircleShape
                                    )
                                    .padding(6.dp) else Modifier.background(Color.Transparent)
                            )
                        }

                        IconToggleButton(
                            checked = isRight.value,
                            onCheckedChange = {
                                richTextState.toggleParagraphStyle(
                                    ParagraphStyle(
                                        textAlign = TextAlign.Right
                                    )
                                )

                                isLeft.value = false
                                isCenter.value = false
                                isRight.value = true

                            }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.FormatAlignRight,
                                contentDescription = "",
                                modifier = if (isRight.value) Modifier
                                    .background(
                                        Color(0x340091EA),
                                        CircleShape
                                    )
                                    .padding(6.dp) else Modifier.background(Color.Transparent)
                            )
                        }

                        IconButton(onClick = { onEvent(NoteEvents.OpenDialogColorPicker) }) {
                            Icon(imageVector = Icons.Default.Colorize, contentDescription = "")
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
                val provider = GoogleFont.Provider(
                    providerAuthority = "com.google.android.gms.fonts",
                    providerPackage = "com.google.android.gms",
                    certificates = R.array.com_google_android_gms_fonts_certs
                )

                RichTextEditor(
                    state = richTextState,
                    modifier = Modifier
                        .fillMaxSize(),
                    placeholder = {
                        Text(text = "Body")
                    },
                    textStyle = LocalTextStyle.current.copy(
                        textDecoration = TextDecoration.None,
                        fontFamily = FontFamily(
                            Font(
                                googleFont = GoogleFont("Poppins"),
                                fontProvider = provider
                            )
                        )
                    ),
                    colors = RichTextEditorDefaults.richTextEditorColors(
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        containerColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(

                        //Todo: Resolver a questão do texto riscado

                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = true,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    )
                )

                if (state.isDialogColorPickerOpen) {

                    val colors = listOf(
                        ColorsItems(
                            colorCode = Color.Red,
                            colorEvent = { richTextState.toggleSpanStyle(SpanStyle(color = Color.Red)) }
                        ),
                        ColorsItems(
                            colorCode = Color.Blue,
                            colorEvent = { richTextState.toggleSpanStyle(SpanStyle(color = Color.Blue)) }
                        )
                    )

                    DialogColorPicker(colors = colors, richTextState = richTextState)
                }


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

data class ColorPicker(
    val colorCode: Color = Color.Black,
    val colorEvent: (() -> Unit)? = null
)

data class ColorsItems(
    val colorCode: Color = Color.White,
    val colorEvent: (() -> Unit)? = null
)

@Composable
fun DialogColorPicker(

    colors: List<ColorsItems>,
    richTextState: RichTextState

) {



    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        confirmButton = {
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "Pick Color")
            }
        },
        dismissButton = {
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "Cancel")
            }
        },
        text = {

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                content = {
                    items(colors) { color ->

                        Button(
                            onClick = { richTextState.toggleSpanStyle(SpanStyle(color = color.colorCode)) },
                            modifier = Modifier
                                .size(70.dp)
                                .fillMaxWidth(),
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = color.colorCode,
//                                contentColor = color.colorCode
                            ),
                            border = if (richTextState.currentSpanStyle.color == color.colorCode) BorderStroke(
                                3.dp, Color.Gray
                            ) else null
                        ) {

                        }

//                        IconButton(
//                            modifier = Modifier.background(Color.Transparent, RectangleShape),
//                            onClick = { richTextState.toggleSpanStyle(SpanStyle(color = color.colorCode)) },
//                            colors = IconButtonDefaults.iconButtonColors(
//                                containerColor = color.colorCode
//                            )
//                        ) {
//                            Icon(imageVector = Icons.Default.Check, contentDescription = "")
//                        }

                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

        }
    )

}

@Composable
fun ColorItem(
    color: ColorsItems,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { color.colorEvent },
        modifier = modifier,
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = color.colorCode,
            contentColor = color.colorCode
        )
    ) {

    }
}


