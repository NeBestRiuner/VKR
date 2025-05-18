package org.example.project.Model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import kotlinx.serialization.Serializable


data class Line(
    var firstPostRectangle: PostRectangle,
    var secondPostRectangle: PostRectangle
    )
