package com.gradysbooch.restaurant.ui.screens.order

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.viewModel
import com.gradysbooch.restaurant.viewmodel.OrderViewModel


@Composable
fun OrderScreen() {
    val orderViewModel = viewModel<OrderViewModel>()

    Column (
            modifier = Modifier.fillMaxSize()
    ){
        orderViewModel.selectAllScreen()
        OrderScreenAppBar()
        Column {
            OrdersList()
            MenuSubScreen()
        }
    }
}
