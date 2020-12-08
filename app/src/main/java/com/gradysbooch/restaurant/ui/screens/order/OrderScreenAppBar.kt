package com.gradysbooch.restaurant.ui.screens.order

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.viewModelScope
import com.gradysbooch.restaurant.model.Table
import com.gradysbooch.restaurant.model.dto.Bullet
import com.gradysbooch.restaurant.ui.values.*
import com.gradysbooch.restaurant.viewmodel.OrderViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@Composable
fun OrderScreenAppBar() {
    val orderViewModel = viewModel<OrderViewModel>()
    var selectedBullet = Bullet("", false, false)
    orderViewModel.viewModelScope.launch {
        orderViewModel.bulletList.collect { bullets ->
            val foundBullet = bullets.find { it.pressed }
            if (foundBullet != null) selectedBullet = foundBullet
        }
    }

    TopAppBar(
            backgroundColor = getColor(selectedBullet.color),
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
    var selectedTable: Table = Table("-1", "name", 0, false)
    orderViewModel.viewModelScope.launch {
        orderViewModel.table.collect { selectedTable = it }
    }

    RoundedRowCard (
            color = Color.Transparent,
            modifier = Modifier.padding(8.dp, 0.dp).fillMaxWidth()
    ){
        RoundedIconButton(
                color = Color.Transparent,
                tint = MaterialTheme.colors.secondary,
                asset = Icons.Filled.ArrowBack,
                onClick = {
                    // Go back
                    orderViewModel.setTable("-1");
                })

        Text(text = "${selectedTable.name} (#${selectedTable.code})")

        var isChecked: Boolean = false
        orderViewModel.viewModelScope.launch {
            orderViewModel.requiresAttention.collect {
                isChecked = it
            }
        }
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
            })
}

@Composable
fun AllCustomersNavigationButtons() {
    val orderViewModel = viewModel<OrderViewModel>()
    var bullets : List<Bullet> = ArrayList()
    orderViewModel.viewModelScope.launch {
        orderViewModel.bulletList.collect { bullets = it }
    }

    LazyRowFor(items = bullets) {
        CustomerNavigationButton(bullet = it)
    }
}

@Composable
fun CustomerNavigationButton(bullet: Bullet) {
    val orderViewModel = viewModel<OrderViewModel>()

    RoundedIconButton(
            modifier = Modifier.padding(4.dp, 0.dp),
            color = getColor(bullet.color),
            tint = MaterialTheme.colors.primary,
            asset = Icons.Filled.Lock,
            onClick = {
                orderViewModel.selectColor(bullet.color)
            })
}
