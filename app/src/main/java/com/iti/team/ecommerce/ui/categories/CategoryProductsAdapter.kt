package com.iti.team.ecommerce.ui.categories

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.databinding.ShopProductRowItemBinding
import com.iti.team.ecommerce.model.data_classes.Products

class CategoryProductsAdapter(val viewModel: CategoriesViewModel) :
    RecyclerView.Adapter<CategoryProductsAdapter.ViewHolder>() {


    var productArray: List<Products> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding =
            ShopProductRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.productName.text = productArray[position].title
        holder.binding.productPrice.text = "${productArray[position].variants[0]?.price} EGP"
        holder.binding.productBrand.text = productArray[position].vendor

        Glide.with(holder.binding.productImage.context)
            .load(productArray[position].image.src)
            .placeholder(R.drawable.ic_back_img)
            .into(holder.binding.productImage)

        if (viewModel.inWishList(productArray[position].productId ?: -1)) {
            holder.binding.addFavItem.setImageResource(R.drawable.ic_favorite_red)
        } else {
            holder.binding.addFavItem.setImageResource(R.drawable.ic_favorite)
        }


    }

    override fun getItemCount() = productArray.size

    inner class ViewHolder(val binding: ShopProductRowItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.card.setOnClickListener(this)
            binding.addFavItem.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.add_fav_item -> {
                    if (viewModel.isLogin()) {
                        if (!viewModel.inWishList(productArray[adapterPosition].productId ?: -1)) {
                            Log.e("cat", "error ${productArray[adapterPosition].image.src}")
                            productArray[adapterPosition].image.src?.let {
                                viewModel.addToWishList(
                                    productArray[adapterPosition],
                                    it
                                )
                            }

                            binding.addFavItem.setImageResource(R.drawable.ic_favorite_red)
                        } else {
                            viewModel.removeFromWishList(
                                productArray[adapterPosition].productId ?: -1
                            )
                            binding.addFavItem.setImageResource(R.drawable.ic_favorite)
                        }

                    }
                    else{
                            viewModel.navigateToLogin()
                    }
                }

                R.id.card -> {
                    viewModel.navigateToDetails(productArray[adapterPosition])
                }
            }

        }

    }

    fun loadData(productArray: List<Products>) {
        this.productArray = productArray
        notifyDataSetChanged()
    }
}