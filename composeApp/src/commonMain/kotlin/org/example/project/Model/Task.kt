package org.example.project.Model

import androidx.compose.runtime.snapshots.SnapshotStateList

data class Task(
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
