package com.iti.team.ecommerce.shop

import android.view.View
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.iti.team.ecommerce.databinding.FragmentShopBinding
import com.iti.team.ecommerce.fake.FakeRepo
import com.iti.team.ecommerce.fake.OfflineRepo

import com.iti.team.ecommerce.ui.shop.ShopViewModel
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShopFragmentTest {


//    @Test
//    fun testMainState() {
//        val binding = FragmentShopBinding.inflate(ApplicationProvider.getApplicationContext())
//        val fakeRepository = FakeRepo(ApplicationProvider.getApplicationContext())
//        val offlineRepository = OfflineRepo()
//        val viewModel = ShopViewModel(fakeRepository,offlineRepository)
//        binding.viewModel = viewModel
//
//
//        binding.viewModel.smartCollection()
//
//        binding.executePendingBindings()
//
//        assertEquals(binding.textNoInternet.visibility,View.GONE)
//    }
}