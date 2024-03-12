package com.guilherme.notepad.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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

            NoteItem(notes = notes, onEvent = onEvent, state = state)

        }


    }

    WriteNoteScreen(isVisible = state.isNoteSheetOpen, state = state, onEvent = onEvent)
}

