package org.example.project.model

import kotlinx.serialization.Serializable
import org.example.project.API.UserAPI

@Serializable

data class Task(
    var name: String,
    var description: String,
    var beginTime: String,
    var endTime: String,
    var priority: String,
    var percent: String,
    var file: ByteArray,
    var responsiblePersons: MutableList<UserAPI>,
    var creatorUser: UserAPI,
    var completed: Boolean
)
