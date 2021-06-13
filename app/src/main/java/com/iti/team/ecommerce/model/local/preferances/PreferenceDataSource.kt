package com.iti.team.ecommerce.model.local.preferances

import com.iti.team.ecommerce.utils.LOGIN_KEY

class PreferenceDataSource(private val sharedPreference: MySharedPreference):Preference {
    override fun isLogin(): Boolean {
        return sharedPreference.getBoolean(LOGIN_KEY)
    }

    override fun setLogin(login: Boolean) {
        sharedPreference.setBoolean(LOGIN_KEY,login)
    }


}