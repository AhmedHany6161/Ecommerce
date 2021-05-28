package com.iti.team.ecommerce.ui.proudcts

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.model.data_classes.Product

class BrandAdapter (private var dataSet: List<String> ,private val viewModel: ProductsViewModel) :
    RecyclerView.Adapter<BrandAdapter.ViewHolder>() {
    fun setData(brands:List<String>){
        dataSet = brands
    }
    class ViewHolder(val view: View,private val viewModel: ProductsViewModel) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.brand_name)

        fun bind(item: String){
            name.text = item
            name.setOnClickListener {
                if(name.currentTextColor == Color.BLACK){
                    name.setTextColor(Color.RED)
                    viewModel.addBrandFilter(item)
                }else{
                    name.setTextColor(Color.BLACK)
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