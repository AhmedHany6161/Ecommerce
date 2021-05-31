package com.iti.team.ecommerce

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.iti.team.ecommerce.model.data_classes.Product
import com.iti.team.ecommerce.model.local.room.OfflineDatabase
import com.iti.team.ecommerce.model.reposatory.offline.OfflineRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OfflineRepositoryTest {
    private lateinit var repo: OfflineRepository
    private val job: Job = Job()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = OfflineDatabase.getInstance(context)
        repo = OfflineRepository.getInstance(db)
    }
    @Test
    fun deleteProduct() {
        runBlocking {
            repo.removeFromWishList(Product(12, "ssss", "sss", "ssss", 41))
        }
    }

    @Test
    fun writeUserAndReadInList() {
        CoroutineScope(Dispatchers.IO).launch {
            repo.addToWishList(Product(12, "ssss", "sss", "ssss", 41))
            repo.addToWishList(Product(13, "ssss", "sss", "ssss", 41))
            repo.addToWishList(Product(14, "ssss", "sss", "ssss", 41))
            repo.addToWishList(Product(15, "ssss", "sss", "ssss", 41))
            Assert.assertEquals(repo.getAllProducts().singleOrNull()?.size, 5)
        }


    }
}