package com.iti.team.ecommerce.model.local.preferances

import com.iti.team.ecommerce.utils.*

class PreferenceDataSource(private val sharedPreference: MySharedPreference):Preference {
    override fun isLogin(): Boolean {
        return sharedPreference.getBoolean(LOGIN_KEY)
    }

    override fun setLogin(login: Boolean) {
        sharedPreference.setBoolean(LOGIN_KEY,login)
    }

    override fun setDiscountId(id: Long) {
        sharedPreference.setLong(DISCOUNT_KEY, id)
    }

    override fun getDiscountId(): Long {
        return sharedPreference.getLong(DISCOUNT_KEY)
    }

    override fun setEmail(email: String) {
        sharedPreference.setString(EMAIL_KEY, email)
    }

    override fun getEmail(): String {
        return sharedPreference.getString(EMAIL_KEY)
    }

    override fun setUserName(userName: String) {
        sharedPreference.setString(UserName_KEY, userName)

    }

    override fun getUserName(): String {
        return sharedPreference.getString(UserName_KEY)

    }

    override fun setAddress(address: String) {
        sharedPreference.setString(ADDRESS_KEY, address)
    }

    override fun getAddress(): String {
        return sharedPreference.getString(ADDRESS_KEY)
    }

    override fun setFirstName(f_name: String) {
        sharedPreference.setString(UserName_KEY, f_name)
    }

    override fun getFirstName(): String {
        return sharedPreference.getString(UserName_KEY)
    }

    override fun setLastName(l_name: String) {
        sharedPreference.setString(LAST_KEY, l_name)
    }

    override fun getLastName(): String {
        return sharedPreference.getString(LAST_KEY)
    }

    override fun setPhoneNum(phone: String) {
        sharedPreference.setString(PHONE_KEY, phone)
    }

    override fun getPhoneNum(): String {
        return sharedPreference.getString(PHONE_KEY)
    }

    override fun setCustomerID(customer_id: Long) {
        sharedPreference.setLong(CUSTOMER_ID, customer_id)
    }

    override fun getCustomerID(): Long {
        return sharedPreference.getLong(CUSTOMER_ID)
    }
}