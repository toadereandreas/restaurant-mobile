package com.gradysbooch.restaurant.viewmodel

import com.gradysbooch.restaurant.model.dto.AllScreenItem
import com.gradysbooch.restaurant.model.dto.Bullet
import com.gradysbooch.restaurant.model.dto.MenuItemDTO
import kotlinx.coroutines.flow.Flow
typealias Color = String

/**
 * Everything works after you call [setTable] with the id of the table that has to be controlled
 */
interface OrderViewModelInterface
{
    /**
     * Gives updates on the table code (null if there is no code yet)
     */
    val tableCode: Flow<Int?>

    /**
     * Tells if the allScreen is selected
     */
    val allScreen: Flow<Boolean>

    /**
     * Information about the colored bullets
     */
    val bulletList: Flow<List<Bullet>>

    /**
     * The equivalent of the Table call
     */
    val requiresAttention: Flow<Boolean>

    /**
     * Gives the filtered menu
     */
    val menu: Flow<List<MenuItemDTO>>

    /**
     * The chosen items for the selected order
     */
    val chosenItems: Flow<List<Pair<MenuItemDTO, Int>>>

    /**
     * The menu items for all orders
     */
    val allScreenMenuItems: Flow<List<AllScreenItem>>

    /**
     * The notes from all the orders (with colors)
     */
    val allScreenNotes: Flow<List<Pair<Color, String>>>

    /**
     * Get the note from the current order
     */
    suspend fun getNote(): String

    /**
     * Select a table
     */
    fun setTable(tableId: String)

    /**
     * Select the all screen
     */
    fun selectAllScreen()

    /**
     * Create a new colored bullet
     */
    fun addBullet()

    /**
     * Press on the seen button
     */
    fun clearAttention()

    /**
     * Select an order
     */
    fun selectColor(color: Color)

    /**
     * Give search criteria (empty string for all items)
     */
    fun search(searchString: String)

    /**
     * Change the note for the current order
     */
    fun changeNote(note: String)

    /**
     * Change the number of an item (from the active order)
     */
    fun changeNumber(menuItemId: String, number: Int)

    /**
     * Clear everything regarding table orders
     */
    fun clearTable()
}