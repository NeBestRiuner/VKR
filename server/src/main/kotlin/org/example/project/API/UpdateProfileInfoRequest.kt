package org.example.project.API

import kotlinx.serialization.Serializable
import org.example.project.model.ProfileInfo

@Serializable
data class UpdateProfileInfoRequest(
    val user: UserAPI,
    val profileInfo: ProfileInfo
)
