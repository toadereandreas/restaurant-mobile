package com.gradysbooch.restaurant.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gradysbooch.restaurant.model.Table
import com.gradysbooch.restaurant.model.dto.toDTO
import com.gradysbooch.restaurant.repository.Repository
import com.gradysbooch.restaurant.util.observeOnce
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TableViewModelTest {

    @get:Rule
    val liveDataRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TableViewModel
    private lateinit var repository: Repository
    private val table = Table("333", "Test", 0, true)

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