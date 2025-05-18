package org.example.project.ViewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.example.project.API.Data.CreateUserResponse
import org.example.project.API.Data.LoginUserResponse
import org.example.project.API.RetrofitClient.apiService
import org.example.project.Model.User
import org.example.project.Model.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class AuthorizeViewModel:ViewModel() {
    var user = mutableStateOf(
        UserSession("","","")
    )

    var errorMessage = mutableStateOf("")


    fun sendCreateUser( login: String, password: String, onNavigateToEnter:()->Unit){
        try{
            val call = apiService.postCreateUser(User(login,password))
            call.enqueue(object : Callback<CreateUserResponse> {
                override fun onResponse(call: Call<CreateUserResponse>, response: Response<CreateUserResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null) {
                            if(result.status=="200"){
                                onNavigateToEnter()
                            }else{
                                errorMessage.value = "Ошибка сервера"
                                println(errorMessage.value)
                            }
                        }
                        // Обработка успешного ответа
                    } else {
                        // Обработка ошибки
                        errorMessage.value = "Ошибка сервера"
                    }
                }

                override fun onFailure(call: Call<CreateUserResponse>, t: Throwable) {
                    // Обработка ошибки сети
                    errorMessage.value = "Ошибка сети"
                }
            })
        }catch (e: SocketTimeoutException){
            errorMessage.value = "Ошибка сети"
        }
    }
    fun sendLoginUser(login:String, password:String, onNavigateToMainMenu:()->Unit){
        val call = apiService.getLoginUser(User(login,password))

        call.enqueue(object : Callback<LoginUserResponse> {
            override fun onResponse(call: Call<LoginUserResponse>, response: Response<LoginUserResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if(result.status=="200"){
                            user.value = UserSession(login,password,result.authToken)
                            onNavigateToMainMenu()
                        }
                    }
                    // Обработка успешного ответа
                } else {
                    // Обработка ошибки
                }
            }

            override fun onFailure(call: Call<LoginUserResponse>, t: Throwable) {
                // Обработка ошибки сети
            }
        })

    }
}