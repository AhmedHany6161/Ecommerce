package com.iti.team.ecommerce.ui.order_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.model.data_classes.OrderDetails
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.ui.proudcts.ProductsViewModel

class OrderDetailsAdapter(private var dataSet: List< Pair<OrderDetails,String>>, private val viewModel: OrderDetailsViewModel) :


    RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {
    fun setData(products:List< Pair<OrderDetails,String>>){
        dataSet = products
    }


    class ViewHolder(val view: View, private val viewModel: OrderDetailsViewModel) : RecyclerView.ViewHolder(view) {

        private val name: TextView = view.findViewById(R.id.order_product_name)
        private val price: TextView = view.findViewById(R.id.order_product_price)
        private val brand: TextView = view.findViewById(R.id.order_product_brand)
        private val image: ImageView = view.findViewById(R.id.order_product_image)
        private val quantity: TextView = view.findViewById(R.id.order_product_quantity)


        fun bind(item: Pair<OrderDetails,String>) {
            name.text = item.first.name
            price.text = "${item.first.price} EGP"
            brand.text = item.first.vendor
            quantity.text = "Quantity : ${item.first.quantity}"
            Glide.with(view).load(item.second).into(image)

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