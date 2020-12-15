package com.gradysbooch.restaurant.viewmodel

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gradysbooch.restaurant.model.MenuItem
import com.gradysbooch.restaurant.model.Order
import com.gradysbooch.restaurant.model.OrderItem
import com.gradysbooch.restaurant.model.Table
import com.gradysbooch.restaurant.model.dto.toDTO
import com.gradysbooch.restaurant.repository.Repository
import com.gradysbooch.restaurant.util.getOrAwaitValue
import com.gradysbooch.restaurant.util.observeOnce
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*

import org.junit.Assert.*
import org.junit.runner.RunWith
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
class OrderViewModelTest {

    @get:Rule
    val liveDataRule = InstantTaskExecutorRule()

    private val orderViewModel: OrderViewModel = OrderViewModel(ApplicationProvider.getApplicationContext())
    private val repository: Repository = Repository(ApplicationProvider.getApplicationContext())
    private val table = Table("333", "test", 333, true)
    private val menu = listOf(
            MenuItem("1001", "item 1", 100),
            MenuItem("1002", "item 2", 200),
            MenuItem("1003", "item 3", 300)
    )
    private val orders = listOf(
            Order(table.tableUID, "red", "note 1"),
            Order(table.tableUID, "green", "note 2"),
            Order(table.tableUID, "blue", "note 3")
    )

    @ExperimentalCoroutinesApi
    @Test(expected = TimeoutException::class)
    fun checkTableEmpty() = runBlockingTest {
        orderViewModel.table.asLiveData().getOrAwaitValue()
    }

