package com.gradysbooch.restaurant.ui.screens.order

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.gradysbooch.restaurant.model.Table
import com.gradysbooch.restaurant.model.dto.Bullet
import com.gradysbooch.restaurant.viewmodel.OrderViewModel
import kotlinx.coroutines.flow.map


@Composable
fun OrderScreen(
    navController: NavHostController,
    orderViewModel: OrderViewModel
) {
    Column (
            modifier = Modifier.fillMaxSize()
    ){
        var selectedTable = orderViewModel.table
                .collectAsState(initial = Table("-1", "name", 0, false))

        var selectedBullet = orderViewModel.bulletList
                .map { bullets -> bullets.firstOrNull { it.pressed } }
                .collectAsState(initial = Bullet("#000", false, false))

        var bullets = orderViewModel.bulletList
                .collectAsState(initial = emptyList())

        var isAllScreenSelected = orderViewModel.allScreen
                .collectAsState(initial = true)

        OrderScreenAppBar(navController, orderViewModel, selectedTable, selectedBullet, bullets).Show()
        Column {
            OrdersList(orderViewModel, selectedBullet).Show(isAllScreenSelected)
            if (!! isAllScreenSelected.value) MenuItems(orderViewModel, selectedBullet).Show()
        }
    }
}
