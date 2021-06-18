package com.iti.team.ecommerce.shop

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.iti.team.ecommerce.getOrAwaitValue
import com.iti.team.ecommerce.ui.shop.ShopViewModel
import org.junit.Assert
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@Config(sdk = [29])
@RunWith(AndroidJUnit4::class)
class ShopViewModelTest{

    @get:Rule
    var executer= InstantTaskExecutorRule()

    @Test
    fun navigateToCart_SetLiveData(){
        val viewModel =  ShopViewModel(ApplicationProvider.getApplicationContext())
        viewModel.navigateToCart()

        val observer = viewModel.navigateToCart.getOrAwaitValue()
        Assert.assertNotNull(observer)

    }
    @Test
    fun getSmartCollection_SetNavigatLiveData(){
        val viewModel =  ShopViewModel(ApplicationProvider.getApplicationContext())
        val smartCollection = viewModel.smartCollection()

        val observer = Observer<Unit>{}

        viewModel.navigateToSearch.observeForever { observer }

        Assert.assertEquals(10,smartCollection)
        Assert.assertNotNull(observer)

        viewModel.navigateToSearch.removeObserver { observer }
    }
    @Test
    fun addNewValue_SetNavigationLiveData(){
        val viewModel =  ShopViewModel(ApplicationProvider.getApplicationContext())
        viewModel.navigateToSearch()

        val value = viewModel.navigateToSearch.getOrAwaitValue()
        Assert.assertNotNull(value)


    }
}