package org.example.project.Model

sealed class NavRoutes(val route:String) {
    object Enter: NavRoutes("enter")
    object Register: NavRoutes("register")
    object MainUserMenu: NavRoutes("main_menu")
}