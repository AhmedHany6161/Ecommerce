package com.iti.team.ecommerce.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.databinding.FragmentShopBinding
import com.iti.team.ecommerce.model.data_classes.Discount
import com.iti.team.ecommerce.model.data_classes.PriceRule

class ShopFragment : Fragment() {

    private lateinit var viewModel: ShopViewModel
    private lateinit var binding: FragmentShopBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        init()
        return binding.root
    }

    private fun init(){
        val priceRule = PriceRule(title = "SUMMERSALE10OFF",
            targetType = "line_item", targetSelection = "all", allocationMethod = "across",
            valueType = "fixed_amount", value = "-10.0",customerSelection = "all",
            startsAt = "2017-01-19T17:59:10Z")
        val discount = Discount(priceRule)
        viewModel.createDiscount(discount)

    }
    companion object {

    }
}