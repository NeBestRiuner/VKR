package org.example.project.API

import kotlinx.serialization.Serializable

@Serializable

data class GetHierarchyResponse(
    val status:String,
    val description: String,
    var postRectangleAPIList: List<PostRectangleAPI>? = null,
    var lineList: List<LineAPI>? = null
)
