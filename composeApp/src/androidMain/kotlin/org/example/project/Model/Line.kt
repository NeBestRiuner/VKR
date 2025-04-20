package org.example.project.Model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

data class Line(
    var firstX: MutableState<Float>,
    var firstY: MutableState<Float>,
    var secondX: MutableState<Float>,
    var secondY: MutableState<Float>,
    )
