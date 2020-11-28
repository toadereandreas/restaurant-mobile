package com.gradysbooch.restaurant.viewmodel

import com.gradysbooch.restaurant.model.dto.AllScreenItem
import com.gradysbooch.restaurant.model.dto.Bullet
import com.gradysbooch.restaurant.model.dto.MenuItemDTO
import kotlinx.coroutines.flow.Flow
typealias Color = String

interface OrderViewModelInterface
{
    val tableCode: Flow<Int?>

    val allScreen: Flow<Boolean>

    val bulletList: Flow<List<Bullet>>

    val requiresAttention: Flow<Boolean>

    val menu: Flow<List<MenuItemDTO>>

    val chosenItems: Flow<List<Pair<MenuItemDTO, Int>>>

    val allScreenMenuItems: Flow<List<AllScreenItem>>

    val allScreenNotes: Flow<List<Pair<Color, String>>>

    suspend fun getNote(): String

    fun setTable(tableId: String)

    fun selectAllScreen()

    fun addBullet()

    fun clearAttention()

    fun selectColor(color: Color)

    fun search(searchString: String)

    fun changeNote(note: String)

    fun changeNumber(menuItemId: String, number: Int)

    fun clearTable()
}