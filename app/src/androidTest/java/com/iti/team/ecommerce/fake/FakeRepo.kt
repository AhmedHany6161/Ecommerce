package com.iti.team.ecommerce.fake

import android.content.Context
import com.iti.team.ecommerce.model.data_classes.*
import com.iti.team.ecommerce.model.local.preferances.Preference
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepo

class FakeRepo(context: Context):ModelRepo {
    val preference  :Preference = FakeSharedPrefrence(context)
    override suspend fun getMainCategories(): Result<MainCategories?> {
        TODO("Not yet implemented")
    }

    override suspend fun getProducts(collectionId: Long): Result<ProductsModel?> {
        TODO("Not yet implemented")
    }

    override suspend fun createDiscount(discount: Discount): Result<Discount?> {
        TODO("Not yet implemented")
    }

    override suspend fun getDiscount(discountId: Long): Result<Discount?> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductImages(productId: Long): Result<ProductImages?> {
        TODO("Not yet implemented")
    }

    override suspend fun createCustomer(customer: CustomerModel): Result<CustomerModel?> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductsFromType(productType: String): Result<ProductsModel?> {
        TODO("Not yet implemented")
    }

    override suspend fun smartCollection(): Result<SmartCollectionModel?> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCustomer(
        customerId: Long,
        customer: EditCustomerModel
    ): Result<EditCustomerModel?> {
        TODO("Not yet implemented")
    }

    override suspend fun login(email: String): Result<CustomerLoginModel?> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductsFromVendor(vendor: String): Result<ProductsModel?> {
        TODO("Not yet implemented")
    }

    override suspend fun addOrder(order: AddOrderModel): Result<GettingOrderModel?> {
        TODO("Not yet implemented")
    }

    override suspend fun getOrders(email: String): Result<OrdersModels?> {
        TODO("Not yet implemented")
    }

    override suspend fun getAddress(customerId: Long): Result<CustomerModel?> {
        TODO("Not yet implemented")
    }

    override fun getAddress(): String {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAddress(
        customerId: Long,
        addressId: Long
    ): Result<CustomerAddressModel?> {
        TODO("Not yet implemented")
    }

    override suspend fun addAddress(
        customerId: Long,
        address: AddressModel
    ): Result<CustomerAddressModel?> {
        TODO("Not yet implemented")
    }

    override suspend fun updateAddress(
        customerId: Long,
        addressId: Long,
        address: AddressModel
    ): Result<CustomerAddressModel?> {
        TODO("Not yet implemented")
    }

    override fun isLogin(): Boolean {
        return preference.isLogin()
    }

    override fun setLogin(login: Boolean) {
        preference.setLogin(login)
    }

    override fun getDiscountId(): Long {
        TODO("Not yet implemented")
    }

    override fun setDiscount(id: Long) {
        TODO("Not yet implemented")
    }

    override fun setAddress(address: String) {
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