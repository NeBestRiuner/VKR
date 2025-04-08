package org.example.project.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.example.project.Model.BottomNavItem
import org.example.project.View.Box.AnalyticBox
import org.example.project.View.Box.BusinessProcessBox
import org.example.project.View.Box.HomeScreen
import org.example.project.View.Box.IerarchyBox
import org.example.project.View.Box.SettingsBox

@Composable
fun TaskManagerScreen(onLeaveDepartment:()->Unit){
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column{
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().height(60.dp).background(Color.LightGray)){
                IconButton(onClick = onLeaveDepartment) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Кнопка выхода")
                }
                Text("Бухгалтерия")
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Filled.Notifications, contentDescription = "Уведомления")
                }
            }
            Box(modifier = Modifier.padding(innerPadding)) {
                NavigationGraph(navController = navController)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Calendar,
        BottomNavItem.Ierarchy,
        BottomNavItem.BusinessProcess,
        BottomNavItem.Analytic,
        BottomNavItem.Settings
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation {
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title, style = TextStyle(fontSize = 12.sp,
                    textAlign = TextAlign.Center) ) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Очистка стека навигации при выборе элемента
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Calendar.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(BottomNavItem.Calendar.route) { HomeScreen() }
        composable(BottomNavItem.Ierarchy.route) { IerarchyBox() }
        composable(BottomNavItem.BusinessProcess.route) { BusinessProcessBox() }
        composable(BottomNavItem.Analytic.route) { AnalyticBox() }
        composable(BottomNavItem.Settings.route) { SettingsBox() }
    }
}









@Composable
@Preview
fun TaskManagerScreenProfile(){
    TaskManagerScreen({})
}