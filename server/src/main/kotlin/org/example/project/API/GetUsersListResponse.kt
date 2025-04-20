package org.example.project.API

import kotlinx.serialization.Serializable

@Serializable

data class GetUsersListResponse(val status: String,
                                val description : String,
                                val userList: List<UserAPI>
)
