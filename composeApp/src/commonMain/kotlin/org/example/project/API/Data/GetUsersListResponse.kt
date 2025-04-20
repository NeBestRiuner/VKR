package org.example.project.API.Data

import org.example.project.Model.User

data class GetUsersListResponse(val status: String,
                                val description : String,
                                val userList: List<User>)
