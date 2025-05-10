package org.example.project.Model

data class TaskWithID(
    val id: Int,
    var name: String,
    var description: String,
    var beginTime: String,
    var endTime: String,
    var priority: String,
    var percent: String,
    var file: ByteArray,
    var responsiblePersons: MutableList<User>,
    var creatorUser: User,
    var completed: Boolean
)
