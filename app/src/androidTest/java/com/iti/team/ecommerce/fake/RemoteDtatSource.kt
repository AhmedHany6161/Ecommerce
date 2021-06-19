package com.iti.team.ecommerce.fake

import com.iti.team.ecommerce.model.data_classes.*
import com.iti.team.ecommerce.model.remote.ApiInterface
import retrofit2.Response

class RemoteDtatSource:ApiInterface {
    override suspend fun getMainCategories(): Response<MainCategories> {
        TODO("Not yet implemented")
    }

    override suspend fun getProducts(collectionId: Long): Response<ProductsModel> {
        TODO("Not yet implemented")
    }

    override suspend fun createDiscount(discount: Discount): Response<Discount> {
        TODO("Not yet implemented")
    }

    override suspend fun getDiscount(discountId: Long): Response<Discount> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductImages(productId: Long): Response<ProductImages> {
        TODO("Not yet implemented")
    }

    override suspend fun createCustomer(customer: CustomerModel): Response<CustomerModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductsFromType(productType: String): Response<ProductsModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductsFromVendor(vendor: String): Response<ProductsModel> {
        TODO("Not yet implemented")
    }

    override suspend fun smartCollection(): Response<SmartCollectionModel> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCustomer(
        customerId: Long,
        customer: EditCustomerModel
    ): Response<EditCustomerModel> {
        TODO("Not yet implemented")
    }

    override suspend fun login(email: String): Response<CustomerLoginModel> {
        TODO("Not yet implemented")
    }

    override suspend fun addOrder(order: AddOrderModel): Response<GettingOrderModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getOrders(email: String): Response<OrdersModels> {
        TODO("Not yet implemented")
    }

    override suspend fun getAddress(customerId: Long): Response<CustomerModel> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAddress(
        customerId: Long,
        addressId: Long
    ): Response<CustomerAddressModel> {
        TODO("Not yet implemented")
    }

    override suspend fun addAddress(
        customerId: Long,
        address: AddressModel
    ): Response<CustomerAddressModel> {
        TODO("Not yet implemented")
    }

    override suspend fun updateAddress(
        customerId: Long,
        addressId: Long,
        address: AddressModel
    ): Response<CustomerAddressModel> {
        TODO("Not yet implemented")
    }
}