    @ExperimentalCoroutinesApi
    @Test(expected = TimeoutException::class)
    fun checkTableCodeEmpty() = runBlockingTest {
        orderViewModel.tableCode.asLiveData().getOrAwaitValue()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun checkAllScreen() = runBlockingTest {
        val allScreen = orderViewModel.allScreen.asLiveData().getOrAwaitValue()
        assertTrue(allScreen)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun checkTableUpdated() = runBlockingTest {
        insertTable()
        orderViewModel.setTable(table.tableUID)
        val selectedTable = orderViewModel.table.asLiveData().getOrAwaitValue()
        assertEquals(table.tableUID, selectedTable.tableUID)
        val selectedCode = orderViewModel.tableCode.asLiveData().getOrAwaitValue()
        assertEquals(table.code, selectedCode)
        val attention = orderViewModel.requiresAttention.asLiveData().getOrAwaitValue()
        assertTrue(attention)
        orderViewModel.setTable("-1")
        removeTable()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun checkMenu() = runBlockingTest {
        insertMenu()
        val menuFromDB = orderViewModel.menu.asLiveData().getOrAwaitValue()
        assertTrue(menuFromDB.contains(menu[0].toDTO()))
        assertTrue(menuFromDB.contains(menu[1].toDTO()))
        assertTrue(menuFromDB.contains(menu[2].toDTO()))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun checkBullets() = runBlockingTest {
        insertTable()
        orders.forEach {
            repository.orderDao().addOrder(it)
        }
        orderViewModel.setTable(table.tableUID)
        val bullets = orderViewModel.bulletList.asLiveData().getOrAwaitValue()
        val colors = bullets.map { bullet -> bullet.color }
        assertTrue(colors.contains("red"))
        assertTrue(colors.contains("green"))
        assertTrue(colors.contains("blue"))
        assertTrue(bullets[0].locked)
        orderViewModel.clearTable()
        orderViewModel.setTable("-1")
        removeTable()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun checkChosenItems() = runBlockingTest {
//        insertTable()
//        orderViewModel.setTable(table.tableUID)
//        insertMenu()
//        insertOrders()
//        val orderItems = listOf(
//                OrderItem(orders[0].orderColor, table.tableUID, "1", 1),
//                OrderItem(orders[0].orderColor, table.tableUID, "2", 2),
//                OrderItem(orders[1].orderColor, table.tableUID, "3", 1)
//        )
//        repository.orderDao().saveOrderItems(orderItems)
//        val orderWithMenuItems = repository.orderDao().getOrderWithMenuItems(table.tableUID, orders[0].orderColor).asLiveData().getOrAwaitValue()
//        assertTrue(orderWithMenuItems!!.orderItems.isNotEmpty())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun checkAddBullet() = runBlockingTest {
        insertTable()
        orderViewModel.setTable(table.tableUID)
        val colors = repository.orderDao().getOrdersForTable(table.tableUID).asLiveData().getOrAwaitValue().map { it.orderColor }.toSet()
        orderViewModel.addBullet()
        Thread.sleep(1000)
        val bullets = orderViewModel.bulletList.asLiveData().getOrAwaitValue()
        val newColors = bullets.map { it.color }.toSet()
        assertEquals(1, newColors.size - colors.size)
        var different = 0
        for (color in newColors) {
            if (!colors.contains(color))
                different += 1
        }
        assertEquals(1, different)
        orderViewModel.clearTable()
        orderViewModel.setTable("-1")
        removeTable()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun checkClearAttention() = runBlockingTest {
        insertTable()
        orderViewModel.setTable(table.tableUID)
        assertTrue(orderViewModel.requiresAttention.asLiveData().getOrAwaitValue())
        orderViewModel.clearAttention()
        assertFalse(orderViewModel.requiresAttention.asLiveData().getOrAwaitValue())
        orderViewModel.setTable("-1")
        removeTable()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun checkChangeNote() = runBlockingTest {
//        insertTable()
//        orderViewModel.setTable(table.tableUID)
//        orderViewModel.addBullet()
//        Thread.sleep(500)
//        val bullets = orderViewModel.bulletList.asLiveData().getOrAwaitValue()
//        val color = bullets[0].color
//        orderViewModel.selectColor(color)
//        assertEquals("", orderViewModel.getNote())
//        val testNote = "Test note"
//        orderViewModel.changeNote(testNote)
//        Thread.sleep(500)
//        val note = orderViewModel.getNote()
//        assertEquals(testNote, note)
//        orderViewModel.clearTable()
//        orderViewModel.setTable("-1")
//        removeTable()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun checkSearch() = runBlockingTest {
        insertTable()
        orderViewModel.setTable(table.tableUID)
        insertMenu()
        val criteria = "item"
        orderViewModel.search(criteria)
        val items = orderViewModel.menu.asLiveData().getOrAwaitValue()
        assertEquals(3, items.size)
        items.map { it.name }.forEach {
            assertTrue(it.contains(criteria))
        }
        orderViewModel.setTable("-1")
        removeTable()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun checkAllScreenNotes() = runBlockingTest {
        insertTable()
        orderViewModel.setTable(table.tableUID)
        orderViewModel.addBullet()
        orderViewModel.addBullet()
        Thread.sleep(1000)
        val colors = orderViewModel.bulletList.asLiveData().getOrAwaitValue().map { it.color }
        orderViewModel.selectColor(colors[0])
        val testNote = "Test note"
        orderViewModel.changeNote(testNote)
        Thread.sleep(1000)
        val notes = orderViewModel.allScreenNotes.asLiveData().getOrAwaitValue()
        assertEquals(2, notes.size)
        assertTrue(notes.contains(colors[0] to testNote))
        assertTrue(notes.contains(colors[1] to ""))
        orderViewModel.clearTable()
        orderViewModel.setTable("-1")
        removeTable()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun checkClearTable() = runBlockingTest {
        insertTable()
        orderViewModel.setTable(table.tableUID)
        orderViewModel.addBullet()
        orderViewModel.addBullet()
        Thread.sleep(1000)
        val bullets = orderViewModel.bulletList.asLiveData().getOrAwaitValue()
        assertTrue(bullets.isNotEmpty())
        orderViewModel.clearTable()
        Thread.sleep(1000)
        assertEquals(0, orderViewModel.bulletList.asLiveData().getOrAwaitValue().size)
        assertNull(orderViewModel.table.asLiveData().getOrAwaitValue().code)
        orderViewModel.setTable("-1")
        removeTable()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun checkAllScreenMenuItems() = runBlockingTest {
        insertTable()
        orderViewModel.setTable(table.tableUID)
        insertMenu()
        insertOrders()
        val menuItems = orderViewModel.allScreenMenuItems.asLiveData().getOrAwaitValue()
        val orderItems = listOf(
                OrderItem(orders[0].orderColor, table.tableUID, "1001", 1),
                OrderItem(orders[1].orderColor, table.tableUID, "1001", 2),
                OrderItem(orders[2].orderColor, table.tableUID, "1001", 2),
                OrderItem(orders[1].orderColor, table.tableUID, "1002", 1),
                OrderItem(orders[2].orderColor, table.tableUID, "1002", 3),
        )
        repository.orderDao().saveOrderItems(orderItems)
        val items = orderViewModel.allScreenMenuItems.asLiveData().getOrAwaitValue()
        items.forEach {
            if (it.menuItem.id == "1001") {
                assertEquals(5, it.number)
                assertEquals(3, it.orders.size)
                assertEquals(100, it.menuItem.price)
            }
            else {
                assertEquals(4, it.number)
                assertEquals(2, it.orders.size)
                assertEquals(200, it.menuItem.price)
            }
        }
        orderViewModel.clearTable()
        orderViewModel.setTable("-1")
        removeTable()
    }

    private suspend fun insertOrders() {
        orders.forEach {
            repository.orderDao().addOrder(it)
        }
    }

    private suspend fun insertMenu() {
        repository.menuItemDAO().insertMenu(menuItems = menu)
    }

    private suspend fun removeMenu() {
        repository.menuItemDAO().deleteMenu()
    }

    private suspend fun insertTable() {
        repository.tableDao().insertTable(table = table)
    }

    private suspend fun removeTable() {
        repository.tableDao().deleteTable(table = table)
    }
}