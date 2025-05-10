package org.example.project.Model

data class BPTask(
    var id: Int,
    var name: String,
    var description: String,
    var duration: String,
    var priority: String,
    var percent: String,
    var file: ByteArray,
    var responsiblePost: String,
    var responsibleUser: MutableList<User>,
    var creatorUser: User,
    var completed: Boolean,
    var position: Int
)
