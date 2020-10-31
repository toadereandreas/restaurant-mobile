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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gradysbooch.restaurant.ui.values.*


@Composable
fun OrderScreenAppBar(selectedTable: MutableState<String>, selectedCustomer: MutableState<Color>) {
    TopAppBar(
            backgroundColor = selectedCustomer.value,
            modifier = Modifier.height(120.dp),
            title = {
                Column{
                    OrderScreenTopRow(selectedTable)
                    CustomerNavigationRow(selectedTable, selectedCustomer)
                }
            })
}

@Composable
fun OrderScreenTopRow(selectedTable: MutableState<String>) {
    RoundedRowCard (
            color = Color.Transparent,
            modifier = Modifier.padding(8.dp, 0.dp).fillMaxWidth()
    ){
        RoundedIconButton(
                color = Color.Transparent,
                tint = MaterialTheme.colors.secondary,
                asset = Icons.Filled.ArrowBack,
                onClick = {
                    // Load TablesScreen by triggering recomposition
                    selectedTable.value = ""
                })

        // todo Remove Hardcoding
        Text(text = "${selectedTable.value} (#123)")

        // todo Remove Hardcoding
        val isChecked = remember { mutableStateOf(true) }
        RoundedIconButton(
                color = Color.Transparent,
                tint = MaterialTheme.colors.secondary,
                asset = if (isChecked.value) Icons.Filled.CheckCircle else Icons.Filled.Check,
                onClick = {
                    // Check or Uncheck Table
                    isChecked.value = ! isChecked.value
                })
    }
}

@Composable
fun CustomerNavigationRow(selectedTable: MutableState<String>, selectedCustomer: MutableState<Color>) {
    Surface(
            modifier = Modifier.padding(8.dp, 0.dp).fillMaxWidth(),
            shape = RoundedCornerShape(20)
    ) {
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
        ) {
            WholeOrderNavigationButton(selectedCustomer)
            AddCustomerButton(selectedTable, selectedCustomer)
            AllCustomersNavigationButtons(selectedCustomer)
        }
    }
}


@Composable
fun WholeOrderNavigationButton(selectedCustomer: MutableState<Color>) {
    RoundedIconButton(
            modifier = Modifier.padding(4.dp, 0.dp),
            asset = Icons.Filled.Check,
            onClick = {
                // todo Remove Hardcoding
                selectedCustomer.value = Color.Unspecified
            })
}

@Composable
fun AddCustomerButton(selectedTable: MutableState<String>, selectedCustomer: MutableState<Color>) {
    RoundedIconButton(
            modifier = Modifier.padding(4.dp, 0.dp),
            asset = Icons.Filled.Add,
            onClick = {
                // todo Add New Empty Customer to Table
                // todo Select Newly added Customer
            })
}

@Composable
fun AllCustomersNavigationButtons(selectedCustomer: MutableState<Color>) {
    // todo Remove Hardcoding
    val colors = listOf(red, green, blue, yellow, cyan, magenta)
    LazyRowFor(items = colors) {
        customer -> CustomerNavigationButton(customer, selectedCustomer)
    }
}

@Composable
fun CustomerNavigationButton(customer: Color, selectedCustomer: MutableState<Color>) {
    RoundedIconButton(
            modifier = Modifier.padding(4.dp, 0.dp),
            color = customer,
            tint = MaterialTheme.colors.primary,
            asset = Icons.Filled.Lock,
            onClick = {
                selectedCustomer.value = customer
            })
}
