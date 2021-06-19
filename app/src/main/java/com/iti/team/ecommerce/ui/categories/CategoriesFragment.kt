package com.iti.team.ecommerce.ui.categories

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.databinding.FragmentCategoriesBinding
import com.iti.team.ecommerce.model.data_classes.MainCollections
import com.iti.team.ecommerce.ui.MainActivity
import com.iti.team.ecommerce.ui.shop.ShopFragmentDirections
import com.iti.team.ecommerce.utils.NetworkConnection

class CategoriesFragment: Fragment() {
    private lateinit var viewModel: CategoriesViewModel
    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var mainCategoriesAdapter: MainCategoriesAdapter
    private lateinit var mainCatagoriesList: List<MainCollections>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)
        mainCategoriesAdapter= MainCategoriesAdapter(ArrayList(),this.context,viewModel)
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init(){
        binding.viewModel = viewModel
        mainCatagoriesList= emptyList()
        setUpUI()
        observeData()
        observeCartCount()
        checkNetwork()
        registerConnectivityNetworkMonitor()
    }
    private fun setUpUI() {
        binding.productsRecycle.layoutManager = GridLayoutManager(context,2)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                mainCatagoriesList?.let {
                it[tab.position].collectionsId?.let { it1 ->
                    viewModel.getProductsById(it1)
                }}
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        binding.subCategoryTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewModel.getProductFromType(tab.text.toString().uppercase())

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
//        binding.mainCatagoryRecycle.apply {
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//            adapter = mainCategoriesAdapter
//        }
//        binding.accessoryTxt.setOnClickListener{
//           viewModel.getProductFromType("ACCESSORIES")
////            binding.accessoryTxt.setTextColor(getResources().getColor(R.color.register_bk_color))
////            binding.tShirtTxt.setTextColor(getResources().getColor(R.color.gray))
////            binding.shoesTxt.setTextColor(getResources().getColor(R.color.gray))
//        }
//        binding.shoesTxt.setOnClickListener{
//            viewModel.getProductFromType("SHOES")
//        }
//        binding.tShirtTxt.setOnClickListener{
//            viewModel.getProductFromType("T-SHIRTS")
//        }
    }
    private fun observeData(){
        observeMainCategoriesList()
        observeToWishList()
        observeToCartList()
        observeToSearch()
        observeToDetails()
        observeLoading()
        observeNavToLogin()
    }
    private fun observeMainCategoriesList(){
        viewModel.mainCategories.observe(viewLifecycleOwner,{
            it?.let {
                mainCatagoriesList=it
                //  viewModel.getData(it[0].collectionsImage)
                it[1].collectionsId?.let { it1 -> viewModel.getProductsById(it1) }
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
    private fun observeLoading(){

    }
    private fun observeCartCount() {
        viewModel.cartCount.observe(viewLifecycleOwner, {
            if(it==0){
                binding.shopCartBadge.visibility = View.GONE
            }else{
                binding.shopCartBadge.text = "$it"
                binding.shopCartBadge.visibility = View.VISIBLE
            }

        })
    }
    private fun observeNavToLogin(){
        viewModel.navigateToLogin.observe(viewLifecycleOwner, { it ->
            if(it) {
                findNavController().navigate(R.id.loginFragment)
            }
        })
    }
    companion object {

    }
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).bottomNavigation.isGone = false
        (activity as MainActivity).bottomNavigation.show(2)
    }

    private fun registerConnectivityNetworkMonitor() {
        if (context != null) {
            val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val builder = NetworkRequest.Builder()
            connectivityManager.registerNetworkCallback(builder.build(),
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        if (activity != null) {
                            activity!!.runOnUiThread {
                                binding.progress.visibility = View.VISIBLE
//                                binding.linearLayout.visibility = View.VISIBLE
//                                binding.mainCatagoryRecycle.visibility = View.VISIBLE
                                binding.noNetworkResult.visibility = View.GONE
                                binding.textNoInternet.visibility = View.GONE
                                viewModel.getMainCategories()
                            }
                        }
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        if (activity != null) {
                            activity!!.runOnUiThread {
                                Toast.makeText(
                                    context, "Network Not Available",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            )
        }
    }

    private fun checkNetwork(){
        val networkConnection = NetworkConnection()
        if (networkConnection.checkInternetConnection(requireContext())) {
            viewModel.getMainCategories()
        } else {
            binding.noNetworkResult.visibility = View.VISIBLE
//            binding.linearLayout.visibility = View.GONE
//            binding.mainCatagoryRecycle.visibility = View.GONE
            binding.progress.visibility = View.GONE
            binding.textNoInternet.visibility = View.VISIBLE
        }
    }
}