package com.iti.team.ecommerce.shop_product

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.iti.team.ecommerce.getOrAwaitValue
import com.iti.team.ecommerce.model.data_classes.Images
import com.iti.team.ecommerce.model.data_classes.Product
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.ui.shop.FakeRepo
import com.iti.team.ecommerce.ui.shop.OfflineRepo
import com.iti.team.ecommerce.ui.shop_products.ShopProductsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@Config(sdk = [29])
@RunWith(AndroidJUnit4::class)
class ShopProductViewModelTest {

    @get:Rule
    var executer= InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Test
    fun getProducts_UpdateList(){
        //given
        val fakeRepository = FakeRepo(ApplicationProvider.getApplicationContext())

        //when
        runBlockingTest {
            val getProduct = fakeRepository.getProducts(11) as Result.Success

            //result
            Assert.assertEquals(3,getProduct.data?.product?.size)

        }
    }

    @Test
    fun navigateToDetails_UpdateLiveData(){
        //given
        val fakeRepository = FakeRepo(ApplicationProvider.getApplicationContext())
        val offlineRepository = OfflineRepo()
        val viewModel =  ShopProductsViewModel(fakeRepository,offlineRepository)

        viewModel.navigateToDetails(
            Products(
            11, "test", "http://image", "adidas",
            "170", "vendor", listOf(), listOf(), Images("testsImage")
        )
        )
        //when
        val value = viewModel.navigateToDetails.getOrAwaitValue()

        //result
        Assert.assertNotNull(value)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun addToWishList_InsertRow(){
        //given
        val offlineRepository = OfflineRepo()
        runBlockingTest {
            Assert.assertNotNull(offlineRepository.addToWishList(
                Product(12, "ssss", "sss", "ssss","12")))
        }

    }

    @ExperimentalCoroutinesApi
    @Test
    fun removeFromWishList_RemoveRow(){
        //given
        val offlineRepository = OfflineRepo()
        runBlockingTest {
            Assert.assertNotNull(offlineRepository.removeFromWishList(12))
        }

    }

    @Test
    fun inWishList_checkFound_result(){
        //given
        val hashSet: HashSet<Long> = hashSetOf()
        hashSet.add(1)
        hashSet.add(2)
        hashSet.add(3)

        //when
        val inWish = hashSet.contains(1)

        Assert.assertEquals(inWish,true)
    }


}