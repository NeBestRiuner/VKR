package org.example.project

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*
import io.ktor.server.application.*

fun Application.configureSecurity() {
    val secret = generate256BitSecret() // Замените на реальный секрет
    val issuer = "OAO Planning"
    val audience = "Android AND Desktop APP"
    val realm = "Command Planning APP"

    install(Authentication) {
        jwt("auth-jwt") {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("login").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}