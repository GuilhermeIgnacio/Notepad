package com.guilherme.notepad.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guilherme.notepad.R
import com.guilherme.notepad.data.NoteEvents
import com.guilherme.notepad.data.NoteState
import com.guilherme.notepad.models.Note
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteItem(
    notes: List<Note>,
    onEvent: (NoteEvents) -> Unit,
    state: NoteState
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(notes) { note ->
            Item(
                note = note,
                modifier = Modifier.combinedClickable(
                    onClick = { onEvent(NoteEvents.OnNoteClick(note)) },
                    onLongClick = { onEvent(NoteEvents.OpenDeleteDialog(note._id)) }
                ))

            if (state.isDeleteDialogOpen){
                AlertDialog(
                    onDismissRequest = { onEvent(NoteEvents.CloseDeleteDialog) },
                    confirmButton = {
                        TextButton(onClick = { onEvent(NoteEvents.DeleteNote) }) {
                            Text(text = "Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { onEvent(NoteEvents.CloseDeleteDialog) }) {
                            Text(text = "Cancel")
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.DeleteForever,
                            contentDescription = "Delete Icon",
                            tint = Color.Red
                        )
                    },
                    title = { Text(text = "Delete Note?") },
                    text = { Text(text = "Are you sure you want do delete this note? This action cannot be undone") }
                )
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Item(
    note: Note,
    modifier: Modifier = Modifier
) {

    OutlinedCard(
        modifier = modifier
            .width(180.dp)
            .defaultMinSize(minHeight = 180.dp)
    ) {

        Column(
            modifier = Modifier.padding(8.dp)
        ) {

            if (note.noteTitle != null) {
                Text(
                    text = note.noteTitle ?: "",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }

            val richTextState = rememberRichTextState()
            richTextState.setHtml(note.noteBody ?: "")
            RichText(
                state = richTextState,
                fontSize = 16.sp
            )



            val date = LocalDateTime.parse(
                note.noteLastChange
            )
            val dateFormatter = date?.format(
                DateTimeFormatter.ofLocalizedDateTime(
                    FormatStyle.LONG,
                    FormatStyle.SHORT
                )
            )

            Text(
                text = stringResource(R.string.last_edit, dateFormatter ?: ""),
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(top = 32.dp),
                fontStyle = FontStyle.Italic
            )

        }


    }

}