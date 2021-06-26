package com.iti.team.ecommerce.ui.product_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.iti.team.ecommerce.databinding.FragmentMoreMenuBinding

class MoreMenu(val viewModel: ProductDetailsViewModel): Fragment() {

    private lateinit var binding: FragmentMoreMenuBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreMenuBinding.inflate(inflater)
        init()
        return binding.root
    }
    private fun init(){
        itemClicked()
    }
    private fun itemClicked(){

        binding.favoriteLayout.setOnClickListener {
            if (viewModel.modelRepository.isLogin()) {
                val navigateToWishList = ProductDetailsFragmentDirections
                    .actionProductDetailsFragmentToWishListFragment()
                findNavController().navigate(navigateToWishList)
            } else {
                 navigateToLogin()
            }
        }

        binding.homeLayout.setOnClickListener {
            val navigateToWishList = ProductDetailsFragmentDirections
                .actionProductDetailsFragmentToShopFragment2(true)
            findNavController().navigate(navigateToWishList)
        }


        binding.bagLayout.setOnClickListener {
            if (viewModel.modelRepository.isLogin()) {
                val navigateToWishList = ProductDetailsFragmentDirections
                    .actionProductDetailsFragmentToShoppingPageFragment()
                findNavController().navigate(navigateToWishList)
            } else {
                navigateToLogin()
            }
        }
    }

    private fun navigateToLogin(){
        val navigate = ProductDetailsFragmentDirections
            .actionProductDetailsFragmentToLoginFragment()
        findNavController().navigate(navigate)
    }
    companion object{
        fun instance(viewModel: ProductDetailsViewModel) = MoreMenu(viewModel)
    }
}