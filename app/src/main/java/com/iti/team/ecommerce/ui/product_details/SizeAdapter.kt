package com.iti.team.ecommerce.ui.product_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.team.ecommerce.databinding.SizeRowItemBinding

class SizeAdapter: RecyclerView.Adapter<SizeAdapter.ViewHolder>() {

    var sizeArray:List<String> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding = SizeRowItemBinding.
        inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.sizeText.text = sizeArray[position]
    }

    override fun getItemCount() = sizeArray.size

    fun loadData( shopArray:List<String> ){
        this.sizeArray = shopArray
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: SizeRowItemBinding):
        RecyclerView.ViewHolder(binding.root)
}