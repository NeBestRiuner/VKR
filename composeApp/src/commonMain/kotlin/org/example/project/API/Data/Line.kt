package org.example.project.API.Data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import kotlinx.serialization.Serializable

@Serializable
data class Line(
    var firstX: MutableState<Float>,
    var firstY: MutableState<Float>,
    var secondX: MutableState<Float>,
    var secondY: MutableState<Float>,
    )
