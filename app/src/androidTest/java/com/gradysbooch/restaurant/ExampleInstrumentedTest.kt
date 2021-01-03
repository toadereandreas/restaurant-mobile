package com.gradysbooch.restaurant

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gradysbooch.restaurant.model.*
import com.gradysbooch.restaurant.repository.networkRepository.NetworkRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest
{
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    val networkRepo = NetworkRepository(appContext)

    @Test
    fun testClientOrdersSocketCall(): Unit = runBlocking{
        networkRepo.clientOrders().collect { item -> Log.d("UndoTag", item.toString()) }
    }

    @Test
    fun testOrderItemsSocketCall(): Unit = runBlocking{
        networkRepo.orderItems().collect { item -> Log.d("UndoTag", item.toString()) }
    }

    @Test
    fun testTablesSocketCall(): Unit = runBlocking{
        networkRepo.getTables().collect { item -> Log.d("UndoTag", item.toString()) }
    }

    @Test
    fun testGetMenuItems(): Unit = runBlocking{
        Log.d("UndoTag", networkRepo.getMenuItems().toString())
    }

    @Test
    fun testClearCall(): Unit = runBlocking{
        networkRepo.clearCall("1")
    }

    @Test
    fun testUnlockOrder(): Unit = runBlocking{
        networkRepo.unlockOrder("1", "black123")
    }


    @Test
    fun testLockOrder(): Unit = runBlocking{
        networkRepo.lockOrder("1", "black123")
    }

    @Test
    fun testUpdateOrder(): Unit = runBlocking{
        networkRepo.updateOrder(OrderWithMenuItems(Order("1", "black123", "I changed this_1"), listOf(
            OrderItemWithMenuItem(OrderItem("black", "2", "1", 99999), MenuItem("1", "GUCCI", 112))
        )))
    }
}