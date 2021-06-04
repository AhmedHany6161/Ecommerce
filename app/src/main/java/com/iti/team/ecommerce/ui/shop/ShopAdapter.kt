package com.iti.team.ecommerce.ui.shop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.team.ecommerce.databinding.CategoryRowItemBinding
import com.iti.team.ecommerce.model.data_classes.SmartCollection

class ShopAdapter(private val viewModel:ShopViewModel): RecyclerView.Adapter<ShopAdapter.ViewHolder>() {

    var shopArray:List<SmartCollection> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding = CategoryRowItemBinding.
        inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(holder.binding.categoryImage.context)
            .load(shopArray[position].image.src)
            .into(holder.binding.categoryImage)
    }

    override fun getItemCount() = shopArray.size

    fun loadData( shopArray:List<SmartCollection> ){
        this.shopArray = shopArray
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: CategoryRowItemBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        init {
            binding.card.setOnClickListener(this) 
        }
        override fun onClick(v: View?) {
            shopArray[adapterPosition].id?.let { viewModel.shopItemsClicked(it) }

        }
    }
}