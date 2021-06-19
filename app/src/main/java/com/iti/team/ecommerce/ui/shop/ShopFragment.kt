package com.iti.team.ecommerce.ui.shop

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.iti.team.ecommerce.databinding.FragmentShopBinding
import com.iti.team.ecommerce.model.local.room.OfflineDatabase
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.model.reposatory.OfflineRepo
import com.iti.team.ecommerce.ui.MainActivity
import com.iti.team.ecommerce.utils.NetworkConnection


class ShopFragment : Fragment() {


    private lateinit var binding: FragmentShopBinding
    val args:ShopFragmentArgs by navArgs()


    val viewModel: ShopViewModel by viewModels {
       ShopViewModelFactory( ModelRepository(
            OfflineDatabase.getInstance(requireActivity().application),
            requireActivity().applicationContext),   ModelRepository(
            OfflineDatabase.getInstance(requireActivity().application),
            requireActivity().applicationContext))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopBinding.inflate(inflater)


        init()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun onResume() {
        super.onResume()
            (activity as MainActivity).bottomNavigation.isGone = false

    }
    private fun init(){
        binding.viewModel = viewModel
       // viewModel.smartCollection()
        checkNetwork()
        setUpRecyclerView()
        itemsClicked()
        observeData()
        observeCartCount()
        registerConnectivityNetworkMonitor()
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
        observeToLogin()
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

    private fun observeToLogin(){
        viewModel.navigateToLogin.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let {
                val navigate = ShopFragmentDirections.actionShopFragmentToLoginFragment()
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
    private fun registerConnectivityNetworkMonitor() {
        if (context != null) {
            val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val builder = NetworkRequest.Builder()
            connectivityManager.registerNetworkCallback(builder.build(),
                object : NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        if (activity != null) {
                            activity!!.runOnUiThread {
                                binding.layout.visibility = View.VISIBLE
                                binding.noNetworkResult.visibility = View.GONE
                                binding.textNoInternet.visibility = View.GONE
                                viewModel.smartCollection()
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
           viewModel.smartCollection()
        } else {
            binding.noNetworkResult.visibility = View.VISIBLE
            binding.layout.visibility = View.GONE
            binding.textNoInternet.visibility = View.VISIBLE
        }
    }

}