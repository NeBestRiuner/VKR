package org.example.project.API

import kotlinx.serialization.Serializable
import org.example.project.model.AccountsDepartment

@Serializable

data class SendHierarchyRequest(
    var accountsDepartment: AccountsDepartment,
    var postRectangleAPIList: List<PostRectangleAPI>,
    var lineList: List<LineAPI>
)
