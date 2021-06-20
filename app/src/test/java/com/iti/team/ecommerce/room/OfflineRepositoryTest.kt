package com.iti.team.ecommerce.room

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.iti.team.ecommerce.model.data_classes.Product
import com.iti.team.ecommerce.model.local.room.OfflineDatabase
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(sdk = [29])
@RunWith(AndroidJUnit4::class)
class OfflineRepositoryTest {
    private lateinit var repo: ModelRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = OfflineDatabase.getInstance(context)
        repo = ModelRepository(db,context)
    }

    @Test
    fun deleteProduct() {
        runBlocking {
            repo.removeFromWishList(12)
        }
    }

    @Test
    fun writeAndReadWishList() {
        runBlocking {
            repo.addToWishList(Product(12, "ssss", "sss", "ssss", "12"))
            repo.addToWishList(Product(13, "ssss", "sss", "ssss", "41"))
            repo.addToWishList(Product(14, "ssss", "sss", "ssss", "41"))
            repo.addToWishList(Product(15, "ssss", "sss", "ssss", "41"))
            val flow = repo.getAllWishListProducts()
            Assert.assertEquals(flow.first().size, 4)
        }
    }

    @Test
    fun addSameItemToCart() {
        runBlocking {
            repo.addToCart(Product(12, "ssss", "sss", "ssss", "41"))
            repo.addToCart(Product(12, "ssss", "sss", "ssss", "41"))
            repo.addToCart(Product(12, "ssss", "sss", "ssss", "41"))
            repo.addToCart(Product(12, "ssss", "sss", "ssss", "41"))
            val flow = repo.getCartProducts()
            Assert.assertEquals(flow.first().size, 1)
            Assert.assertEquals(flow.first()[0].count, 4)
        }
    }

    @Test
    fun addDifferentItemToCart() {
        runBlocking {
            repo.addToCart(Product(12, "ssss", "sss", "ssss", "41"))
            repo.addToCart(Product(11, "ssss", "sss", "ssss", "41"))
            repo.addToCart(Product(13, "ssss", "sss", "ssss", "41"))
            repo.addToCart(Product(10, "ssss", "sss", "ssss", "41"))
            val flow = repo.getCartProducts()
            Assert.assertEquals(flow.first().size, 4)
        }
    }


}