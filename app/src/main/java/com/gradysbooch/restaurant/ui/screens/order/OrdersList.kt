package com.gradysbooch.restaurant.ui.screens.order

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gradysbooch.restaurant.ui.values.*


@Composable
fun OrdersList(selectedTable: MutableState<String>, selectedCustomer: MutableState<Color>) {
    if(selectedCustomer.value == Color.Unspecified) {
        AllCustomerOrders(selectedTable.value)
        AllCustomerNotes(selectedTable)
    } else OneCustomerOrdersAndNote(selectedCustomer)
}

@Composable
fun AllCustomerOrders(table: String) {
    // todo Remove Hardcoding
    val allItems = listOf(Pair("Item-1", 21), Pair("Item-2", 21), Pair("Item-3", 21),
            Pair("Item-4", 21), Pair("Item-5", 21))
    LazyColumnFor(items = allItems) {item ->
        val expanded = remember { mutableStateOf(false) }
        RoundedButtonColumnCard(
                border = BorderStroke(1.dp, MaterialTheme.colors.onSurface),
                onClick = { expanded.value = !expanded.value }
        ) {
            AllCustomersItems(expanded.value, item)
        }
    }
}

@Composable
fun AllCustomersItems(expanded: Boolean, item: Pair<String, Int>) {
    RoundedRowCard {
        Text(text = item.first)
        Text(text = item.second.toString())
    }
    if (expanded) {
        // todo Remove Hardcoding
        val quantities = listOf(Pair(red, 1), Pair(green, 2), Pair(blue, 3),
                Pair(yellow, 4), Pair(cyan, 5), Pair(magenta, 6))
        RoundedRowCard (
                modifier = Modifier.padding(8.dp, 0.dp).fillMaxWidth()
        ){
            LazyRowFor(items = quantities) {quantity ->
                Surface(
                        shape = CircleShape,
                        color = quantity.first,
                        modifier = Modifier.padding(4.dp, 0.dp)
                ) {
                    Text(text = quantity.second.toString(), modifier = Modifier.padding(10.dp, 4.dp))
                }
            }
        }
    }
}

@Composable
fun AllCustomerNotes(selectedTable: MutableState<String>) {
    // todo Remove Hardcoding
    val customerColors = listOf(red, green, blue, yellow, cyan, magenta)
    LazyColumnFor(items = customerColors) {color ->
        // todo Remove Hardcoding
        val customerNote = remember { mutableStateOf("add note... ") }
        TextField(
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                backgroundColor = color,
                value = customerNote.value,
                onValueChange = { newNote->
                    customerNote.value = newNote
                }
        )
    }
}

@Composable
fun OneCustomerOrdersAndNote(selectedCustomer: MutableState<Color>) {
    // todo Remove Hardcoding
    val customersItems = listOf(Pair("Item-1", 2), Pair("Item-3", 2), Pair("Item-5", 1))
    LazyColumnFor(items = customersItems) {
        item -> OneCustomerItem(item, selectedCustomer)
    }
    val customerNote = remember { mutableStateOf("add note...") }
    TextField(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            backgroundColor = selectedCustomer.value,
            value = customerNote.value,
            onValueChange = {newNote ->
                customerNote.value = newNote
            })
}

@Composable
fun OneCustomerItem(item: Pair<String, Int>, selectedCustomer: MutableState<Color>) {
    RoundedRowCard(
            color = selectedCustomer.value
    ) {
        Text(
                text = item.first,
                modifier = Modifier.padding(16.dp, 0.dp)
        )

        Row {
            val number = remember { mutableStateOf(item.second) }
            RoundedIconButton(
                    modifier = Modifier.then(Modifier.preferredSize(24.dp)),
                    asset = Icons.Filled.KeyboardArrowDown,
                    onClick = {
                        // todo Subtract one from Order
                        if (number.value > 0) number.value -= 1
                    })
            Text(text = number.value.toString())
            RoundedIconButton(
                    modifier = Modifier.then(Modifier.preferredSize(24.dp)),
                    asset = Icons.Filled.KeyboardArrowUp,
                    onClick = {
                        // todo Add one to Order
                        number.value += 1
                    })
        }
    }
}
