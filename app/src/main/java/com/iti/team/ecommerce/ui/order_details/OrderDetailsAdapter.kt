package com.iti.team.ecommerce.ui.order_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.model.data_classes.OrderDetails

class OrderDetailsAdapter(
    private var dataSet: List<OrderDetails>,
    private val viewModel: OrderDetailsViewModel
) :


    RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {
    fun setData(products: List<OrderDetails>) {
        dataSet = products
    }


    class ViewHolder(val view: View, private val viewModel: OrderDetailsViewModel) :
        RecyclerView.ViewHolder(view) {

        private val name: TextView = view.findViewById(R.id.order_product_name)
        private val price: TextView = view.findViewById(R.id.order_product_price)
        private val brand: TextView = view.findViewById(R.id.order_product_brand)
        private val image: ImageView = view.findViewById(R.id.order_product_image)
        private val quantity: TextView = view.findViewById(R.id.order_product_quantity)


        fun bind(item: OrderDetails) {
            name.text = item.name
            price.text = "${item.price} EGP"
            brand.text = item.vendor
            quantity.text = "Quantity : ${item.quantity}"
            Glide.with(view).load(item.image).into(image)

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.order_details_holder, viewGroup, false)

        return ViewHolder(view,viewModel)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

}