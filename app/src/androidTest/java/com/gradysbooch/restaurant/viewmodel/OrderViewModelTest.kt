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
            MenuItem("1", "item 1", 100),
            MenuItem("2", "item 2", 200),
            MenuItem("3", "item 3", 300)
    )
    private val orders = listOf(
            Order(table.tableUID, "red", "note 1"),
            Order(table.tableUID, "green", "note 2"),
            Order(table.tableUID, "blue", "note 3")
    )
    private val orderItems = listOf(
            OrderItem("red", table.tableUID, "1", 1),
            OrderItem("red", table.tableUID, "2", 1)
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

        repository.orderDao().clearTable(table.tableUID)
        orderViewModel.setTable("-1")
        removeTable()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun checkChosenItems() = runBlockingTest {
//        insertTable()
//        val order = orders[0]
//        repository.orderDao().addOrder(order = order)
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
        repository.orderDao().clearTable(table.tableUID)
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
        insertTable()
        orderViewModel.setTable(table.tableUID)
        orderViewModel.addBullet()
        Thread.sleep(500)
        val bullets = orderViewModel.bulletList.asLiveData().getOrAwaitValue()
        val color = bullets[0].color
        orderViewModel.selectColor(color)
        assertEquals("", orderViewModel.getNote())
        val testNote = "Test note"
        orderViewModel.changeNote(testNote)
        Thread.sleep(500)
        val note = orderViewModel.getNote()
        assertEquals(testNote, note)
        repository.orderDao().clearTable(table.tableUID)
        orderViewModel.setTable("-1")
        removeTable()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun checkSearch() = runBlockingTest {
        val criteria = "item 2"
        orderViewModel.search(criteria)
        insertMenu()
        Thread.sleep(3000)
        val items = orderViewModel.menu.asLiveData().getOrAwaitValue()
        assertEquals(1, items.size)
        assertEquals("2", items[0].id)
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