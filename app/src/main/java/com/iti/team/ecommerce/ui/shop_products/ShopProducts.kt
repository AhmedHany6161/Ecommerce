package com.iti.team.ecommerce.ui.shop_products

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.databinding.FragmentShopProductsBinding
import com.iti.team.ecommerce.model.local.room.OfflineDatabase
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.ui.MainActivity

class ShopProducts: Fragment() {

    private lateinit var binding: FragmentShopProductsBinding
    val arg:ShopProductsArgs by navArgs()


    val viewModel: ShopProductsViewModel by viewModels {
        ShopProductViewModelFactory( ModelRepository(
            OfflineDatabase.getInstance(requireActivity().application),
            requireActivity().applicationContext),   ModelRepository(
            OfflineDatabase.getInstance(requireActivity().application),
            requireActivity().applicationContext)
        )
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
                it1.second?.let { it2 -> navigate(it1.first, it2) }
            }
        })
    }
    private fun observeToLogin(){
        viewModel.navigateToLogin.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let {
                val navigate = ShopProductsDirections.actionShopProductsToLoginFragment()
                findNavController().navigate(navigate)
            }
        })
    }
    private fun setUpRecyclerView(){
        binding.shopProductRecycler.layoutManager = GridLayoutManager(context,2)
    }

    private fun navigate(productObject: String,inWish:Boolean){
        val action = ShopProductsDirections.actionShopProductsToProductDetailsFragment(productObject,inWish)
        findNavController().navigate(action)
    }

    private fun observeData(){
      Log.i("observeData","observeData")
        observeButtonBackClicked()
        observeToLogin()
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