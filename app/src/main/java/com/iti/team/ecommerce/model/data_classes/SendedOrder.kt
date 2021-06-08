package com.iti.team.ecommerce.model.data_classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SendedOrder(


    @Json(name = "email")
    val email: String? = "",

    @Json(name = "financial_status")
    val financialStatus: String? = "",

    @Json(name = "billing_address")
    val billingAddress: BillingShippingAddress? = null,

    @Json(name = "shipping_address")
    val shippingAddress: BillingShippingAddress? = null,

    @Json(name = "transactions")
    val transactions: List<Transactions?> = listOf(),

    @Json(name = "discount_codes")
    val discountCodes: List<DiscountCodes?> = listOf(),

    @Json(name = "line_items")
    val lineItems: List<LineItems?> = listOf(),


)
