package org.example.project.model

import kotlinx.serialization.Serializable

@Serializable
data class Bytes(
    val postRectangleBytes: ByteArray,
    val lineListBytes: ByteArray
)
