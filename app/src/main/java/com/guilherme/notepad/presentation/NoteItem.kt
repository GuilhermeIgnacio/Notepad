package com.guilherme.notepad.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guilherme.notepad.R
import com.guilherme.notepad.data.NoteEvents
import com.guilherme.notepad.models.Note
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteItem(
    notes: List<Note>,
    onEvent: (NoteEvents) -> Unit
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
                modifier = Modifier.clickable { onEvent(NoteEvents.OnNoteClick(note)) })
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

            Text(
                text = note.noteBody ?: "",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
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