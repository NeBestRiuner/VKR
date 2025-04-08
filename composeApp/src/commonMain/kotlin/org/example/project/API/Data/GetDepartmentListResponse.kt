package org.example.project.API.Data

import org.example.project.Model.AccountsDepartment

data class GetDepartmentListResponse(val status:String, val departmentList:List<AccountsDepartment>)
