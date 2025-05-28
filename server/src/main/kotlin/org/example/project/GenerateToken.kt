package org.example.project
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

fun generateToken(login: String): String {
    val algorithm = Algorithm.HMAC256(Secret.secret)

    return JWT.create()
        .withIssuer("OAO Planning") // установка издателя
        .withAudience("Android AND Desktop APP") // установка потребителя токена
        .withClaim("login", login) // установка логина
        .withExpiresAt(Date(System.currentTimeMillis() + 36000000)) // установка лимита времени
        .sign(algorithm)// подпись секретным ключом
}
