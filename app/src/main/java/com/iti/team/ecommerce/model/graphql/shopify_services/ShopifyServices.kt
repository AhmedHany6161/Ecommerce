package com.iti.team.ecommerce.model.graphql.shopify_services

import com.apollographql.apollo.api.Response
import com.iti.team.ecommerce.RequestAllOrdersQuery

interface ShopifyServices {
    suspend fun getOrders(email: String): Response<RequestAllOrdersQuery.Data>
}