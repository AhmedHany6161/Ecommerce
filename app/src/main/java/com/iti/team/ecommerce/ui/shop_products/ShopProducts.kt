package com.iti.team.ecommerce.ui.shop_products

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.databinding.FragmentShopProductsBinding
import com.iti.team.ecommerce.ui.MainActivity

class ShopProducts: Fragment() {

    private lateinit var binding: FragmentShopProductsBinding
    val arg:ShopProductsArgs by navArgs()
    private val viewModel by lazy {
        ShopProductsViewModel(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopProductsBinding.inflate(inflater)

            init()

        return binding.root
    }

    private fun init(){
        binding.viewModel = viewModel
        viewModel.getData(arg.imageUrl)
        viewModel.getProducts(arg.collectionId)
        observeData()
        setUpRecyclerView()
        listeningForNavigate()
    }

    private fun listeningForNavigate(
    ) {
        viewModel.navigateToDetails.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {it1->
                navigate(it1)
            }
        })
    }
    private fun setUpRecyclerView(){
        binding.shopProductRecycler.layoutManager = GridLayoutManager(context,2)
    }

    private fun navigate(productObject: String){
        val action = ShopProductsDirections.actionShopProductsToProductDetailsFragment(productObject)
        findNavController().navigate(action)
    }

    private fun observeData(){
      Log.i("observeData","observeData")
        observeButtonBackClicked()
    }
    private fun observeButtonBackClicked(){
        viewModel.buttonBackClicked.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .popBackStack()
            }
        })
    }
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).bottomNavigation.isGone = true
    }
}