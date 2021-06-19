package com.iti.team.ecommerce.product_details

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.iti.team.ecommerce.getOrAwaitValue
import com.iti.team.ecommerce.model.data_classes.Images
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.ui.product_details.ProductDetailsViewModel
import com.iti.team.ecommerce.ui.shop.FakeRepo
import com.iti.team.ecommerce.ui.shop.OfflineRepo
import com.iti.team.ecommerce.utils.Constants
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode


@Config(sdk = [29])
@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
class ProductDetailsViewModelTest {

    private lateinit var fakeRepository: FakeRepo
    private lateinit var offlineRepository: OfflineRepo
    private  var viewModel: ProductDetailsViewModel? = null

    @Before
    fun setup(){
        fakeRepository = FakeRepo(ApplicationProvider.getApplicationContext())
        offlineRepository = OfflineRepo()
        viewModel =  ProductDetailsViewModel(fakeRepository,offlineRepository)
    }

    @After
    fun free(){
        viewModel = null
    }

    @Test
    fun setProduct_updateProduct(){

        val productAdapter = Constants.moshi.adapter(Products::class.java)
         productAdapter.toJson(Products(11,"title","type","descri",
         "stat","vendor", listOf(), listOf(),Images("testsImage")))

        viewModel?.setProduct(" Products(11,test,http://image,adidas,170,vendor, listOf(), listOf(), Images(testsImage))")
        val value = viewModel?.imageProduct?.getOrAwaitValue()

        //then
        Assert.assertNotNull(value)

    }

}