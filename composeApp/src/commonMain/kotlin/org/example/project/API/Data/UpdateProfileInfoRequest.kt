package org.example.project.API.Data

import org.example.project.Model.ProfileInfo
import org.example.project.Model.User


data class UpdateProfileInfoRequest(
    val user: User,
    val profileInfo: ProfileInfo
)
