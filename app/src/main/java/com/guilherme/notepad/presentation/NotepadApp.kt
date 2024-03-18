package com.guilherme.notepad.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
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
    val notes by viewModel.notes.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(NoteEvents.OnCreateNewNoteClick) },
                shape = CircleShape,
                modifier = Modifier.padding(16.dp),
                containerColor = Color(82, 111, 242)
            ) {
                Icon(
                    imageVector = Icons.Filled.Create,
                    contentDescription = "Create New Note",
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
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp
            )

            //Todo: Se não tiver nenhuma categoria criada esconder esse componente
            NoteCategory(state = state, notes = notes, onEvent = onEvent)

            NoteItem(notes = notes, onEvent = onEvent, state = state)

        }


    }

    WriteNoteScreen(isVisible = state.isNoteSheetOpen, state = state, onEvent = onEvent)
}

@Composable
fun NoteCategory(
    state: NoteState,
    notes: List<Note>,
    onEvent: (NoteEvents) -> Unit
) {

    Row {
        SuggestionChip(
            modifier = Modifier.padding(end = 4.dp),
            onClick = { onEvent(NoteEvents.OnChipClick(null)) },
            label = {
                Text(text = "All Notes")
            },
            colors = if (state.noteCategory == null) SuggestionChipDefaults.suggestionChipColors(
                containerColor = Color(84, 110, 241),
                labelColor = Color.White,

                ) else SuggestionChipDefaults.suggestionChipColors(),
            border = if (state.noteCategory == null) SuggestionChipDefaults.suggestionChipBorder(
                borderColor = Color.Transparent
            ) else SuggestionChipDefaults.suggestionChipBorder()
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {


            items(notes) { note ->

                SuggestionChip(
                    onClick = { onEvent(NoteEvents.OnChipClick(note.noteCategory)) },
                    label = {
                        Text(text = note.noteCategory ?: "")
                    })

            }

        }
    }

}

