package org.example.project.API.Data

import org.example.project.Model.AccountsDepartment
import org.example.project.Model.BusinessProcess

data class DeleteBPRequest(
    val accountsDepartment: AccountsDepartment,
    val businessProcess: BusinessProcess
)
