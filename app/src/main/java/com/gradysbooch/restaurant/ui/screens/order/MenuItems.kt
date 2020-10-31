package com.gradysbooch.restaurant.ui.screens.order

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gradysbooch.restaurant.ui.values.RoundedRowCard
import com.gradysbooch.restaurant.ui.values.RoundedSearchBar


@Composable
fun MenuSubScreen(selectedCustomer: Color) {
    if (selectedCustomer == Color.Unspecified) return
    val text = remember { mutableStateOf("search ...") }
    RoundedSearchBar(text)
    Spacer(modifier = Modifier.height(16.dp))
    FilteredMenuItems(selectedCustomer, text.value)
}

@Composable
fun FilteredMenuItems(selectedCustomer: Color, searchText: String) {
    // todo Remove Hardcoding
    val allItems = listOf(Pair("Item-1", 5), Pair("Item-2", 4), Pair("Item-3", 3),
            Pair("Item-4", 2), Pair("Item-5", 1))
    LazyColumnFor(items = allItems.filter { item -> item.first.contains(searchText) }) { item ->
        if (item.second % 2 == 0) MenuItemEntry(item, selectedCustomer)
        else MenuItemEntry(item)
    }
}

@Composable
fun MenuItemEntry(
        item: Pair<String, Int>,
        color: Color = MaterialTheme.colors.surface
) {
    RoundedRowCard(
            color = color
    ) {
        Text(text = item.first)
        Text(text = "${item.second} RON")
    }
}

