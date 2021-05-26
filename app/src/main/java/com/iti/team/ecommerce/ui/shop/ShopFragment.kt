package com.iti.team.ecommerce.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.iti.team.ecommerce.R

class ShopFragment : Fragment() {
    private lateinit var viewModel: ShopViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        return inflater.inflate(R.layout.fragment_shop, container, false)
    }

    companion object {

    }
}