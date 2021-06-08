package com.iti.team.ecommerce.model.local.preferances

interface Preference {
    fun isLogin():Boolean
    fun  setLogin(login:Boolean)
}