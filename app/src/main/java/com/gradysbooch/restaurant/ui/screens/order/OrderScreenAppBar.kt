package com.gradysbooch.restaurant.ui.screens.order

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.gradysbooch.restaurant.model.Table
import com.gradysbooch.restaurant.model.dto.Bullet
import com.gradysbooch.restaurant.ui.values.*
import com.gradysbooch.restaurant.viewmodel.OrderViewModel
import kotlinx.coroutines.flow.map
import androidx.compose.runtime.getValue
import androidx.compose.ui.gesture.longPressGestureFilter
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate

class OrderScreenAppBar(
    private val navController: NavHostController
) {
    @Composable
    fun Show() {
        val orderViewModel = viewModel<OrderViewModel>()
        val selectedBullet by orderViewModel.bulletList
                .map { bullets -> bullets.firstOrNull { it.pressed } }
                .collectAsState(initial = Bullet("#000", false, false))

        TopAppBar(
                backgroundColor = getColor(selectedBullet?.color),
                modifier = Modifier.height(120.dp),
                title = {
                    Column{
                        OrderScreenTopRow()
                        CustomerNavigationRow()
                    }
                })
    }

    @Composable
    fun OrderScreenTopRow() {
        val orderViewModel = viewModel<OrderViewModel>()
        val selectedTable by orderViewModel.table
                .collectAsState(initial = Table("-1", "name", 0, false))

        RoundedRowCard (
                color = Color.Transparent,
                modifier = Modifier.padding(8.dp, 0.dp).fillMaxWidth()
        ){
            RoundedIconButton(
                    color = Color.Transparent,
                    tint = MaterialTheme.colors.secondary,
                    asset = Icons.Filled.ArrowBack,
                    onClick = {
                        orderViewModel.setTable("-1");
                        navController.navigate("tables")
                    })

            Text(text = "${selectedTable.name} (#${selectedTable.code})")

            val isChecked by orderViewModel.requiresAttention
                    .collectAsState(initial = false)
            RoundedIconButton(
                    color = Color.Transparent,
                    tint = MaterialTheme.colors.secondary,
                    asset = if (isChecked) Icons.Filled.CheckCircle else Icons.Filled.Check,
                    onClick = {
                        // Check or Uncheck Table
                        orderViewModel.clearAttention()
                    })
        }
    }

    @Composable
    fun CustomerNavigationRow() {
        Surface(
                modifier = Modifier.padding(8.dp, 0.dp).fillMaxWidth(),
                shape = RoundedCornerShape(20)
        ) {
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
            ) {
                WholeOrderNavigationButton()
                AddCustomerButton()
                AllCustomersNavigationButtons()
            }
        }
    }


    @Composable
    fun WholeOrderNavigationButton() {
        val orderViewModel = viewModel<OrderViewModel>()

        RoundedIconButton(
                modifier = Modifier.padding(4.dp, 0.dp),
                asset = Icons.Filled.Check,
                onClick = {
                    orderViewModel.selectAllScreen()
                })
    }

    @Composable
    fun AddCustomerButton() {
        val orderViewModel = viewModel<OrderViewModel>()
        RoundedIconButton(
                modifier = Modifier.padding(4.dp, 0.dp),
                asset = Icons.Filled.Add,
                onClick = {
                    // todo Maybe select added customer?
                    orderViewModel.addBullet()
                    Log.d("UndoTag", "Trying to add bullet")
                })
    }

    @Composable
    fun AllCustomersNavigationButtons() {
        val orderViewModel = viewModel<OrderViewModel>()
        val bullets by orderViewModel.bulletList
                .collectAsState(initial = emptyList())

        LazyRowFor(items = bullets) {
            CustomerNavigationButton(bullet = it)
        }
    }

    @Composable
    fun CustomerNavigationButton(bullet: Bullet) {
        val orderViewModel = viewModel<OrderViewModel>()

        RoundedIconButton(
                modifier = Modifier.padding(4.dp, 0.dp)
                    .longPressGestureFilter {
                                            // todo lock button
                    },
                color = getColor(bullet.color),
                tint = MaterialTheme.colors.primary,
                asset = if (bullet.locked) Icons.Filled.Lock else Icons.Filled.Clear,
                onClick = {
                    orderViewModel.selectColor(bullet.color)
                })
    }

}


