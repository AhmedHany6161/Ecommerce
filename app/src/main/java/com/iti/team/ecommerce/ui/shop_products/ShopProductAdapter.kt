package com.iti.team.ecommerce.ui.shop_products

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.databinding.CategoryRowItemBinding
import com.iti.team.ecommerce.databinding.ShopProductRowItemBinding
import com.iti.team.ecommerce.model.data_classes.Products

class ShopProductAdapter(val viewModel: ShopProductsViewModel):
    RecyclerView.Adapter<ShopProductAdapter.ViewHolder>() {

    var productArray:List<Pair<Products,String>> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding = ShopProductRowItemBinding.
        inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(viewBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.productName.text = productArray[position].first.title
        holder.binding.productPrice.text ="EGP ${productArray[position].first.variants[0]?.price}"
        holder.binding.productBrand.text = productArray[position].first.vendor

        Glide.with(holder.binding.productImage.context)
            .load(productArray[position].second)
            .placeholder(R.drawable.ic_back_img)
            .into(holder.binding.productImage)


    }

    override fun getItemCount() = productArray.size

    inner class ViewHolder(val binding:ShopProductRowItemBinding)
        :RecyclerView.ViewHolder(binding.root),View.OnClickListener{
        init {
            binding.card.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            viewModel.navigateToDetails(productArray[adapterPosition].first)
        }

    }

    fun loadData( productArray:List<Pair<Products,String>> ){
        this.productArray = productArray
        notifyDataSetChanged()
    }

}