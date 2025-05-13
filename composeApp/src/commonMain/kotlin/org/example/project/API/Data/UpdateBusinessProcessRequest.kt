package org.example.project.API.Data

import org.example.project.Model.AccountsDepartment
import org.example.project.Model.BusinessProcess

data class UpdateBusinessProcessRequest(
    val accountsDepartment: AccountsDepartment,
    val businessProcess: BusinessProcess
)
