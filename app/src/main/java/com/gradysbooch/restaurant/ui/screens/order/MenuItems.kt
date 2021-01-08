package com.gradysbooch.restaurant.ui.screens.order

import androidx.compose.material.Text
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.gradysbooch.restaurant.model.dto.Bullet
import com.gradysbooch.restaurant.model.dto.MenuItemDTO
import com.gradysbooch.restaurant.ui.values.RoundedRowCard
import com.gradysbooch.restaurant.ui.values.RoundedSearchBar
import com.gradysbooch.restaurant.ui.values.getColor
import com.gradysbooch.restaurant.viewmodel.OrderViewModel
import androidx.compose.runtime.getValue
import kotlinx.coroutines.flow.map

class MenuItems(
        private val orderViewModel: OrderViewModel
) {
    @Composable
    fun Show() {
        // val orderViewModel = viewModel<OrderViewModel>()
        val isAllScreenSelected by orderViewModel.allScreen
                .collectAsState(initial = true)

        if (isAllScreenSelected) return
        val text = remember { mutableStateOf("search ...") }
        RoundedSearchBar(text)
        Spacer(modifier = Modifier.height(16.dp))
        FilteredMenuItems(text.value)
    }

    @Composable
    fun FilteredMenuItems(searchText: String) {
        // val orderViewModel = viewModel<OrderViewModel>()
        val allMenuItems by orderViewModel.menu
                .collectAsState(initial = emptyList())

        LazyColumnFor(items = allMenuItems
                .filter { it.name.contains(searchText) }) {
            MenuItemEntry(it)
        }
    }

    @Composable
    fun MenuItemEntry(item: MenuItemDTO) {
        // val orderViewModel = viewModel<OrderViewModel>()
        val selectedBullet by orderViewModel.bulletList
                .map { bullets -> bullets.firstOrNull() { it.pressed } }
                .collectAsState(initial = Bullet("#000", false, false))

        RoundedRowCard(
                color = getColor(selectedBullet?.color)
        ) {
            Text(text = item.name)
            Text(text = "${item.price} RON")
        }
    }
}

