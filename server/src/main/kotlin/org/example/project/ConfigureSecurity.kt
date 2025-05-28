package org.example.project

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*
import io.ktor.server.application.*

fun Application.configureSecurity() {
    val secret = Secret.secret // секретный ключ для подписи токена
    val issuer = "OAO Planning" //установка названия издателя
    val audience = "Android AND Desktop APP" // установка названия приложения потребителя

    install(Authentication) { // установка аутентификации
        jwt("auth-jwt") { // создание JWT провайдера
            verifier( //проверка токена
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )
            validate { credential -> // проверка содержимого токена - логин
                if (credential.payload.getClaim("login").asString() != "") {//проверка логина
                    JWTPrincipal(credential.payload) // объект токена
                } else {
                    null
                }
            }
        }
    }
}
