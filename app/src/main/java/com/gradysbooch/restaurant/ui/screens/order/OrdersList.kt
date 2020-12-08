package com.gradysbooch.restaurant.ui.screens.order

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.gradysbooch.restaurant.model.dto.AllScreenItem
import com.gradysbooch.restaurant.model.dto.Bullet
import com.gradysbooch.restaurant.model.dto.MenuItemDTO
import com.gradysbooch.restaurant.ui.values.*
import com.gradysbooch.restaurant.viewmodel.OrderViewModel
import kotlinx.coroutines.flow.map


@Composable
fun OrdersList() {
    val orderViewModel = viewModel<OrderViewModel>()
    val isAllScreenSelected by orderViewModel.allScreen
            .collectAsState(initial = true)

    if(isAllScreenSelected) {
        AllCustomerItemsAndNotes()
    } else OneCustomerItemsAndNote()
}

@Composable
fun AllCustomerItemsAndNotes() {
    val orderViewModel = viewModel<OrderViewModel>()
    val allOrderItems by orderViewModel.allScreenMenuItems
            .collectAsState(initial = emptyList())
    val allOrderNotes by orderViewModel.allScreenNotes
            .collectAsState(initial = emptyList())

    LazyColumn {
        items(allOrderItems) { AllCustomerItem(it) }
        items(allOrderNotes) { AllCustomerNote(it) }
    }
}

@Composable
fun AllCustomerItem(item: AllScreenItem) {
    val expanded = remember { mutableStateOf(false) }
    RoundedButtonColumnCard(
        border = BorderStroke(1.dp, MaterialTheme.colors.onSurface),
        onClick = { expanded.value = !expanded.value }
    ) {
        RoundedRowCard{
            Text(text = item.menuItem.name)
            Text(text = item.number.toString())
        }
        if (expanded.value) {
            RoundedRowCard(
                modifier = Modifier.padding(8.dp, 0.dp).fillMaxWidth()
            ) {
                LazyRowFor(items = item.orders) {quantity ->
                    Surface(
                        shape = CircleShape,
                        color = getColor(quantity.first),
                        modifier = Modifier.padding(4.dp, 0.dp)
                    ) {
                        Text(text = quantity.second.toString(), modifier = Modifier.padding(10.dp, 4.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun AllCustomerNote(note: Pair<String, String>) {
    RoundedRowCard(
            color = getColor(note.first)
    ){
        Text(text = note.second)
    }
}

@Composable
fun OneCustomerItemsAndNote() {
    val orderViewModel = viewModel<OrderViewModel>()
    val selectedOrderItems by orderViewModel.chosenItems
            .collectAsState(initial = emptyList())
    // todo - getNote is suspend
    var selectedOrderNote = ""
    LaunchedEffect(subject = selectedOrderNote, block = {
        selectedOrderNote = orderViewModel.getNote()
    })
    val selectedBullet by orderViewModel.bulletList
            .map { bullets -> bullets.first { it.pressed } }
            .collectAsState(initial = Bullet("#000", false, false))

    LazyColumn{
        items(selectedOrderItems) { OneCustomerItem(it, getColor(selectedBullet.color)) }
        item{
            val customerNote = remember { mutableStateOf(selectedOrderNote) }
            TextField(
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                backgroundColor = getColor(selectedBullet.color),
                value = customerNote.value,
                onValueChange = {
                    customerNote.value = it
                    orderViewModel.changeNote(it)
                })
        }
    }
}

@Composable
fun OneCustomerItem(item: Pair<MenuItemDTO, Int>, color: Color) {
    val orderViewModel = viewModel<OrderViewModel>()
    RoundedRowCard(
            color = color
    ) {
        Text(
                text = item.first.name,
                modifier = Modifier.padding(16.dp, 0.dp)
        )

        Row {
            val number = remember { mutableStateOf(item.second) }
            RoundedIconButton(
                    modifier = Modifier.then(Modifier.preferredSize(24.dp)),
                    asset = Icons.Filled.KeyboardArrowDown,
                    onClick = {
                        if (number.value > 0) {
                            number.value -= 1
                            orderViewModel.changeNumber(item.first.id, number.value)
                        }
                    })
            Text(text = number.value.toString())
            RoundedIconButton(
                    modifier = Modifier.then(Modifier.preferredSize(24.dp)),
                    asset = Icons.Filled.KeyboardArrowUp,
                    onClick = {
                        number.value += 1
                        orderViewModel.changeNumber(item.first.id, number.value)
                    })
        }
    }
}
