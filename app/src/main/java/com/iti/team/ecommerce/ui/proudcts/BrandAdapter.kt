package com.iti.team.ecommerce.ui.proudcts

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iti.team.ecommerce.R

class BrandAdapter (private var dataSet: List<String> ,private val viewModel: ProductsViewModel) :
    RecyclerView.Adapter<BrandAdapter.ViewHolder>() {
    fun setData(brands:List<String>){
        dataSet = brands
    }
    class ViewHolder(val view: View,private val viewModel: ProductsViewModel) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.brand_name)

        fun bind(item: String){
            name.text = item
            if (viewModel.inFilteredList(item)) {
                name.setBackgroundColor(Color.RED)
            } else {
                name.setBackgroundColor(Color.GRAY)
            }
            name.setOnClickListener {
                if (!viewModel.inFilteredList(item)) {
                    name.setBackgroundColor(Color.RED)
                    viewModel.addBrandFilter(item)
                } else {
                    name.setBackgroundColor(Color.GRAY)
                    viewModel.removeBrandFilter(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.brand_holder, viewGroup, false)

        return ViewHolder(view,viewModel)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

}