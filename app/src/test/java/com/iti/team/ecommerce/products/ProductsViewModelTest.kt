package com.iti.team.ecommerce.products

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.iti.team.ecommerce.model.data_classes.Images
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.ui.proudcts.ProductsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(sdk = [29])
@RunWith(AndroidJUnit4::class)
class ShopProductViewModelTest {

    private lateinit var viewModel: ProductsViewModel

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>() as Application
        viewModel = ProductsViewModel(context)
    }

    @Test
    fun navigateToLogin() {
        val live = viewModel.navigateToLogin
        viewModel.navigateToLogin()
        Assert.assertNotNull(live.value)
    }

    @Test
    fun inWishListTest() {
        viewModel.addToWishList(
            Products(
                12, "asas", "asas",
                "asas", "asas", "asas", listOf(), listOf(), Images("")
            ), ""
        )
        Assert.assertTrue(viewModel.inWishList(12))
    }

    @Test
    fun navigateToDetailsTest() {
        runBlocking {
            val live = viewModel.navigateToDetails
            viewModel.navigateToDetails(
                Products(
                    12, "asas", "asas",
                    "asas", "asas", "asas", listOf(), listOf(), Images("")
                )
            )
            delay(2000)
            Assert.assertNotNull(live.value)
        }
    }

    @Test
    fun addToCart() {
        runBlocking {
            val live = viewModel.addToCart
            viewModel.addToCart(
                Products(
                    12, "asas", "asas",
                    "asas", "asas", "asas", listOf(), listOf(), Images("")
                )," "
            )
            Assert.assertEquals(live.value,"product successfully added to cart")
        }
    }

}