package com.iti.team.ecommerce.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.iti.team.ecommerce.databinding.FragmentCategoriesBinding
import com.iti.team.ecommerce.ui.shop.ShopFragmentDirections

class CategoriesFragment: Fragment() {
    private lateinit var viewModel: CategoriesViewModel
    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var mainCategoriesAdapter: MainCategoriesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)
        mainCategoriesAdapter= MainCategoriesAdapter(ArrayList(),this.context,viewModel)
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        setUpUI()
        init()
        return binding.root
    }

    private fun init(){
        binding.viewModel = viewModel
        viewModel.getMainCategories()
        setUpUI()
        observeData()

    }
    private fun setUpUI() {
        binding.mainCatagoryRecycle.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = mainCategoriesAdapter
        }
        binding.productsRecycle.layoutManager = GridLayoutManager(context,2)
        binding.accessoryTxt.setOnClickListener{
           viewModel.getProductFromType("ACCESSORIES")
        }
        binding.shoesTxt.setOnClickListener{
            viewModel.getProductFromType("SHOES")
        }
        binding.tShirtTxt.setOnClickListener{
            viewModel.getProductFromType("T-SHIRTS")
        }
    }
    private fun observeData(){
        observeToMainCategoriesList()
        observeToWishList()
        observeToCartList()
        observeToSearch()
        observeToDetails()

    }
    private fun observeToMainCategoriesList(){
        viewModel._mainCategories.observe(viewLifecycleOwner,{
            it?.let {
                mainCategoriesAdapter.updateFavorite(it)
                //  viewModel.getData(it[0].collectionsImage)
                it[0].collectionsId?.let { it1 -> viewModel.getProductsById(it1) }
            }
        })
    }

    private fun observeToDetails() {
        viewModel.navigateToDetails.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {it1->
                navigate(it1)
            }
        })

    }

    private fun navigate(productObject: String){
        val action = CategoriesFragmentDirections.actionShopProductsToProductDetailsFragment(productObject)
        findNavController().navigate(action)
    }

    private fun observeToWishList(){
        viewModel.navigateToWish.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let {
                val navigate = CategoriesFragmentDirections.actionShopFragmentToWishListFragment()
                findNavController().navigate(navigate)
            }
        })
    }
    private fun observeToCartList(){
        viewModel.navigateToCart.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let {
                val navigate = CategoriesFragmentDirections.actionShopFragmentToShoppingPageFragment()
                findNavController().navigate(navigate)
            }
        })
    }
    private fun observeToSearch(){
        viewModel.navigateToSearch.observe(viewLifecycleOwner,{ it ->
            it.getContentIfNotHandled()?.let {
                val navigate = ShopFragmentDirections.actionShopFragmentToSearchFragment(it)
                findNavController().navigate(navigate)
            }
        })
    }
    companion object {

    }
}