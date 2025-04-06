package org.example.project.ViewModel

import androidx.lifecycle.ViewModel
import org.example.project.API.Data.CreateUserResponse
import org.example.project.API.Data.LoginUserResponse
import org.example.project.API.RetrofitClient.apiService
import org.example.project.Model.User
import org.example.project.Model.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthorizeViewModel:ViewModel() {
    var user: UserSession? = null
    fun sendCreateUser( login: String, password: String, onNavigateToEnter:()->Unit){
        val call = apiService.postCreateUser(User(login,password))
        call.enqueue(object : Callback<CreateUserResponse> {
            override fun onResponse(call: Call<CreateUserResponse>, response: Response<CreateUserResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if(result!=null){
                        if(result.status=="success"){
                            onNavigateToEnter()
                        }
                    }

                    // Обработка успешного ответа
                } else {
                    // Обработка ошибки
                }
            }

            override fun onFailure(call: Call<CreateUserResponse>, t: Throwable) {
                // Обработка ошибки сети
            }
        })
    }
    fun sendLoginUser(login:String, password:String, onNavigateToMainMenu:()->Unit){
        val call = apiService.getLoginUser(User(login,password))

        call.enqueue(object : Callback<LoginUserResponse> {
            override fun onResponse(call: Call<LoginUserResponse>, response: Response<LoginUserResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if(result.status=="success"){
                            user = UserSession(login,password,result.authToken)
                            onNavigateToMainMenu()
                        }
                        // Обработка успешного ответа
                    }
                    // Обработка ответа или с ошибкой логики на сервере
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