package com.guilherme.notepad.models

import androidx.compose.ui.graphics.Color

data class ChooseTextColorItems(
    val colorCode: Color = Color.White,
    val colorEvent: (() -> Unit)? = null
)