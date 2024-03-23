package com.guilherme.notepad.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Bookmark
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.guilherme.notepad.models.ChooseTextColorItems
import com.guilherme.notepad.ui.theme.fontFamily
import com.guilherme.notepad.ui.theme.provider
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteNoteScreen(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    state: NoteState,
    onEvent: (NoteEvents) -> Unit,
    categories: MutableList<String>
) {
    val isLeft = rememberSaveable {
        mutableStateOf(false)
    }
    val isCenter = rememberSaveable {
        mutableStateOf(false)
    }
    val isRight = rememberSaveable {
        mutableStateOf(false)
    }

    if (!isVisible) {
        isLeft.value = false
        isCenter.value = false
        isRight.value = false
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
        val sheetState = rememberModalBottomSheetState()



        Scaffold(

            snackbarHost = { SnackbarHost(hostState = state.snackbar) },

            topBar = {
                TopAppBar(
                    title = {
                        TextField(
                            value = state.noteTitle ?: "",
                            onValueChange = { onEvent(NoteEvents.OnNoteTitleChange(it)) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(text = stringResource(R.string.title), fontWeight = FontWeight.Bold) },
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
                                contentDescription = stringResource(R.string.return_button_icon_description)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { onEvent(NoteEvents.OnCategoryDialogClick) }) {
                            Icon(
                                imageVector = Icons.Filled.Bookmark,
                                contentDescription = stringResource(R.string.set_category_button_icon_description)
                            )
                        }
                        TextButton(
                            onClick = {
                                onEvent(
                                    NoteEvents.RichTextEditorSaveNote(
                                        richTextState.toHtml(),
                                        richTextState.toMarkdown()
                                    )
                                )
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(text = stringResource(R.string.save_note_button))
                        }
                    }

                )
            },

            bottomBar = {
                BottomAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
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
                                contentDescription = stringResource(R.string.toggle_bold_text_buttonicon_description),
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
                                contentDescription = stringResource(R.string.toggle_italic_text_buttonicon_description),
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
                                contentDescription = stringResource(R.string.toggle_code_text_icon_description),
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
                                contentDescription = stringResource(R.string.toggle_crossed_out_text_icon_description),
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
                                contentDescription = stringResource(R.string.toggle_unordered_list_icon_description),
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
                                contentDescription = stringResource(R.string.toggle_numbered_list_icon_description),
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
                                contentDescription = stringResource(R.string.text_align_left_icon_desc),
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
                                contentDescription = stringResource(R.string.text_align_center_icon_description),
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
                                contentDescription = stringResource(R.string.text_align_right_icon_description),
                                modifier = if (isRight.value) Modifier
                                    .background(
                                        Color(0x340091EA),
                                        CircleShape
                                    )
                                    .padding(6.dp) else Modifier.background(Color.Transparent)
                            )
                        }

                        IconToggleButton(
                            checked = true,
                            onCheckedChange = { onEvent(NoteEvents.OpenBottomSheetColorPicker) },
                            modifier = Modifier
                                .background(
                                    richTextState.currentSpanStyle.color,
                                    CircleShape
                                )
                                .border(1.dp, Color.Gray, CircleShape)
                                .width(32.dp)
                                .height(32.dp)
                        ) {

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

                RichTextEditor(
                    state = richTextState,
                    modifier = Modifier
                        .fillMaxSize()
                        .onFocusChanged { richTextState.currentParagraphStyle.textAlign },
                    placeholder = {
                        Text(text = stringResource(R.string.textfield_body))
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

                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = true,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    )
                )

                if (state.noteBody != null) {
                    LaunchedEffect(Unit) {
                        richTextState.setHtml(state.noteBody)
                    }
                }


            }

            if (state.isBottomSheetColorPickerOpen) {

                val colors = listOf(
                    ChooseTextColorItems(
                        colorCode = Color.Red,
                        colorEvent = { richTextState.toggleSpanStyle(SpanStyle(color = Color.Red)) }
                    ),
                    ChooseTextColorItems(
                        colorCode = Color.Gray,
                        colorEvent = { richTextState.toggleSpanStyle(SpanStyle(color = Color.Gray)) }
                    ),
                    ChooseTextColorItems(
                        colorCode = Color.Blue,
                        colorEvent = { richTextState.toggleSpanStyle(SpanStyle(color = Color.Blue)) }
                    ),
                    ChooseTextColorItems(
                        colorCode = Color.Cyan,
                        colorEvent = { richTextState.toggleSpanStyle(SpanStyle(color = Color.Cyan)) }
                    ),
                    ChooseTextColorItems(
                        colorCode = Color.Black,
                        colorEvent = { richTextState.toggleSpanStyle(SpanStyle(color = Color.Black)) }
                    ),
                    ChooseTextColorItems(
                        colorCode = Color.DarkGray,
                        colorEvent = { richTextState.toggleSpanStyle(SpanStyle(color = Color.DarkGray)) }
                    ),
                    ChooseTextColorItems(
                        colorCode = Color.Green,
                        colorEvent = { richTextState.toggleSpanStyle(SpanStyle(color = Color.Green)) }
                    ),
                    ChooseTextColorItems(
                        colorCode = Color.LightGray,
                        colorEvent = { richTextState.toggleSpanStyle(SpanStyle(color = Color.LightGray)) }
                    ),
                    ChooseTextColorItems(
                        colorCode = Color.Magenta,
                        colorEvent = { richTextState.toggleSpanStyle(SpanStyle(color = Color.Magenta)) }
                    ), ChooseTextColorItems(
                        colorCode = Color.Yellow,
                        colorEvent = { richTextState.toggleSpanStyle(SpanStyle(color = Color.Yellow)) }
                    )
                )

                DialogColorPicker(
                    colors = colors,
                    richTextState = richTextState,
                    onEvent = onEvent,
                    sheetState = sheetState,
                )


            }

        }

        if (state.isCategoryDialogOpen) {
            NoteDialog(state = state, onEvent = onEvent, categories = categories)
        }


    }


}

