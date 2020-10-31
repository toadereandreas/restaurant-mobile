package com.gradysbooch.restaurant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.platform.setContent
import androidx.ui.tooling.preview.Preview
import com.gradysbooch.restaurant.ui.screens.order.OrderScreen
import com.gradysbooch.restaurant.ui.screens.tables.TablesScreen
import com.gradysbooch.restaurant.ui.values.RestaurantmobileTheme

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Preview @Composable
fun App() {
    RestaurantmobileTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            // todo Change String to Table Object from Domain
            val selectedTable = remember { mutableStateOf("") }
            if (selectedTable.value == "") TablesScreen(selectedTable)
            else OrderScreen(selectedTable)
        }
    }
}

