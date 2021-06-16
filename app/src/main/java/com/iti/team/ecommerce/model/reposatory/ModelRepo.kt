package com.iti.team.ecommerce.model.reposatory

import com.iti.team.ecommerce.model.data_classes.*
import com.iti.team.ecommerce.model.remote.Result
import retrofit2.Response

interface ModelRepo {

    suspend fun getMainCategories(): Result<MainCategories?>
    suspend fun getProducts(collectionId: Long): Result<ProductsModel?>

    suspend fun createDiscount(discount: Discount): Result<Discount?>
    suspend fun getDiscount(discountId: Long): Result<Discount?>

    suspend fun getProductImages(productId: Long): Result<ProductImages?>

    suspend fun createCustomer(customer: CustomerModel): Result<CustomerModel?>

    suspend fun getProductsFromType(productType: String): Result<ProductsModel?>

    suspend fun smartCollection(): Result<SmartCollectionModel?>

    suspend fun updateCustomer(customerId: Long,customer: EditCustomerModel): Result<EditCustomerModel?>

    suspend fun login(email: String): Result<CustomerLoginModel?>

    suspend fun getProductsFromVendor(vendor: String): Result<ProductsModel?>

    suspend fun addOrder(order: AddOrderModel): Result<GettingOrderModel?>

    suspend fun getOrders(email:String): Result<OrdersModels?>

    suspend fun getAddress(customerId:Long,addressId: Long): Result<CustomerAddressModel?>
    suspend fun deleteAddress(customerId:Long,addressId: Long): Result<CustomerAddressModel?>

    suspend fun addAddress(customerId:Long,address: AddressModel): Result<CustomerAddressModel?>
    suspend fun updateAddress(customerId:Long,addressId: Long,address: AddressModel): Result<CustomerAddressModel?>


    fun isLogin():Boolean
    fun setLogin(login:Boolean)

    fun getDiscountId():Long
    fun setDiscount(id:Long)

    fun setAddress(address:String)
    fun getAddress():String

    fun setEmail(email:String)
    fun getEmail():String

    fun setUserName(userName:String)
    fun getUserName():String

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