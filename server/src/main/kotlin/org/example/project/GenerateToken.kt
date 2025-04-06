package org.example.project
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

fun generateToken(login: String): String {
    val algorithm = Algorithm.HMAC256(Secret.secret)

    return JWT.create()
        .withIssuer("OAO Planning")
        .withAudience("Android AND Desktop APP")
        .withClaim("login", login)
        .withExpiresAt(Date(System.currentTimeMillis() + 360000000)) // 100 часов
        .sign(algorithm)
}