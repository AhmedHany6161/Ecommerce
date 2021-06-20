package com.iti.team.ecommerce.ui.proudcts

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.ui.MainActivity
import com.iti.team.ecommerce.utils.NetworkConnection


class ProductsFragment : Fragment() {

    private val arg: ProductsFragmentArgs by navArgs()
    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var profile: LottieAnimationView

   private lateinit var noInternet: TextView
   private lateinit var  lottie: LottieAnimationView
    private lateinit var  stopLottie: LottieAnimationView
   private lateinit var productRecyclerView:RecyclerView
   private lateinit var  brandRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.products_fragment, container, false)
         productRecyclerView = view.findViewById(R.id.products_rec)
         brandRecyclerView = view.findViewById(R.id.brand_rec)
        val search: SearchView = view.findViewById(R.id.product_search)
        val title: TextView = view.findViewById(R.id.textView)
        val back: ImageView = view.findViewById(R.id.product_back)
        lottie = view.findViewById(R.id.no_network_result)
        stopLottie = view.findViewById(R.id.stop_animation)
         noInternet = view.findViewById(R.id.text_no_internet)
//        productRecyclerView.visibility = View.GONE
//        loti.visibility = View.VISIBLE
        profile = view.findViewById(R.id.shapeableImageView)
        title.text = arg.productType
        val productAdapter = ProductAdapter(ArrayList(),viewModel)
        val brandAdapter = BrandAdapter(ArrayList(),viewModel)
        checkNetwork()
        registerConnectivityNetworkMonitor()
        setupProductRecyclerView(productRecyclerView, productAdapter)
        setupBrandRecyclerView(brandRecyclerView, brandAdapter)
        listeningForProducts( productAdapter)
        setupSearch(search)
        listeningForBrand(brandAdapter)
        //viewModel.getProductsFromType(arg.productType)
        navigateToProfile()
        listingToBack(back)
        listingToAddCart()
        listeningForNavigate()
//        val container = view.findViewById(R.id.shimmer_view_container) as ShimmerFrameLayout
//        container.startShimmer()
        navigateToLogin()
        return view
    }

    private fun navigateToLogin() {
        viewModel.navigateToLogin.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(R.id.loginFragment)
            }
        })
    }

    private fun navigateToProfile() {
        profile.setOnClickListener {
            findNavController().popBackStack()
            findNavController().navigate(R.id.profileFragment)
        }
    }

    private fun listingToBack(back: ImageView) {
        back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun listingToAddCart() {
        viewModel.addToCart.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun listeningForLoginState() {

        if (viewModel.getLogInState()) {
            profile.setAnimation(R.raw.login_profile)
        } else {
            profile.setAnimation(R.raw.error_animation)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).bottomNavigation.isGone = true
        listeningForLoginState()
    }


    private fun listeningForBrand(
        brandAdapter: BrandAdapter
    ) {
        viewModel.getBrandData().observe(viewLifecycleOwner, {
            brandAdapter.setData(it)
            brandAdapter.notifyDataSetChanged()
        })
    }

    private fun listeningForProducts(
        adapter: ProductAdapter
    ) {
        viewModel.getProductsData().observe(viewLifecycleOwner, {
            adapter.setData(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listeningForNavigate(
    ) {
        viewModel.navigateToDetails.observe(viewLifecycleOwner, {
           it.getContentIfNotHandled()?.let {it1->
               it1.second?.let { it2 -> navigate(it1.first, it2) }
           }
        })
    }

    private fun setupProductRecyclerView(
        recyclerView: RecyclerView,
        adapter: ProductAdapter
    ) {
        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adapter
    }
    private fun setupBrandRecyclerView(
        recyclerView: RecyclerView,
        adapter: BrandAdapter
    ) {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
    }

    private fun setupSearch(
        search: SearchView,
    ) {
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.search(newText)
                return false
            }
        })
    }

    private fun navigate(productObject: String,inWish:Boolean){
        val action = ProductsFragmentDirections
            .actionProductsToProductDetailsFragment(productObject,inWish)
        findNavController().navigate(action)
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
                                noInternet.visibility = View.INVISIBLE
                                lottie.visibility = View.INVISIBLE
                                productRecyclerView.visibility = View.VISIBLE
                                brandRecyclerView.visibility = View.VISIBLE
                                stopLottie.visibility = View.VISIBLE
                                viewModel.getProductsFromType(arg.productType)
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
            viewModel.getProductsFromType(arg.productType)
        } else {
            productRecyclerView.visibility = View.GONE
            brandRecyclerView.visibility = View.GONE
            noInternet.visibility = View.VISIBLE
            lottie.visibility = View.VISIBLE
            stopLottie.visibility = View.GONE

        }
    }
}