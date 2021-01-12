package com.gradysbooch.restaurant.ui.screens.order

import androidx.compose.material.Text
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gradysbooch.restaurant.model.dto.Bullet
import com.gradysbooch.restaurant.model.dto.MenuItemDTO
import com.gradysbooch.restaurant.ui.values.RoundedSearchBar
import com.gradysbooch.restaurant.ui.values.getColor
import com.gradysbooch.restaurant.viewmodel.OrderViewModel
import com.gradysbooch.restaurant.ui.values.RoundedButtonRowCard
import kotlinx.coroutines.flow.map

class MenuItems(
        private val orderViewModel: OrderViewModel,
        private var selectedBullet: State<Bullet?>
) {
    @Composable
    fun Show() {
        val text = remember { mutableStateOf("") }
        RoundedSearchBar(text)
        Spacer(modifier = Modifier.height(16.dp))
        FilteredMenuItems(text.value)
    }

    @Composable
    fun FilteredMenuItems(searchText: String) {
        val filteredMenuItems by orderViewModel.menu
                .map { list -> list.filter {
                    it.name.contains(searchText)
                } }
                .collectAsState(initial = emptyList())

        LazyColumnFor(items = filteredMenuItems) {
            MenuItemEntry(it)
        }
    }

    @Composable
    fun MenuItemEntry(item: MenuItemDTO) {
        /*
        val selectedBullet by orderViewModel.bulletList
                .map { bullets -> bullets.firstOrNull { it.pressed } }
                .collectAsState(initial = Bullet("#000", false, false))
         */

        RoundedButtonRowCard(
                color = getColor(selectedBullet.value?.color),
                onClick = {
                    // todo - add item to order
                }
        ) {
            Text(text = item.name)
            Text(text = "${item.price} RON")
        }
    }
}

