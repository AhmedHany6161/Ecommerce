package com.iti.team.ecommerce.ui.product_details

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.databinding.SliderRowItemBinding
import com.iti.team.ecommerce.model.data_classes.Images
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(private val imagesArray:List<Images>): SliderViewAdapter<SliderAdapter.ViewHolder>() {

    override fun getCount(): Int {
        return imagesArray.size
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val viewBinding = SliderRowItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(viewBinding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.binding.imageSlide.context).load(imagesArray[position].src).centerCrop()
            .placeholder(R.drawable.home)
            .into(holder.binding.imageSlide)

    }
    inner class ViewHolder(var binding: SliderRowItemBinding) :
        SliderViewAdapter.ViewHolder(binding.root)

}