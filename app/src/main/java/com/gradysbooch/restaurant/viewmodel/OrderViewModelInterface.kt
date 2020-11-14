package com.gradysbooch.restaurant.viewmodel

import androidx.compose.ui.graphics.Color
import com.gradysbooch.restaurant.model.Bullet
import com.gradysbooch.restaurant.model.MenuItem
import kotlinx.coroutines.flow.Flow

interface OrderViewModelInterface
{
    val tableCode: Flow<Int>

    val allScreen: Flow<Boolean>

    val bulletList: Flow<List<Bullet>>

    val requiresAttention: Flow<Boolean>

    val menu: Flow<List<MenuItem>>

    val note: Flow<String>

    val chosenItems: Flow<List<Pair<MenuItem, Int>>>

    val allScreenMenuItems: Flow<AllScreenItem>

    val allScreenNotes: Flow<List<Pair<Color, String>>>

    fun setTable(tableId: Int)

    fun selectAllScreen()

    fun addBullet()

    fun clearAttention()

    fun selectBullet(bullet: Bullet)

    fun search(searchString: String)

    fun changeNote(note: String)

    fun changeNumber(menuItem: MenuItem, number: Int)

    data class AllScreenItem(val menuItem: MenuItem, val number: Int, val orders: List<Pair<Color, Int>>)
}