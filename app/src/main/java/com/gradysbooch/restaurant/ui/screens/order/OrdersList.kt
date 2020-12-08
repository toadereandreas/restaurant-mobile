package com.gradysbooch.restaurant.ui.screens.order

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.viewModelScope
import com.gradysbooch.restaurant.model.dto.AllScreenItem
import com.gradysbooch.restaurant.model.dto.Bullet
import com.gradysbooch.restaurant.model.dto.MenuItemDTO
import com.gradysbooch.restaurant.ui.values.*
import com.gradysbooch.restaurant.viewmodel.OrderViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@Composable
fun OrdersList() {
    val orderViewModel = viewModel<OrderViewModel>()
    var isAllScreenSelected : Boolean = true
    orderViewModel.viewModelScope.launch {
        orderViewModel.allScreen.collect { isAllScreenSelected = it }
    }

    if(isAllScreenSelected) {
        AllCustomerItemsAndNotes()
    } else OneCustomerItemsAndNote()
}

@OptIn(ExperimentalLazyDsl::class)
@Composable
fun AllCustomerItemsAndNotes() {
    val orderViewModel = viewModel<OrderViewModel>()
    var allOrderItems : List<AllScreenItem> = ArrayList()
    var allOrderNotes : List<Pair<String, String>> = ArrayList()
    orderViewModel.viewModelScope.launch {
        orderViewModel.allScreenMenuItems.collect { allOrderItems = it }
        orderViewModel.allScreenNotes.collect { allOrderNotes = it }
    }

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


@OptIn(ExperimentalLazyDsl::class)
@Composable
fun OneCustomerItemsAndNote() {
    val orderViewModel = viewModel<OrderViewModel>()
    var selectedOrderItems : List<Pair<MenuItemDTO, Int>> = ArrayList()
    var selectedOrderNote: String = "add note..."
    var selectedBullet = Bullet("", false, false)
    orderViewModel.viewModelScope.launch {
        orderViewModel.chosenItems.collect { selectedOrderItems = it }
        selectedOrderNote = orderViewModel.getNote()
        orderViewModel.bulletList.collect { bullets ->
            val foundBullet = bullets.find { it.pressed }
            if (foundBullet != null) selectedBullet = foundBullet
        }
    }

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
