package org.example.project
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

fun generateToken(login: String): String {
    val secret = generate256BitSecret()
    val algorithm = Algorithm.HMAC256(secret)

    return JWT.create()
        .withIssuer("your_issuer")
        .withAudience("your_audience")
        .withClaim("login", login)
        .withExpiresAt(Date(System.currentTimeMillis() + 3600000)) // 1 час
        .sign(algorithm)
}