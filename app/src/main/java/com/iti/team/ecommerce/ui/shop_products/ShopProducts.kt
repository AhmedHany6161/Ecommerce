package com.iti.team.ecommerce.ui.shop_products

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.databinding.FragmentShopProductsBinding
import com.iti.team.ecommerce.ui.MainActivity
import com.iti.team.ecommerce.ui.shop.ShopFragmentDirections
import com.iti.team.ecommerce.utils.NetworkConnection

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
        //viewModel.getProducts(arg.collectionId)
        checkNetwork()
        registerConnectivityNetworkMonitor()
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
                                binding.shopProductRecycler.visibility = View.VISIBLE
                                binding.shopProductImage.visibility = View.VISIBLE
                                binding.textNoInternet.visibility = View.GONE
                                binding.noNetworkResult.visibility = View.GONE
                                viewModel.getProducts(arg.collectionId)
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
            viewModel.getProducts(arg.collectionId)
        } else {
            binding.textNoInternet.visibility = View.VISIBLE
            binding.noNetworkResult.visibility = View.VISIBLE
            binding.shopProductRecycler.visibility = View.GONE
            binding.shopProductImage.visibility = View.GONE

        }
    }
}