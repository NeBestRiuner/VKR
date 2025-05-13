package org.example.project.API

import kotlinx.serialization.Serializable
import org.example.project.model.AccountsDepartment
import org.example.project.model.BusinessProcess

@Serializable

data class RunBusinessProcessRequest(
    val accountsDepartment: AccountsDepartment,
    val businessProcess: BusinessProcess,
    val postRectangleAPIList: List<PostRectangleAPI>
)
