package com.iti.team.ecommerce.fake

import android.content.Context
import com.iti.team.ecommerce.model.local.preferances.MySharedPreference
import com.iti.team.ecommerce.model.local.preferances.Preference
import com.iti.team.ecommerce.model.local.preferances.PreferenceDataSource
import com.iti.team.ecommerce.utils.PREF_FILE_NAME

class FakeSharedPrefrence( context: Context):Preference {
    private val preference =
        MySharedPreference(context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE))

    private val sharedPreference:Preference = PreferenceDataSource(preference)
    override fun isLogin(): Boolean {
       return sharedPreference.isLogin()
    }

    override fun setLogin(login: Boolean) {
        sharedPreference.setLogin(true)
    }

    override fun setDiscountId(id: Long) {
        TODO("Not yet implemented")
    }

    override fun getDiscountId(): Long {
        TODO("Not yet implemented")
    }

    override fun setEmail(email: String) {
        TODO("Not yet implemented")
    }

    override fun getEmail(): String {
        TODO("Not yet implemented")
    }

    override fun setUserName(userName: String) {
        TODO("Not yet implemented")
    }

    override fun getUserName(): String {
        TODO("Not yet implemented")
    }

    override fun setAddress(address: String) {
        TODO("Not yet implemented")
    }

    override fun getAddress(): String {
        TODO("Not yet implemented")
    }

    override fun setFirstName(f_name: String) {
        TODO("Not yet implemented")
    }

    override fun getFirstName(): String {
        TODO("Not yet implemented")
    }

    override fun setLastName(l_name: String) {
        TODO("Not yet implemented")
    }

    override fun getLastName(): String {
        TODO("Not yet implemented")
    }

    override fun setPhoneNum(phone: String) {
        TODO("Not yet implemented")
    }

    override fun getPhoneNum(): String {
        TODO("Not yet implemented")
    }

    override fun setCustomerID(customer_id: Long) {
        TODO("Not yet implemented")
    }

    override fun getCustomerID(): Long {
        TODO("Not yet implemented")
    }

    override fun setAddressID(addressId: Long) {
        TODO("Not yet implemented")
    }

    override fun getAddressID(): Long {
        TODO("Not yet implemented")
    }
}