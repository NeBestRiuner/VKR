package org.example.project.API

import kotlinx.serialization.Serializable

@Serializable

data class SendHierarchyResponse(
    var status: String,
    var description: String
)
