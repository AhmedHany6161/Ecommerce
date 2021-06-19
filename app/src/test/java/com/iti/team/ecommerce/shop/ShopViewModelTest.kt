package com.iti.team.ecommerce.shop

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.iti.team.ecommerce.getOrAwaitValue
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.ui.shop.FakeRepo
import com.iti.team.ecommerce.ui.shop.OfflineRepo
import com.iti.team.ecommerce.ui.shop.ShopViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
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
        //given
        val fakeRepository = FakeRepo(ApplicationProvider.getApplicationContext())
        val offlineRepository = OfflineRepo()
        val viewModel =  ShopViewModel(fakeRepository,offlineRepository)
        fakeRepository.setLogin(true)

        //when
        viewModel.navigateToCart()
        val observer = viewModel.navigateToCart.getOrAwaitValue()
        //result
        Assert.assertNotNull(observer)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getSmartCollection_ListOfSmartCollection(){
        //given
        val fakeRepository = FakeRepo(ApplicationProvider.getApplicationContext())

        //when
        runBlockingTest {
            val smartCollection = fakeRepository.smartCollection() as Result.Success

            //result
            Assert.assertEquals(2,smartCollection.data?.smart_collections?.size)

        }

    }
    @Test
    fun navigateToLogin_SetLiveData(){
        //given
        val fakeRepository = FakeRepo(ApplicationProvider.getApplicationContext())
        val offlineRepository = OfflineRepo()
        val viewModel =  ShopViewModel(fakeRepository,offlineRepository)

        //when
        viewModel.navigateToCart()
        //result
        val observer = viewModel.navigateToLogin.getOrAwaitValue()
        Assert.assertNotNull(observer)
    }

    @Test
    fun addNewValue_SetNavigationLiveData(){
        //given
        val fakeRepository = FakeRepo(ApplicationProvider.getApplicationContext())
        val offlineRepository = OfflineRepo()
        val viewModel =  ShopViewModel(fakeRepository,offlineRepository)
        //when
        viewModel.navigateToSearch()

        val value = viewModel.navigateToSearch.getOrAwaitValue()
        //result
        Assert.assertNotNull(value)


    }
}