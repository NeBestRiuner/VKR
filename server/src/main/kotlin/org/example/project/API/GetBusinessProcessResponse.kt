package org.example.project.API

import kotlinx.serialization.Serializable
import org.example.project.model.BusinessProcess

@Serializable

data class GetBusinessProcessResponse(
    val status: String,
    val description: String,
    val businessProcessList: List<BusinessProcess>
)
