package com.gradysbooch.restaurant.ui.screens.order

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.gradysbooch.restaurant.model.Table
import com.gradysbooch.restaurant.viewmodel.OrderViewModel
import androidx.navigation.compose.navigate


@Composable
fun OrderScreen(
        orderViewModel: OrderViewModel,
        navController: NavHostController,
        tableId: String?,
        orderColor: String?,
) {
    if (tableId.isNullOrEmpty()) {
        navController.navigate("tables")
        return
    }
    orderViewModel.setTable(tableId)
    val isAllScreenSelected = orderColor.isNullOrEmpty() || orderColor == "#000"

    if (isAllScreenSelected) {
        orderViewModel.selectAllScreen()
    } else {
        orderViewModel.selectColor(orderColor ?: "#000")
    }

    val selectedTable by orderViewModel.table
            .collectAsState(initial = Table("-1", "name", 0, false))
    val bullets by orderViewModel.bulletList
            .collectAsState(initial = emptyList())
    Log.d("WHAT IS THIS", "All bullets: $bullets")

    // val selectedColor = remember { mutableStateOf(orderColor) }

    Column (
            modifier = Modifier.fillMaxSize()
    ){
        OrderScreenAppBar(navController, orderViewModel, selectedTable, tableId, orderColor, bullets).Show()
        Column {
            OrdersList(orderViewModel, orderColor).Show(isAllScreenSelected)
            if (! isAllScreenSelected) MenuItems(orderViewModel, orderColor).Show()
        }
    }
}
