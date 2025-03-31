package org.example.project

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.project.API.APIService
import org.example.project.API.Data.GetAllUsersResponse
import org.example.project.API.Data.HelloWorldResponse
import org.example.project.API.RetrofitClient.apiService
import org.example.project.API.RetrofitClientClear.retrofit
import org.example.project.Model.NavRoutes
import org.example.project.Model.User
import org.example.project.View.EnterScreen
import org.example.project.View.MainUserMenuScreen
import org.example.project.View.RegisterScreen
import org.example.project.ViewModel.AuthorizeViewModel
import org.example.project.ViewModel.MainDepartmentViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val authorizeViewModel = AuthorizeViewModel()
            val mainDepartmentViewModel = MainDepartmentViewModel()
            NavHost(navController = navController, startDestination = NavRoutes.Enter.route){
                composable(NavRoutes.Enter.route){
                    EnterScreen(
                        onNavigateToRegister = {navController.navigate(NavRoutes.Register.route)},
                        onCheckUserAuth = authorizeViewModel::sendLoginUser,
                        onNavigateToMainMenu = {
                            if(authorizeViewModel.user!=null){
                                navController.navigate(NavRoutes.MainUserMenu.route)
                            }
                        }
                    )
                }
                composable(NavRoutes.Register.route){
                    RegisterScreen(
                        onNavigateToEnter = { navController.navigate(NavRoutes.Enter.route) },
                        onRegistration = AuthorizeViewModel()::sendCreateUser)
                }
                composable(NavRoutes.MainUserMenu.route){
                    MainUserMenuScreen(onCreateDepartment = {
                        mainDepartmentViewModel.SendCreateDepartment(
                            user = User(authorizeViewModel.user!!.login,
                                authorizeViewModel.user!!.password),
                            accountsDepartment = mainDepartmentViewModel.createdDepartment
                        )
                    })
                }
            }
        }
    }
}



fun sendGetHelloWorld(){
  //  val apiService2 = retrofit.create(APIService::class.java)
    val call = apiService.getHelloWorld()
    call.enqueue(object : Callback<HelloWorldResponse> {
        override fun onResponse(call: Call<HelloWorldResponse>, response: Response<HelloWorldResponse>) {
            if (response.isSuccessful) {
                val result = response.body()
                Log.d("TAG",result.toString())
                // Обработка успешного ответа
            } else {
                // Обработка ошибки
            }
        }

        override fun onFailure(call: Call<HelloWorldResponse>, t: Throwable) {
            // Обработка ошибки сети
        }
    })
}
fun sendGetAllUsers(){
    //  val apiService2 = retrofit.create(APIService::class.java)
    val call = apiService.getAllUsers()
    call.enqueue(object : Callback<GetAllUsersResponse> {
        override fun onResponse(call: Call<GetAllUsersResponse>, response: Response<GetAllUsersResponse>) {
            if (response.isSuccessful) {
                val result = response.body()
                Log.d("TAG",result.toString())
                // Обработка успешного ответа
            } else {
                // Обработка ошибки
            }
        }

        override fun onFailure(call: Call<GetAllUsersResponse>, t: Throwable) {
            // Обработка ошибки сети
        }
    })
}