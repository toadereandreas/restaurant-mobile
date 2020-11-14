package com.gradysbooch.restaurant.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import com.gradysbooch.restaurant.model.Bullet
import com.gradysbooch.restaurant.model.MenuItem
import kotlinx.coroutines.flow.Flow

class OrderViewModel(application: Application) : BaseViewModel(application), OrderViewModelInterface
{
    override val tableCode: Flow<Int>
        get() = TODO("Not yet implemented")
    override val allScreen: Flow<Boolean>
        get() = TODO("Not yet implemented")
    override val bulletList: Flow<List<Bullet>>
        get() = TODO("Not yet implemented")
    override val requiresAttention: Flow<Boolean>
        get() = TODO("Not yet implemented")
    override val menu: Flow<List<MenuItem>>
        get() = TODO("Not yet implemented")
    override val note: Flow<String>
        get() = TODO("Not yet implemented")
    override val chosenItems: Flow<List<Pair<MenuItem, Int>>>
        get() = TODO("Not yet implemented")
    override val allScreenMenuItems: Flow<OrderViewModelInterface.AllScreenItem>
        get() = TODO("Not yet implemented")
    override val allScreenNotes: Flow<List<Pair<Color, String>>>
        get() = TODO("Not yet implemented")

    override fun setTable(tableId: Int)
    {
        TODO("Not yet implemented")
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

    override fun changeNumber(menuItem: MenuItem, number: Int)
    {
        TODO("Not yet implemented")
    }


}