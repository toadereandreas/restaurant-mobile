package com.gradysbooch.restaurant.ui.screens.tables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gradysbooch.restaurant.ui.values.RoundedButtonRowCard

@Composable
fun TablesScreen(selectedTable: MutableState<String>) {
    Column {
        TablesScreenAppBar()
        TablesList(selectedTable)
    }
}


@Composable
fun TablesScreenAppBar() {
    TopAppBar(title = {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Restaurant")
        }
    })
}


@Composable
fun TablesList(selectedTable: MutableState<String>) {
    // todo Remove Hardcoding
    val tables = ArrayList<String>()
    for (i in 1..20) tables.add("Table $i")
    LazyColumnFor(items = tables) {table ->
        TableEntry(table, selectedTable)
    }
}

@Composable
fun TableEntry(table: String, selectedTable: MutableState<String>) {
    RoundedButtonRowCard(
            border = BorderStroke(1.dp, MaterialTheme.colors.onSurface),
            onClick = { selectedTable.value = table }
    ) {
        Text(text = table)
        // todo Remove Hardcoding
        Text(text = "Clients : 2")
    }
}

