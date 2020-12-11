package com.gradysbooch.restaurant.viewmodel

import android.app.Application
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.viewModelScope
import com.gradysbooch.restaurant.model.Order
import com.gradysbooch.restaurant.model.dto.AllScreenItem
import com.gradysbooch.restaurant.model.dto.Bullet
import com.gradysbooch.restaurant.model.dto.MenuItemDTO
import com.gradysbooch.restaurant.model.dto.toDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class OrderViewModel(application: Application) : BaseViewModel(application),
        OrderViewModelInterface
{
    init
    {
        repository.networkRepository.orderItems()
                .onEach { repository.orderDao().saveOrderItems(it) }
                .launchIn(viewModelScope)

        viewModelScope.launch {
            repository.menuItemDAO().updateMenu(repository.networkRepository.getMenuItems())
        }
    }
    private val tableUID = MutableStateFlow<String?>(null)
    private val activeColor = MutableStateFlow<String?>(null)
    private val searchQuery = MutableStateFlow("")

    val table = tableUID.flatMapLatest {
        it?.let { repository.tableDao().getTable(it).filterNotNull() } ?: emptyFlow()
    }

    override val tableCode: Flow<Int?> = table.map { it.code }

    private val _allScreen = MutableStateFlow(true)
    override val allScreen: Flow<Boolean> = _allScreen

    override val bulletList: Flow<List<Bullet>> = forCurrentOrder { tableUID, activeColor ->
        val clientOrders = repository.networkRepository.clientOrders().map { orders ->
            orders.filter { it.tableUID == tableUID }.map {
                Bullet(it.orderColor, false, it.orderColor == activeColor)
            }
        }.onStart { emit(emptyList()) }
        return@forCurrentOrder repository.orderDao().getOrdersForTable(tableUID).map { orders ->
            orders.map {
                Bullet(it.orderColor, true, it.orderColor == activeColor)
            }
        }.combine(clientOrders) { lockedBullets, unlockedBullets ->
            lockedBullets + unlockedBullets
        }
    }

    private inline fun <T> forCurrentOrder(crossinline block: (tableUID: String, color: String?) -> Flow<T>): Flow<T>
    {
        return tableUID.flatMapLatest { tableUID ->
            tableUID ?: return@flatMapLatest emptyFlow()

            activeColor.flatMapLatest colorMap@{ activeColor ->
                block(tableUID, activeColor)
            }
        }
    }

    override val requiresAttention: Flow<Boolean> = table.map { it.call }

    override val menu: Flow<List<MenuItemDTO>> = repository.menuItemDAO()
            .getMenuFlow()
            .flatMapLatest { menuItems ->
                searchQuery.mapLatest { searchCriteria ->
                    menuItems.filter { it.name.contains(searchCriteria, ignoreCase = true) }
                            .map { menuItem -> menuItem.toDTO() }
                }
            }

    override val chosenItems: Flow<List<Pair<MenuItemDTO, Int>>> =
            forCurrentOrder { tableUID, activeColor ->
                activeColor ?: return@forCurrentOrder emptyFlow()
                repository
                        .orderDao()
                        .getOrderWithMenuItems(tableUID, activeColor)
                        .map { orderWithMenuItems ->
                            orderWithMenuItems
                                    ?.orderItems
                                    ?.map { it.menuItem.toDTO() to it.orderItem.quantity }
                                    ?: emptyList()
                        }
            }

    override val allScreenMenuItems: Flow<List<AllScreenItem>> = run {
        return@run tableUID.flatMapLatest { tableUID ->
            tableUID ?: return@flatMapLatest emptyFlow()
            repository.menuItemDAO()
                    .getMenuItemsForTable(tableUID)
                    .map { menuItemsWithOrderItems ->
                        menuItemsWithOrderItems.map {
                            val orders = it.orderItems.map { orderItem -> orderItem.orderColor to orderItem.quantity }
                            AllScreenItem(it.menuItem.toDTO(), orders.sumOf { order -> order.second }, orders)
                        }
                    }
        }
    }

    override val allScreenNotes: Flow<List<Pair<String, String>>> = run {
        return@run tableUID.flatMapLatest { tableUID ->
            tableUID ?: return@flatMapLatest emptyFlow()
            repository.orderDao()
                    .getOrdersForTable(tableUID)
                    .map { orders -> orders.map { it.orderColor to it.note } }
        }
    }

    override suspend fun getNote(): String
    {
        val tableUID = tableUID.value ?: return ""
        val activeColor = activeColor.value ?: return ""
        return repository.orderDao().getOrder(tableUID, activeColor)?.note ?: ""
    }

    override fun setTable(tableId: String)
    {
        this.tableUID.value = tableId
    }

    override fun selectAllScreen()
    {
        activeColor.value = null
    }

    override fun addBullet()
    {
        viewModelScope.launch {
            tableUID.value?.let { tableUID ->
                repository.orderDao().addOrder(
                        Order(
                                tableUID,
                                ColorManager.randomColor(bulletList.first().map { it.color }.toSet()),
                                ""
                        )
                )
            }
        }
    }

    override fun clearAttention()
    {
        viewModelScope.launch {
            tableUID.value?.let {
                repository.tableDao().updateTableCall(it, false)
                //repository.networkRepository.clearCall(it)
            }
        }
    }

    override fun selectColor(color: String)
    {
        activeColor.value = color
    }

    override fun search(searchString: String)
    {
        searchQuery.value = searchString
    }

    override fun changeNote(note: String)
    {
        viewModelScope.launch {
            val tableUID = tableUID.value ?: return@launch
            val activeColor = activeColor.value ?: return@launch
            repository.orderDao().updateNote(tableUID, activeColor, note)
        }
    }

    override fun changeNumber(menuItemId: String, number: Int)
    {
        viewModelScope.launch {
            val tableUID = tableUID.value ?: return@launch
            val activeColor = activeColor.value ?: return@launch
            repository.orderDao().changeNumber(tableUID, activeColor, menuItemId, number)
        }
    }

    override fun clearTable()
    {
        val tableUID = tableUID.value ?: return
        viewModelScope.launch {
            repository.clearTable(tableUID)
        }
    }

    override fun lockOrder(tableUID: String, color: Color)
    {
        TODO("Not yet implemented")
    }

    override fun unlockOrder(tableUID: String, color: Color)
    {
        TODO("Not yet implemented")
    }
}