package com.gradysbooch.restaurant.ui.screens.tables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.viewModelScope
import com.gradysbooch.restaurant.model.dto.TableDTO
import com.gradysbooch.restaurant.ui.values.RoundedButtonRowCard
import com.gradysbooch.restaurant.viewmodel.OrderViewModel
import com.gradysbooch.restaurant.viewmodel.TableViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue

@Composable
fun TablesScreen() {
    Column {
        TablesScreenAppBar()
        TablesList()
    }
}


@Composable
fun TablesScreenAppBar() {
    TopAppBar(title = {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Restaurant")
        }
    })
}

@Composable
fun TablesList() {
    val tableViewModel = viewModel<TableViewModel>()
    val tables by tableViewModel.tables.collectAsState(initial = emptyList())

    LazyColumnFor(items = tables) {
        TableEntry(table = it)
    }
}

@Composable
fun TableEntry(table: TableDTO) {
    val orderViewModel = viewModel<OrderViewModel>()

    RoundedButtonRowCard(
            border = BorderStroke(1.dp, MaterialTheme.colors.onSurface),
            onClick = { orderViewModel.setTable(table.id) }
    ) {
        Text(text = table.name)
    }
}

