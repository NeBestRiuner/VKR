package org.example.project.Model

data class UserSession(
    var login:String,
    var password: String,
    val token: String
)
