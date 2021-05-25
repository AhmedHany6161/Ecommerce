package com.iti.team.ecommerce.ui.product_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.iti.team.ecommerce.R

class ProductDetailsFragment: Fragment() {
    private lateinit var viewModel: ProductDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ProductDetailsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_product_details, container, false)
    }

    companion object {

    }

}