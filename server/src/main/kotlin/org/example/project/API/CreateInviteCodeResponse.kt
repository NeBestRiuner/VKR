package org.example.project.API

import kotlinx.serialization.Serializable

@Serializable

data class CreateInviteCodeResponse(val status: String, val description: String, val inviteCode: String)
