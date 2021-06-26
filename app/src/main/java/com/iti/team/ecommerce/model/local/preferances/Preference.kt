package com.iti.team.ecommerce.model.local.preferances

interface Preference {
    fun isLogin():Boolean
    fun  setLogin(login:Boolean)
    fun setDiscountId(id:Long)
    fun getDiscountId():Long
    fun setEmail(email:String)
    fun getEmail():String
    fun setUserName(userName:String)
    fun getUserName():String
    fun setAddress(address:String)
    fun getAddress():String
    fun setFirstName(f_name:String)
    fun getFirstName():String
    fun setLastName(l_name:String)
    fun getLastName():String
    fun setPhoneNum(phone:String)
    fun getPhoneNum():String
    fun setCustomerID(customer_id:Long)
    fun getCustomerID():Long

    fun setAddressID(addressId:Long)
    fun getAddressID():Long
}