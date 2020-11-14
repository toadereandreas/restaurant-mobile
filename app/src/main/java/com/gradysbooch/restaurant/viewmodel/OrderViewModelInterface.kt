package com.gradysbooch.restaurant.viewmodel

import androidx.compose.ui.graphics.Color
import com.gradysbooch.restaurant.model.dto.Bullet
import com.gradysbooch.restaurant.model.dto.MenuItemDTO
import kotlinx.coroutines.flow.Flow

interface OrderViewModelInterface
{
    val tableCode: Flow<Int>

    val allScreen: Flow<Boolean>

    val bulletList: Flow<List<Bullet>>

    val requiresAttention: Flow<Boolean>

    val menu: Flow<List<MenuItemDTO>>

    val chosenItems: Flow<List<Pair<MenuItemDTO, Int>>>

    val allScreenMenuItems: Flow<AllScreenItem>

    val allScreenNotes: Flow<List<Pair<Color, String>>>

    suspend fun getNote(): String

    fun setTable(tableId: Int)

    fun selectAllScreen()

    fun addBullet()

    fun clearAttention()

    fun selectBullet(bullet: Bullet)

    fun search(searchString: String)

    fun changeNote(note: String)

    fun changeNumber(menuItem: MenuItemDTO, number: Int)

    data class AllScreenItem(val menuItem: MenuItemDTO, val number: Int, val orders: List<Pair<Color, Int>>)
}