package com.gradysbooch.restaurant.ui.screens.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gradysbooch.restaurant.model.Table
import com.gradysbooch.restaurant.model.dto.Bullet
import com.gradysbooch.restaurant.ui.values.*
import com.gradysbooch.restaurant.viewmodel.OrderViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.gesture.longPressGestureFilter
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate

class OrderScreenAppBar(
        private val navController: NavHostController,
        private val orderViewModel: OrderViewModel,
        private var selectedTable: State<Table>,
        private var selectedBullet: State<Bullet?>,
        private var bullets: State<List<Bullet>>
) {
    @Composable
    fun Show() {
        /*
        val selectedBullet by orderViewModel.bulletList
                .map { bullets -> bullets.firstOrNull { it.pressed } }
                .collectAsState(initial = Bullet("#000", false, false))
         */

        TopAppBar(
                backgroundColor = getColor(selectedBullet.value?.color),
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
        /*
        val selectedTable by orderViewModel.table
                .collectAsState(initial = Table("-1", "name", 0, false))
         */

        RoundedRowCard (
                color = Color.Transparent,
                modifier = Modifier.padding(8.dp, 0.dp).fillMaxWidth()
        ){
            RoundedIconButton(
                    color = Color.Transparent,
                    tint = MaterialTheme.colors.secondary,
                    asset = Icons.Filled.ArrowBack,
                    onClick = {
                        orderViewModel.setTable("-1")
                        navController.navigate("tables")
                    })

            Text(text = "${selectedTable.value.name} (#${selectedTable.value.code})")

            val isChecked by orderViewModel.requiresAttention
                    .collectAsState(initial = false)
            RoundedIconButton(
                    color = Color.Transparent,
                    tint = MaterialTheme.colors.secondary,
                    asset = if (isChecked) Icons.Filled.CheckCircle else Icons.Filled.Check,
                    onClick = {
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
        RoundedIconButton(
                modifier = Modifier.padding(4.dp, 0.dp),
                asset = Icons.Filled.Check,
                onClick = {
                    orderViewModel.selectAllScreen()
                })
    }

    @Composable
    fun AddCustomerButton() {
        RoundedIconButton(
                modifier = Modifier.padding(4.dp, 0.dp),
                asset = Icons.Filled.Add,
                onClick = {
                    try { orderViewModel.addBullet()
                    } catch (e : NoSuchElementException) {}
                })
    }

    @Composable
    fun AllCustomersNavigationButtons() {
        /*
        val bullets by orderViewModel.bulletList
                .collectAsState(initial = emptyList())
         */

        LazyRowFor(items = bullets.value) {
            CustomerNavigationButton(bullet = it)
        }
    }

    @Composable
    fun CustomerNavigationButton(bullet: Bullet) {
        /*
        val selectedTable by orderViewModel.table
                .collectAsState(initial = Table("-1", "name", 0, false))
         */

        RoundedIconButton(
                modifier = Modifier.padding(4.dp, 0.dp)
                    .longPressGestureFilter {
                        if (bullet.locked) { orderViewModel.unlockOrder(selectedTable.value.tableUID, bullet.color)
                        } else { orderViewModel.lockOrder(selectedTable.value.tableUID, bullet.color) }
                    },
                color = getColor(bullet.color),
                tint = MaterialTheme.colors.primary,
                asset = if (bullet.locked) Icons.Filled.Lock else Icons.Filled.Clear,
                onClick = {
                    orderViewModel.selectColor(bullet.color)
                }
        )
    }

}


