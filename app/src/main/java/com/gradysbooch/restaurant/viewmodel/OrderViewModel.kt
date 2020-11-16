package com.gradysbooch.restaurant.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import com.gradysbooch.restaurant.model.dto.Bullet
import com.gradysbooch.restaurant.model.dto.MenuItemDTO
import com.gradysbooch.restaurant.model.dto.toDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class OrderViewModel(application: Application) : BaseViewModel(application), OrderViewModelInterface
{

    private val tableUID = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val tableWithOrders = tableUID.flatMapLatest {
        it?.let { repository.tableDao().getTableWithOrdersFlow(it) } ?: emptyFlow()
    }

    override val tableCode: Flow<Int> = tableWithOrders.map { it.table.code }

    private val _allScreen = MutableStateFlow(true)
    override val allScreen: Flow<Boolean> = _allScreen

    override val bulletList: Flow<List<Bullet>>
        get() = TODO("Not yet implemented")

    override val requiresAttention: Flow<Boolean> = tableWithOrders.map { it.table.call }

    override val menu: Flow<List<MenuItemDTO>> = repository.menuItemDAO().getMenuFlow().map { it.map { menuItem -> menuItem.toDTO() } }

    override val chosenItems: Flow<List<Pair<MenuItemDTO, Int>>> = tableUID.value?.let{ repository.orderDao().getOrderWithMenuItems().map { it.menuItems }} ?: emptyFlow()
    override val allScreenMenuItems: Flow<OrderViewModelInterface.AllScreenItem>
        get() = TODO("Not yet implemented")
    override val allScreenNotes: Flow<List<Pair<Color, String>>>
        get() = TODO("Not yet implemented")

    override suspend fun getNote(): String
    {
        TODO("Not yet implemented")
    }

    override fun setTable(tableId: String)
    {
        this.tableUID.value = tableId
    }

    override fun selectAllScreen()
    {
        TODO("Not yet implemented")
    }

    override fun addBullet()
    {
        TODO("Not yet implemented")
    }

    override fun clearAttention()
    {
        TODO("Not yet implemented")
    }

    override fun selectBullet(bullet: Bullet)
    {
        TODO("Not yet implemented")
    }

    override fun search(searchString: String)
    {
        TODO("Not yet implemented")
    }

    override fun changeNote(note: String)
    {
        TODO("Not yet implemented")
    }

    override fun changeNumber(menuItem: MenuItemDTO, number: Int)
    {
        TODO("Not yet implemented")
    }


}