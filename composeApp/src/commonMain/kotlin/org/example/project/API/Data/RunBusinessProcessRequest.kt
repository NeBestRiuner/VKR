package org.example.project.API.Data

import org.example.project.Model.AccountsDepartment
import org.example.project.Model.BusinessProcess

data class RunBusinessProcessRequest(
    val accountsDepartment: AccountsDepartment,
    val businessProcess: BusinessProcess,
    val postRectangleAPIList: List<PostRectangleAPI>
)
