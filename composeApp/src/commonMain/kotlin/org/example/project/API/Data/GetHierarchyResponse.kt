package org.example.project.API.Data

data class GetHierarchyResponse(
    val status:String,
    val description: String,
    val postRectangleAPIList: List<PostRectangleAPI>? = null,
    val lineList: List<LineAPI>? = null
)
