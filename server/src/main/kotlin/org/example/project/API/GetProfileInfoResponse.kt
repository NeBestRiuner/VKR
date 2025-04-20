package org.example.project.API

import kotlinx.serialization.Serializable

@Serializable
data class GetProfileInfoResponse(val status: String,val description: String,  val getProfileInfo: GetProfileInfo?)
