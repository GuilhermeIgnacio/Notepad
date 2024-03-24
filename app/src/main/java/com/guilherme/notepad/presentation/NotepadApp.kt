package com.guilherme.notepad.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.guilherme.notepad.R
import com.guilherme.notepad.data.MainViewModel
import com.guilherme.notepad.data.NoteEvents
import com.guilherme.notepad.data.NoteState
import com.guilherme.notepad.models.Note

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotepadApp(
    viewModel: MainViewModel = viewModel()
) {

    val onEvent = viewModel::onEvent
    val state by viewModel.state.collectAsStateWithLifecycle()
    val notes = state.notes
    val categories = state.categories

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(NoteEvents.OnCreateNewNoteClick) },
                shape = CircleShape,
                containerColor = Color(82, 111, 242)
            ) {
                Icon(
                    imageVector = Icons.Filled.Create,
                    contentDescription = stringResource(R.string.create_new_note_icon_description),
                    tint = Color.White
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            Text(
                text = "Notepad",
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp
            )

            NoteCategory(state = state, notes = categories, onEvent = onEvent)

            AnimatedContent(
                targetState = state.selectedChip,
                label = "null",
                transitionSpec = {
                    slideInHorizontally(
                        initialOffsetX = { -it }
                    ) togetherWith slideOutHorizontally(
                        targetOffsetX = { it }
                    )
                }
            ) {
                if (it == state.selectedChip) {
                    NoteItem(notes = notes, onEvent = onEvent, state = state)
                }
            }

        }


    }

    WriteNoteScreen(
        isVisible = state.isNoteSheetOpen,
        state = state,
        onEvent = onEvent,
        categories = categories
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteCategory(
    state: NoteState,
    notes: MutableList<String>,
    onEvent: (NoteEvents) -> Unit
) {

    Row {
        FilterChip(
            onClick = {
                onEvent(NoteEvents.OnChipClick(null))
                println(state.noteCategory)
            },
            selected = state.selectedChip == null,
            label = {
                Text(text = stringResource(R.string.all_notes_chip_filter))
            },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Color(84, 110, 241),
                selectedLabelColor = Color.White,
                disabledLabelColor = Color.Black,
                containerColor = Color(240, 240, 255, 255)
            ),
            border = FilterChipDefaults.filterChipBorder(
                borderColor = Color.Transparent
            )
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {


            items(notes.distinct()) { note ->

                FilterChip(
                    onClick = {
                        onEvent(NoteEvents.OnChipClick(note))
                        println(state.noteCategory)
                    },
                    selected = state.selectedChip == note,
                    label = {
                        Text(text = note)
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(84, 110, 241),
                        selectedLabelColor = Color.White,
                        disabledLabelColor = Color.Black,
                        containerColor = Color(240, 240, 255, 255)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = Color.Transparent
                    )
                )


            }

        }
    }

}

