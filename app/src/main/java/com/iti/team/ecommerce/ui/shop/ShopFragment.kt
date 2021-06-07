package com.iti.team.ecommerce.ui.shop

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.iti.team.ecommerce.databinding.FragmentShopBinding
import com.iti.team.ecommerce.ui.MainActivity

class ShopFragment : Fragment() {

    private lateinit var viewModel: ShopViewModel
    private lateinit var binding: FragmentShopBinding
    val args:ShopFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        init()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun onResume() {
        super.onResume()
        if (args.backFromDetails){
            (activity as MainActivity).bottomNavigation.isGone = false
        }
    }
    private fun init(){
        binding.viewModel = viewModel
        viewModel.smartCollection()
        setUpRecyclerView()
        itemsClicked()
        observeData()

    }

    private fun setUpRecyclerView(){
        binding.shopRecycler.layoutManager = GridLayoutManager(context,2)
    }
    private fun itemsClicked(){
        binding.tShirtLayout.subCategoryCard.setOnClickListener {
            navigate("T-SHIRTS")
        }

        binding.shoesLayout.subCategoryCard.setOnClickListener {
            navigate("SHOES")
        }

        binding.accessoryLayout.subCategoryCard.setOnClickListener {
            navigate("ACCESSORIES")
        }
    }

    private fun observeData(){
        observeShowErrorDialog()
        observeShowSuccessDialog()
        observeToWishList()
        observeToCartList()
        observeToSearch()
        observeNavigateToShopProduct()
    }

    private fun observeShowSuccessDialog(){
        viewModel.showSuccessDialog.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let { it1->
                Log.i("observeSuccessDialog", it1)
                val dialog = DiscountDialog.newInstance(viewModel,it1)
                dialog.show(this.childFragmentManager, "SuccessDialog")
            }

        })
    }
    private fun observeShowErrorDialog(){
        viewModel.showErrorDialog.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let { it1->
                Log.i("observeErrorDialog", it1)
                val dialog = DiscountDialog.newInstance(viewModel,it1)
                dialog.show(this.childFragmentManager, "ErrorDialog")
            }

        })
    }

    private fun observeToWishList(){
        viewModel.navigateToWish.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let {
                val navigate = ShopFragmentDirections.actionShopFragmentToWishListFragment()
                findNavController().navigate(navigate)
            }
        })
    }
    private fun observeToCartList(){
        viewModel.navigateToCart.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let {
                val navigate = ShopFragmentDirections.actionShopFragmentToShoppingPageFragment()
                findNavController().navigate(navigate)
            }
        })
    }
    private fun observeToSearch(){
        viewModel.navigateToSearch.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let {
                val navigate = ShopFragmentDirections.actionShopFragmentToSearchFragment(it)
                findNavController().navigate(navigate)
            }
        })
    }
    private fun navigate(productType: String = ""){
        val action = ShopFragmentDirections.actionShopFragmentToProducts(productType)
        findNavController().navigate(action)
    }

    private fun observeNavigateToShopProduct(){
        viewModel.navigateToShopProduct.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let {it1->
                val action = ShopFragmentDirections.
                actionShopFragmentToShopProducts(it1.first,it1.second)
                findNavController().navigate(action)
            }
        })
    }

}