package com.gradysbooch.restaurant.ui.screens.tables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gradysbooch.restaurant.model.dto.TableDTO
import com.gradysbooch.restaurant.ui.values.RoundedButtonRowCard
import com.gradysbooch.restaurant.viewmodel.TableViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.gradysbooch.restaurant.ui.values.RoundedIconButton
import com.gradysbooch.restaurant.viewmodel.OrderViewModel


class TablesScreen(
        private val tableViewModel: TableViewModel,
        private val orderViewModel: OrderViewModel,
        private val screenNavController: NavHostController,
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(fontSize = 36.sp, text = "Restaurant")
            }
        })
    }

    @Composable
    fun TablesList() {
        val tables by tableViewModel.tables.collectAsState(initial = emptyList())

        LazyColumnFor(items = tables) {
            TableEntry(table = it)
        }
    }

    @Composable
    fun TableEntry(table: TableDTO) {
        RoundedButtonRowCard(
                border = BorderStroke(2.dp, MaterialTheme.colors.onSurface),
                onClick = {
                    screenNavController.navigate("orders/${table.id}")
                }
        ) {
            Text(modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp),
                    fontSize = 16.sp, text = table.name)

            RoundedIconButton(
                    modifier = Modifier.preferredSize(36.dp),
                    asset = Icons.Default.Delete,
                    onClick = {
                        orderViewModel.setTable(table.id)
                        orderViewModel.clearTable()
                    })
        }
    }
}