@Composable
fun NoteDialog(
    state: NoteState,
    onEvent: (NoteEvents) -> Unit,
    categories: MutableList<String>
) {
    AlertDialog(


        onDismissRequest = { onEvent(NoteEvents.OnCategoryDialogClose) },
        confirmButton = {
            TextButton(onClick = { onEvent(NoteEvents.OnCategoryDialogClose) }) {
                Text(text = stringResource(R.string.note_confirm_button))
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Filled.Bookmark,
                contentDescription = stringResource(R.string.bookmark_icon_for_dialog)
            )
        },
        title = { Text(text = stringResource(R.string.note_dialog_set_a_category)) },
        text = {
            OutlinedTextField(
                value = state.noteCategory ?: "",
                onValueChange = { onEvent(NoteEvents.OnNoteCategoryChange(it)) },
                label = { Text(text = stringResource(R.string.note_category_dialog_label)) },
                trailingIcon = {
                    if (!state.noteCategory.isNullOrEmpty()) {
                        IconButton(onClick = { onEvent(NoteEvents.ClearCategoryField) }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = stringResource(R.string.clear_category_text_field_icon_description)
                            )
                        }
                    } else {

                        if (categories.isNotEmpty()) {
                            IconButton(onClick = { onEvent(NoteEvents.OpenDropDownMenu) }) {

                                if (state.isDropDownMenuOpen) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropUp,
                                        contentDescription = stringResource(R.string.open_drop_down_menu_icon_descripiton)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = stringResource(R.string.open_drop_down_menu_icon_description)
                                    )
                                }

                            }
                        }


                    }
                },
                textStyle = LocalTextStyle.current.copy(
                    fontFamily = FontFamily(
                        Font(
                            googleFont = GoogleFont("Poppins"),
                            fontProvider = provider
                        )
                    )
                )

            )

            CategoryDropDownMenu(state = state, onEvent = onEvent, categories = categories)

        }
    )
}

data class NoteDropDownItem(
    var category: String? = null
)


@Composable
fun CategoryDropDownMenu(
    state: NoteState,
    onEvent: (NoteEvents) -> Unit,
    categories: MutableList<String>
) {

    val lorem = NoteDropDownItem()

    categories.forEach { values ->
        lorem.apply {
            category = values
        }
    }

    DropdownMenu(
        expanded = state.isDropDownMenuOpen,
        onDismissRequest = { onEvent(NoteEvents.CloseDropDownMenu) }) {

        categories.distinct().forEach { values ->

            DropdownMenuItem(
                text = { Text(text = values) },
                onClick = { onEvent(NoteEvents.OnNoteCategoryChange(values)) })

        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogColorPicker(

    colors: List<ChooseTextColorItems>,
    richTextState: RichTextState,
    onEvent: (NoteEvents) -> Unit,
    sheetState: SheetState,
) {

    ModalBottomSheet(
        onDismissRequest = { onEvent(NoteEvents.CloseBottomSheetColorPicker) },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.secondary
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            content = {
                items(colors) { color ->

                    Button(
                        onClick = { richTextState.toggleSpanStyle(SpanStyle(color = color.colorCode)) },
                        modifier = Modifier
                            .size(50.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(10),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = color.colorCode,
                        ),
                        border = if (richTextState.currentSpanStyle.color == color.colorCode) BorderStroke(
                            3.dp, Color.White
                        ) else null
                    ) {

                    }

                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 64.dp, start = 8.dp, end = 8.dp)
        )
    }

}


