package com.iti.team.ecommerce.wish

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.iti.team.ecommerce.ui.wish.WishListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@Config(sdk = [29])
@RunWith(AndroidJUnit4::class)
class WishViewModelTest {

    private lateinit var viewModel: WishListViewModel

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>() as Application
        viewModel = WishListViewModel(context)
    }

    @Test
    fun getWishListTest() {
        runBlocking {
            val live = viewModel.getWishLis()
            live.observeForever {
                Assert.assertNotNull(it)
            }
            delay(6000)

        }
    }
    @Test
    fun askForRemoveTest() {
        runBlocking {
            val live = viewModel.remove
            viewModel.askForRemove(12)
            delay(1000)
            live.value?.getContentIfNotHandled()?.let {
                Assert.assertEquals(it,12)
            }
        }
    }

}