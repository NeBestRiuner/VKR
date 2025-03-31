package org.example.project.API.Data

import org.example.project.Model.AccountsDepartment
import org.example.project.Model.User

data class CreateDepartmentRequest(val user: User, val accountsDepartment: AccountsDepartment)
