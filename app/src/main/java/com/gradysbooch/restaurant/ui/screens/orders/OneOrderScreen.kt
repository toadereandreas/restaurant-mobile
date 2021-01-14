package com.gradysbooch.restaurant.ui.screens.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gradysbooch.restaurant.model.dto.MenuItemDTO
import com.gradysbooch.restaurant.ui.values.*
import com.gradysbooch.restaurant.viewmodel.OrderViewModel
import kotlinx.coroutines.flow.map

class OneOrderScreen(
        private val orderViewModel: OrderViewModel,
        private var selectedColor: MutableState<String>,
        private var locked: MutableState<Boolean>
) {

    @Composable
    fun Show() {
        orderViewModel.selectColor(selectedColor.value)

        val selectedOrderItems by orderViewModel.chosenItems
                .collectAsState(initial = emptyList())

        val text = remember { mutableStateOf("") }
        orderViewModel.search(text.value)
        val filteredMenuItems by orderViewModel.menu
                .collectAsState(initial = emptyList())

        LazyColumn {
            items(selectedOrderItems) { CustomerItem(it) }

            item {
                CustomerNote()
            }

            if (locked.value) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    RoundedSearchBar(text)
                }

                items(filteredMenuItems) { MenuItem(it) }
            }
        }
    }

    @Composable
    private fun CustomerItem(item: Pair<MenuItemDTO, Int>) {
        Surface(
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                color = getColorOr(selectedColor.value),
                shape = CircleShape,
        ) {
            Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                        text = smartSubstring(item.first.name, 25),
                        modifier = Modifier.padding(16.dp, 0.dp)
                )

                Row {
                    val number = remember { mutableStateOf(item.second) }
                    if (locked.value) {
                        RoundedIconButton(
                                modifier = Modifier.then(Modifier.preferredSize(24.dp)),
                                asset = Icons.Default.KeyboardArrowDown,
                                onClick = {
                                    // if (locked.value && number.value > 0) {
                                    if (number.value > 0) {
                                        number.value -= 1
                                        orderViewModel.changeNumber(item.first.id, number.value)
                                    }
                                })
                    }
                    Text(
                            modifier = Modifier.padding(8.dp, 0.dp),
                            text = number.value.toString()
                    )
                    if (locked.value) {
                        RoundedIconButton(
                                modifier = Modifier.then(Modifier.preferredSize(24.dp)),
                                asset = Icons.Default.KeyboardArrowUp,
                                onClick = {
                                    // if (locked.value) {
                                        number.value += 1
                                        orderViewModel.changeNumber(item.first.id, number.value)
                                    // }
                                })
                    }
                }
            }
        }
    }


    @Composable
    private fun CustomerNote() {
        val selectedOrderNote = orderViewModel.allScreenNotes
                .map { list -> list.firstOrNull {
                    it.first == selectedColor.value
                } }
                .map { it?.second }
                .collectAsState(initial = "") as MutableState

        TextField(
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                backgroundColor = getColorOr(selectedColor.value),
                value = selectedOrderNote.value ?: "",
                onValueChange = {
                    if (locked.value) {
                        selectedOrderNote.value = it
                        orderViewModel.changeNote(it)
                    }
                })
    }

    @Composable
    private fun MenuItem(it: MenuItemDTO) {
        RoundedButtonRowCard(
                color = getColorOr(selectedColor.value),
                onClick = {
                    // if (locked.value) {
                        orderViewModel.addMenuItem(it.id)
                    // }
                }
        ) {
            Text(text = smartSubstring(it.name, 25))
            Text(text = "${it.price} RON")
        }
    }
}