package com.iti.team.ecommerce.ui.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.RequestAllOrdersQuery
import com.iti.team.ecommerce.model.data_classes.Order

class MyOrderAdapter(private var dataSet: List<RequestAllOrdersQuery.Edge>, private val viewModel: MyOrdersViewModel) :

    RecyclerView.Adapter<MyOrderAdapter.ViewHolder>() {
    fun setData(list: List<RequestAllOrdersQuery.Edge>) {
        dataSet = list
    }

    class ViewHolder(val view: View, val viewModel: MyOrdersViewModel) :
        RecyclerView.ViewHolder(view) {
        private val createdAt: TextView = view.findViewById(R.id.order_holder_created_at)
        private val price: TextView = view.findViewById(R.id.order_holder_final_price)
        private val status: TextView = view.findViewById(R.id.order_holder_stute)
        fun bind(item: RequestAllOrdersQuery.Node?) {
            createdAt.text = item?.createdAt.toString()
            price.text = "Price : ${item?.totalPrice.toString()} EGP"
            status.text = if (item?.displayFinancialStatus?.name?.lowercase() == "voided") {
                "UNPAID"
            } else {
                item?.displayFinancialStatus?.name
            }
            view.setOnClickListener {
                item?.let { it1 ->
                    viewModel.navigateToDetails(it1.lineItems)
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.order_holder, viewGroup, false)

        return ViewHolder(view,viewModel)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position].node)
    }

    override fun getItemCount() = dataSet.size
}