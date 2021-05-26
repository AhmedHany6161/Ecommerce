package com.iti.team.ecommerce.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.iti.team.ecommerce.R

class CategoriesFragment: Fragment() {
    private lateinit var viewModel: CategoriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)
        init()
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    private fun init(){
        //viewModel.getCategories()
        //viewModel.getProducts(268359598278)
       // viewModel.createDiscount()

    }


    companion object {

    }
}