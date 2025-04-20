package org.example.project.Model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset

class InfiniteScrollState {
    var offsetX by mutableFloatStateOf(0f)
    var offsetY by mutableFloatStateOf(0f)

    fun onScroll(dragAmount: Offset) {
        offsetX += dragAmount.x
        offsetY += dragAmount.y
    }
}