package com.gradysbooch.restaurant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import com.gradysbooch.restaurant.ui.RestaurantmobileTheme
import com.gradysbooch.restaurant.viewmodel.MainViewModel
import androidx.compose.runtime.getValue

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContent {
            RestaurantmobileTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String)
{
    val viewModel = viewModel<MainViewModel>()
    val online by viewModel.onlineStatus.collectAsState(initial = false)

    Text(text = "Hello $name! Is the device online? $online")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview()
{
    RestaurantmobileTheme {
        Greeting("Android")
    }
}