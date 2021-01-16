package com.gradysbooch.restaurant.ui.screens.tables

import androidx.compose.animation.animate
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.gradysbooch.restaurant.R
import com.gradysbooch.restaurant.model.dto.TableDTO
import com.gradysbooch.restaurant.ui.values.RoundedButtonRowCard
import com.gradysbooch.restaurant.ui.values.RoundedIconButton
import com.gradysbooch.restaurant.viewmodel.OrderViewModel
import com.gradysbooch.restaurant.viewmodel.TableViewModel


class TablesScreen(
        private val tableViewModel: TableViewModel,
        private val orderViewModel: OrderViewModel,
        private val screenNavController: NavHostController,
) {
    @Composable
    fun Show() {
        Column {
            //TablesScreenAppBar()
            TablesList()
        }
    }

    @Composable
    fun TablesScreenAppBar() {
        TopAppBar(title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(fontSize = 36.sp, text = "Restaurant")
            }
        })
    }

    @Composable
    fun TablesList() {
        val tables by tableViewModel.tables.collectAsState(initial = emptyList())

        Box(Modifier.background(Color.Black).fillMaxHeight()) {
            Image(asset = imageResource(id = R.drawable.background_image), contentScale = ContentScale.Crop, modifier = Modifier.fillMaxWidth())
           Image(asset = vectorResource(id = R.drawable.tables_screen), contentScale = ContentScale.Crop, modifier = Modifier.fillMaxWidth())
            Column {
                Spacer(modifier = Modifier.height(100.dp))
                var target by remember(tables) { mutableStateOf(0f)}
                val opacity = animate(target = target)
                if (target == 0f)
                    target = 1f
                LazyColumnFor(items = tables, modifier = Modifier.drawOpacity(opacity)) {
                    TableEntry(table = it)
                }
            }
        }
    }

    @Composable
    fun TableEntry(table: TableDTO) {
        RoundedButtonRowCard(
                border = BorderStroke(2.dp, MaterialTheme.colors.onSurface),
                onClick = {
                    screenNavController.navigate("orders/${table.id}")
                }
        ) {
            Text(modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp),
                    fontSize = 16.sp, text = table.name)

            RoundedIconButton(
                    modifier = Modifier.preferredSize(36.dp),
                    asset = Icons.Default.Delete,
                    onClick = {
                        orderViewModel.setTable(table.id)
                        orderViewModel.clearTable()
                    })
        }
    }
}


