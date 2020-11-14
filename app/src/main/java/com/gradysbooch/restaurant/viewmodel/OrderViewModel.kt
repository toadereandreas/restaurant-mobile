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

    private val tableId = MutableStateFlow<Int?>(null)
    private val menuItems = repository.dao.getMenuFlow()
    private val tables = repository.dao.getTableFlow()

    //TODO use this for all of the flows in order to depend on the tableId or choose ViewModelFactory approach to set the tableId in the constructor
    @OptIn(ExperimentalCoroutinesApi::class)
    private val orders = tableId.flatMapLatest {
        it?.let { repository.dao.getOrdersWithMenuItemsForTable(it).cancellable() } ?: emptyFlow()
    }

    override val tableCode: Flow<Int>
        get() = TODO("Not yet implemented")
    override val allScreen: Flow<Boolean>
        get() = TODO("Not yet implemented")
    override val bulletList: Flow<List<Bullet>>
        get() = TODO("Not yet implemented")
    override val requiresAttention: Flow<Boolean>
        get() = TODO("Not yet implemented")
    override val menu: Flow<List<MenuItemDTO>> = menuItems.map { it.map { menuItem -> menuItem.toDTO() } }
    override val chosenItems: Flow<List<Pair<MenuItemDTO, Int>>>
        get() = TODO("Not yet implemented")
    override val allScreenMenuItems: Flow<OrderViewModelInterface.AllScreenItem>
        get() = TODO("Not yet implemented")
    override val allScreenNotes: Flow<List<Pair<Color, String>>>
        get() = TODO("Not yet implemented")

    override suspend fun getNote(): String
    {
        TODO("Not yet implemented")
    }

    override fun setTable(tableId: Int)
    {
        this.tableId.value = tableId
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