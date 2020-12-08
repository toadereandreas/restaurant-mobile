package com.gradysbooch.restaurant.ui.screens.order

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.viewModelScope
import com.gradysbooch.restaurant.model.dto.Bullet
import com.gradysbooch.restaurant.model.dto.MenuItemDTO
import com.gradysbooch.restaurant.ui.values.RoundedRowCard
import com.gradysbooch.restaurant.ui.values.RoundedSearchBar
import com.gradysbooch.restaurant.ui.values.getColor
import com.gradysbooch.restaurant.viewmodel.OrderViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@Composable
fun MenuSubScreen() {
    val orderViewModel = viewModel<OrderViewModel>()
    var isAllScreenSelected : Boolean = true
    orderViewModel.viewModelScope.launch {
        orderViewModel.allScreen.collect { isAllScreenSelected = it }
    }

    if (isAllScreenSelected) return
    val text = remember { mutableStateOf("search ...") }
    RoundedSearchBar(text)
    Spacer(modifier = Modifier.height(16.dp))
    FilteredMenuItems(text.value)
}

@Composable
fun FilteredMenuItems(searchText: String) {
    val orderViewModel = viewModel<OrderViewModel>()
    var allMenuItems : List<MenuItemDTO> = ArrayList()
    orderViewModel.viewModelScope.launch {
        orderViewModel.menu.collect { allMenuItems = it }
    }

    LazyColumnFor(items = allMenuItems
            .filter { it.name.contains(searchText) }) {
        MenuItemEntry(it)
    }
}

@Composable
fun MenuItemEntry(item: MenuItemDTO) {
    val orderViewModel = viewModel<OrderViewModel>()
    var selectedBullet = Bullet("", false, false)
    orderViewModel.viewModelScope.launch {
        orderViewModel.bulletList.collect { bullets ->
            val foundBullet = bullets.find { it.pressed }
            if (foundBullet != null) selectedBullet = foundBullet
        }
    }

    RoundedRowCard(
            color = getColor(selectedBullet.color)
    ) {
        Text(text = item.name)
        Text(text = "${item.price} RON")
    }
}

