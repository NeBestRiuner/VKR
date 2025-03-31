package org.example.project.API

import kotlinx.serialization.Serializable


@Serializable
data class UserAPI(val login:String, val password: String)
