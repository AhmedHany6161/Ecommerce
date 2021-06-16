package com.iti.team.ecommerce.ui.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.model.data_classes.Order

class MyOrderAdapter(private var dataSet: List<Order?> ) :

    RecyclerView.Adapter<MyOrderAdapter.ViewHolder>() {
    fun setData(list: List<Order?>) {
        dataSet = list
    }
    class ViewHolder(val view: View) :
        RecyclerView.ViewHolder(view) {
        private val createdAt: TextView = view.findViewById(R.id.order_holder_created_at)
        private val price: TextView = view.findViewById(R.id.order_holder_final_price)
        private val status: TextView = view.findViewById(R.id.order_holder_stute)
        fun bind(item: Order?) {
            createdAt.text = item?.createdAt
            price.text = "Price : ${item?.finalPrice} EGP"
            status.text = if (item?.financialStatus == "voided") {
                "unpaid"
            } else {
                item?.financialStatus
            }

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.order_holder, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size
}