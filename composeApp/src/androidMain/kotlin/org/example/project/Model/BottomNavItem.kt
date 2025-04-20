package org.example.project.Model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route:String, val title: String, val icon: ImageVector) {
    object Calendar : BottomNavItem("calendar", "Календарь", Icons.Default.DateRange)
    object Hierarchy : BottomNavItem("hierarchy", "Иерархия бухгалтерии", Icons.Default.Home)
    object BusinessProcess : BottomNavItem("business_process", "Бизнес-процессы", Icons.AutoMirrored.Default.List)
    object Analytic : BottomNavItem("analytic", "Аналитика", Icons.Default.Search)
    object Settings : BottomNavItem("settings", "Настройки", Icons.Default.Settings)
}