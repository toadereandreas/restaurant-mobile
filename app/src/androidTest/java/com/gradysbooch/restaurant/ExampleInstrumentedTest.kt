package com.gradysbooch.restaurant

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
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
    fun useAppContext(): Unit = runBlocking{
        //networkRepo.clientOrders().collect { item -> Log.d("UndoTag", item.toString()) }
        networkRepo.getMenuItems()
    }
}