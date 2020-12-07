package com.gradysbooch.restaurant.viewmodel

import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gradysbooch.restaurant.model.Table
import com.gradysbooch.restaurant.model.dto.TableDTO
import com.gradysbooch.restaurant.model.dto.toDTO
import com.gradysbooch.restaurant.repository.Repository
import com.gradysbooch.restaurant.repository.RoomDB
import com.gradysbooch.restaurant.repository.TableDAO
import com.gradysbooch.restaurant.util.TableUtil
import com.gradysbooch.restaurant.util.observeOnce
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock

@RunWith(AndroidJUnit4::class)
class TableViewModelTest {

    @get:Rule
    val liveDataRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TableViewModel
    private lateinit var repository: Repository
    val table = Table("333", "Test", 0, false)

    @ExperimentalCoroutinesApi
    @Before
    fun setup() = runBlockingTest{
        repository = Repository(ApplicationProvider.getApplicationContext())
        viewModel = TableViewModel(ApplicationProvider.getApplicationContext())
        insert()
    }

    private suspend fun insert() {
        repository.tableDao().insertTable(table = table)
    }

    private suspend fun remove() {
        repository.tableDao().clearTable(tableUID = table.tableUID)
    }

    @ExperimentalCoroutinesApi
    @After
    fun after() = runBlockingTest {
        remove()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun checkLiveData() = runBlockingTest {
        viewModel.tables.asLiveData().observeOnce {
            assertTrue(it.contains(table.toDTO()))
        }
    }
}