package org.example.project

import com.auth0.jwt.HeaderParams.ALGORITHM
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.crypto.spec.IvParameterSpec
import com.google.gson.Gson
import org.example.project.model.AccountsDepartment
import org.example.project.model.InviteToken
import java.nio.charset.Charset
import java.security.AlgorithmParameters
import javax.crypto.SecretKey

object AccountingSecurity {

    val secret = "zyxwvutsrqponmlk"
    val key: SecretKey = SecretKeySpec(secret.toByteArray(),"AES")
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")


    fun encode(name: String, author: String): String {
        val data = (name+"|"+author+"|"+System.currentTimeMillis()).toByteArray()
        //cipher.init(Cipher.ENCRYPT_MODE, key)
        //val encryptedData = cipher.doFinal(data)
        return Base64.getUrlEncoder().encodeToString(data)
    }

    fun decode(token: String): String{
        println(token)
        val cleanToken = token.filter {
            it in 'A'..'Z' ||
                    it in 'a'..'z' ||
                    it in '0'..'9' ||
                    it == '+' ||
                    it == '/' ||
                    it == '='
        }
        val decodedBytes = Base64.getUrlDecoder().decode(cleanToken)
        //cipher.init(Cipher.DECRYPT_MODE, key)
        //val originalString = cipher.doFinal(decodedBytes)
        return String(decodedBytes)
    }
    fun decodeToAccountDepartment(token: String):AccountsDepartment?{
        val parts = token.split("|")

        // Проверяем, что есть все 3 части
        if (parts.size != 3) {
            return null
        }

        val name = parts[0]
        val author = parts[1]
        val timestampStr = parts[2]

        // Парсим время в миллисекундах
        val timestamp = timestampStr.toLongOrNull() ?: return null

        // Получаем текущее время
        val currentTime = System.currentTimeMillis()

        // Проверяем, что разница меньше 24 часов (86400000 мс)
        if (currentTime - timestamp > 86_400_000) {
            return null
        }
        return AccountsDepartment(-1,name,"1970-01-01",author,0)
    }
}