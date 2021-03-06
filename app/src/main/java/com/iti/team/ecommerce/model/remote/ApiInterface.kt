package com.iti.team.ecommerce.model.remote

import com.iti.team.ecommerce.model.data_classes.*
import kotlinx.coroutines.Deferred
import retrofit2.Response

interface ApiInterface {

    suspend fun getMainCategories(): Response<MainCategories>
    suspend fun getProducts(collectionId: Long): Response<ProductsModel>
    suspend fun createDiscount(discount: Discount): Response<Discount>
    suspend fun getDiscount(discountId: Long): Response<Discount>
    suspend fun getProductImages(productId: Long): Response<ProductImages>
    suspend fun createCustomer(customer: CustomerModel): Response<CustomerModel>
    suspend fun getProductsFromType(productType: String): Response<ProductsModel>
    suspend fun getProductsFromVendor(vendor: String): Response<ProductsModel>
    suspend fun smartCollection(): Response<SmartCollectionModel>

    suspend fun updateCustomer(customerId: Long,customer: EditCustomerModel): Response<EditCustomerModel>
    suspend fun login(email: String): Response<CustomerLoginModel>
    suspend fun addOrder(order: AddOrderModel): Response<GettingOrderModel>

    suspend fun getOrders(email:String): Response<OrdersModels>

    suspend fun getAddress(customerId:Long): Response<CustomerModel>
    suspend fun deleteAddress(customerId:Long,addressId: Long): Response<CustomerAddressModel>

    suspend fun addAddress(customerId:Long,address: AddressModel): Response<CustomerAddressModel>
    suspend fun updateAddress(customerId:Long,addressId: Long,address: AddressModel): Response<CustomerAddressModel>

}