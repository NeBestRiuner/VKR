package org.example.project.API.Data

import org.example.project.Model.BusinessProcess

data class GetBusinessProcessResponse(
    val status: String,
    val description: String,
    val businessProcessList: List<BusinessProcess>
)
