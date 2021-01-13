package com.gradysbooch.restaurant.ui.screens.tables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gradysbooch.restaurant.model.dto.TableDTO
import com.gradysbooch.restaurant.ui.values.RoundedButtonRowCard
import com.gradysbooch.restaurant.viewmodel.OrderViewModel
import com.gradysbooch.restaurant.viewmodel.TableViewModel
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate


class TablesScreen(
    private val tableViewModel: TableViewModel,
    private val orderViewModel: OrderViewModel,
    private val navController: NavHostController,
) {
    @Composable
    fun Show() {
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
        // val tableViewModel = viewModel<TableViewModel>()
        val tables by tableViewModel.tables.collectAsState(initial = emptyList())

        LazyColumnFor(items = tables) {
            TableEntry(table = it)
        }
    }

    @Composable
    fun TableEntry(table: TableDTO) {

        RoundedButtonRowCard(
                border = BorderStroke(1.dp, MaterialTheme.colors.onSurface),
                onClick = {
                    // orderViewModel.setTable(table.id)
                    navController.navigate("orders/${table.id}/#000")
                }
        ) {
            Text(text = table.name)
        }
    }
}


