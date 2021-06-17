package com.iti.team.ecommerce.ui.shop

import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ShopViewModelTest{

    @Test
    fun addNewValue_SetLiveData(){
        val viewModel =  ShopViewModel(ApplicationProvider.getApplicationContext())
        viewModel.navigateToSearch()

        val observer = Observer<Unit>{}

        viewModel.navigateToSearch.observeForever { observer }

        Assert.assertNotNull(observer)

        viewModel.navigateToSearch.removeObserver { observer }
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
        viewModel.navigateToCart()

        val observer = Observer<Unit>{}

        viewModel.navigateToSearch.observeForever { observer }

        Assert.assertNotNull(observer)

        viewModel.navigateToSearch.removeObserver { observer }
    }
}