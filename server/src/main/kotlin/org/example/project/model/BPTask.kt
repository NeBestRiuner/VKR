package org.example.project.model

import kotlinx.serialization.Serializable
import org.example.project.API.UserAPI

@Serializable

data class BPTask(
    var id: Int,
    var name: String,
    var description: String,
    var duration: String,
    var priority: String,
    var percent: String,
    var file: ByteArray,
    var responsiblePost: String,
    var responsibleUser: MutableList<UserAPI>,
    var creatorUser: UserAPI,
    var completed: Boolean,
    var position: Int
)
