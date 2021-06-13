package com.iti.team.ecommerce.model.local.preferances

interface Preference {
    fun isLogin():Boolean
    fun  setLogin(login:Boolean)
    fun setDiscountId(id:Long)
    fun getDiscountId():Long

    fun setAddress(address:String)
    fun getAddress():String
}