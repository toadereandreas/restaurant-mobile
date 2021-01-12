package com.gradysbooch.restaurant.ui.screens.order

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.gradysbooch.restaurant.viewmodel.OrderViewModel


@Composable
fun OrderScreen(
    navController: NavHostController,
    orderViewModel: OrderViewModel
) {
    Column (
            modifier = Modifier.fillMaxSize()
    ){
        OrderScreenAppBar(navController, orderViewModel).Show()
        Column {
            OrdersList(orderViewModel).Show()
            MenuItems(orderViewModel).Show()
        }
    }
}
