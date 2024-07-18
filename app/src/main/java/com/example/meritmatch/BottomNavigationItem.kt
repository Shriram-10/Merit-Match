package com.example.meritmatch

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AllInclusive
import androidx.compose.material.icons.outlined.BookmarkAdded
import androidx.compose.material.icons.outlined.CreateNewFolder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val title: String,
    val route : String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
)

val items = listOf(
    BottomNavigationItem(
        route = "available_tasks",
        title = "Available Tasks",
        unselectedIcon = Icons.Outlined.AllInclusive,
        selectedIcon = Icons.Filled.AllInclusive
    ),

    BottomNavigationItem(
        route = "reserved_tasks",
        title = "Reserved Tasks",
        unselectedIcon = Icons.Outlined.BookmarkAdded,
        selectedIcon = Icons.Filled.BookmarkAdded
    ),

    BottomNavigationItem(
        route = "posted_tasks",
        title = "Posted Tasks",
        unselectedIcon = Icons.Outlined.CreateNewFolder,
        selectedIcon = Icons.Filled.CreateNewFolder
    )
)