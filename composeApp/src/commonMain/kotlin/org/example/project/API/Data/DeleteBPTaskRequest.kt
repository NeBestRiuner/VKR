package org.example.project.API.Data

import org.example.project.Model.AccountsDepartment
import org.example.project.Model.BPTask

data class DeleteBPTaskRequest(
    val accountsDepartment: AccountsDepartment,
    val bpTask: BPTask
)
