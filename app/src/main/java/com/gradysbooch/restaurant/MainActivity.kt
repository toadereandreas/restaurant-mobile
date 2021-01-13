package com.gradysbooch.restaurant

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.viewinterop.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.gradysbooch.restaurant.notifications.NotificationReceiver
import com.gradysbooch.restaurant.ui.screens.order.OrderScreen
import com.gradysbooch.restaurant.ui.screens.tables.TablesScreen
import com.gradysbooch.restaurant.ui.values.RestaurantmobileTheme
import com.gradysbooch.restaurant.viewmodel.OrderViewModel
import com.gradysbooch.restaurant.viewmodel.TableViewModel

class MainActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        NotificationReceiver.createNotificationChannel(
                getString(R.string.channel_id),
                getString(R.string.channel_name),
                getString(R.string.channel_description),
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)

        setContent {
            App(viewModel<TableViewModel>(), viewModel<OrderViewModel>())
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationReceiver.removeNotificationChannel(
                    getString(R.string.channel_id),
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        }
    }
}

// @Preview
@Composable
fun App(tableViewModel: TableViewModel, orderViewModel: OrderViewModel, startLocation: String = "tables") {
    RestaurantmobileTheme {
        // A surface container using the 'background' color from the theme
        Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
        ) {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = startLocation) {
                composable("tables"){
                    TablesScreen(tableViewModel, orderViewModel, navController).Show()
                }
                // todo change this cause it's a mess (a working mess, mind you, but still a mess)
                composable("orders/{tableId}/{orderColor}",
                        arguments = listOf(
                                navArgument("tableId") { type = NavType.StringType },
                                navArgument("orderColor") { type = NavType.StringType }
                        )
                ) {
                    OrderScreen(orderViewModel, navController,
                            it.arguments?.getString("tableId"),
                            it.arguments?.getString("orderColor")
                    )
                }
            }
        }
    }
}

