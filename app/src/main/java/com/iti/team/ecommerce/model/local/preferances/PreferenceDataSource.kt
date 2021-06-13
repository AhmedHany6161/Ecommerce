package com.iti.team.ecommerce.model.local.preferances

import com.iti.team.ecommerce.utils.ADDRESS_KEY
import com.iti.team.ecommerce.utils.DISCOUNT_KEY
import com.iti.team.ecommerce.utils.LOGIN_KEY

class PreferenceDataSource(private val sharedPreference: MySharedPreference):Preference {
    override fun isLogin(): Boolean {
        return sharedPreference.getBoolean(LOGIN_KEY)
    }

    override fun setLogin(login: Boolean) {
        sharedPreference.setBoolean(LOGIN_KEY,login)
    }

    override fun setDiscountId(id:Long) {
        sharedPreference.setLong(DISCOUNT_KEY,id)
    }

    override fun getDiscountId():Long {
        return sharedPreference.getLong(DISCOUNT_KEY)
    }

    override fun setAddress(address: String) {
        sharedPreference.setString(ADDRESS_KEY,address)
    }

    override fun getAddress(): String {
        return sharedPreference.getString(ADDRESS_KEY)
    }


}