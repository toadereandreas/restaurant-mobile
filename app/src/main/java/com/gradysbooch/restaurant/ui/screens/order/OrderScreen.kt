package com.gradysbooch.restaurant.ui.screens.order

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun OrderScreen(selectedTable: MutableState<String>) {
    Column (
            modifier = Modifier.fillMaxSize()
    ){
        // todo Remove Hardcoding
        val selectedCustomer = remember { mutableStateOf(Color.Unspecified) }
        OrderScreenAppBar(selectedTable, selectedCustomer)
        // todo Should be ScrollableColumn, but that's not allowed
        Column {
            OrdersList(selectedTable, selectedCustomer)
            MenuSubScreen(selectedCustomer.value)
        }
    }
}
