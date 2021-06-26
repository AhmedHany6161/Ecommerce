package com.iti.team.ecommerce.ui.categories

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.databinding.MainCatagoryItemBinding
import com.iti.team.ecommerce.model.data_classes.MainCollections
import com.iti.team.ecommerce.model.data_classes.SmartCollection

class MainCategoriesAdapter
    ( private val mainCatagoriesList: ArrayList<MainCollections>,
    private val context: Context?,private var mainCatagoriesViewModel: CategoriesViewModel): RecyclerView.Adapter<MainCategoriesAdapter.MainCategoriesViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainCategoriesAdapter.MainCategoriesViewHolder {
        return MainCategoriesViewHolder(
            MainCatagoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mainCatagoriesList.size
    }

    override fun onBindViewHolder(holder: MainCategoriesAdapter.MainCategoriesViewHolder, position: Int) {
        holder.binding.mainCatagoryTitleTxt.setOnClickListener {
            mainCatagoriesList[position].collectionsId?.let { it1 ->
                mainCatagoriesViewModel.getProductsById(it1)
                //holder.binding.mainCatagoryTitleTxt.setTextColor(ContextCompat.getColor(context!!,R.color.register_bk_color))
            }
            true
        }
        holder.binding.mainCatagoryTitleTxt.text=mainCatagoriesList[position].collectionsTitle

    }






    fun updateFavorite(newMainCategieresList: List<MainCollections>) {
        mainCatagoriesList.clear()
        mainCatagoriesList.addAll(newMainCategieresList)
        notifyDataSetChanged()
    }

    inner class MainCategoriesViewHolder constructor(val binding: MainCatagoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}