package org.example.project.API.Data

import org.example.project.Model.AccountsDepartment

data class SendHierarchyRequest(
    var accountsDepartment: AccountsDepartment,
    var postRectangleAPIList: List<PostRectangleAPI>,
    var lineList: List<LineAPI>
)
