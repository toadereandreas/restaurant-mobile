package com.gradysbooch.restaurant.ui.screens.order

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController


@Composable
fun OrderScreen(
    navController: NavHostController
) {
    Column (
            modifier = Modifier.fillMaxSize()
    ){
        OrderScreenAppBar(navController).Show()
        Column {
            OrdersList().Show()
            MenuItems().Show()
        }
    }
}
