package com.iti.team.ecommerce.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.iti.team.ecommerce.getOrAwaitValue
import com.iti.team.ecommerce.model.data_classes.Customer
import com.iti.team.ecommerce.model.data_classes.CustomerModel
import com.iti.team.ecommerce.ui.register.RegisterViewModel
import com.iti.team.ecommerce.ui.shop.FakeRepo
import com.iti.team.ecommerce.ui.shop.OfflineRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(sdk = [29])
@RunWith(AndroidJUnit4::class)



class RegisterViewModelTest {


    @get:Rule
    var executer= InstantTaskExecutorRule()


    @ExperimentalCoroutinesApi
    @Test
    fun createcustomerTest() {
        //given
        val fakeRepository = FakeRepo(ApplicationProvider.getApplicationContext())
        val viewModel =  RegisterViewModel(fakeRepository)
        val customer= Customer(email = "nn34@gmail.com",phone = "0124569876",firstName = "nn",lastName = "zz",note = "123456")
        val customerModel= CustomerModel(customer = customer)

        //when
        viewModel.createCustomer(customerModel)

        //result
        val observer = viewModel.successRegister.getOrAwaitValue()
        Assert.assertTrue(observer)
    }


}