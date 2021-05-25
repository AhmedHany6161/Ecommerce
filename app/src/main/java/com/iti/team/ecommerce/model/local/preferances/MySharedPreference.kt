package com.iti.team.ecommerce.model.local.preferances

import android.content.SharedPreferences

class MySharedPreference(private val sharedPreference: SharedPreferences) {

    // ahmed

    //ahmed

    //wesam

    //wesam

    //norhan

    //norhan

    //nermeen

    //nermeen
    fun setString (key: String,value: String ){
        sharedPreference.edit().putString(key,value)?.apply()
    }

    fun getString(key: String):String{
        return sharedPreference.getString(key,"")?:""
    }


}