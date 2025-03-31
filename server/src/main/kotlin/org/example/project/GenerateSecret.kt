package org.example.project

import java.security.SecureRandom
import java.util.Base64

fun generate256BitSecret(): String {
    val random = SecureRandom()
    val bytes = ByteArray(32) // 256 бит
    random.nextBytes(bytes)
    return Base64.getEncoder().encodeToString(bytes)
}