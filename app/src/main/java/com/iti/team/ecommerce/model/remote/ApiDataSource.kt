package com.iti.team.ecommerce.model.remote

import com.iti.team.ecommerce.model.data_classes.*
import retrofit2.Response


class ApiDataSource: ApiInterface {

    private  var _apiService: ApiService = NetworkService.createRetrofit()
        .create(ApiService::class.java)

    override suspend fun getMainCategories(): Response<MainCategories>{
        return _apiService.getMainCategories()

    }

    override suspend fun getProducts(collectionId: Long): Response<ProductsModel> {
        return _apiService.getProducts(collectionId)
    }

    override suspend fun createDiscount(discount: Discount): Response<Discount> {
        return  _apiService.createDiscount(discount)
    }

    override suspend fun getDiscount(discountId: Long): Response<Discount> {
        return _apiService.getDiscount(discountId)
    }

    override suspend fun getProductImages(productId: Long): Response<ProductImages> {
        return _apiService.getProductImage(productId)
    }

    override suspend fun createCustomer(customer: CustomerModel): Response<CustomerModel> {
        return _apiService.register(customer)
    }

    override suspend fun getProductsFromType(productType: String): Response<ProductsModel> {
        return _apiService.getProductsFromType(productType)
    }

    override suspend fun getProductsFromVendor(vendor: String): Response<ProductsModel> {
        return _apiService.getProductsByVendor(vendor)
    }

    override suspend fun smartCollection(): Response<SmartCollectionModel> {
        return  _apiService.smartCollection()
    }

    override suspend fun updateCustomer(
        customerId: Long,
        customer: EditCustomerModel
    ): Response<EditCustomerModel> {
        return  _apiService.updateCustomer(customerId,customer)
    }

    override suspend fun login(email: String): Response<CustomerLoginModel> {
        return _apiService.login(email)
    }

    override suspend fun addOrder(order: AddOrderModel): Response<GettingOrderModel> {
      return _apiService.addOrder(order)
    }

    override suspend fun getOrders(email: String): Response<OrdersModels> {
        return _apiService.getOrders(email)
    }

}