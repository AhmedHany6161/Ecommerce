package com.iti.team.ecommerce.model.local.preferances

import android.content.SharedPreferences

class MySharedPreference(private val sharedPreference: SharedPreferences) {

    // ahmed
    fun setBoolean (key: String,value: Boolean = false ){
        sharedPreference.edit().putBoolean(key,value)?.apply()
    }

    fun getBoolean(key: String):Boolean{
        return sharedPreference.getBoolean(key,false)?:false
    }
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