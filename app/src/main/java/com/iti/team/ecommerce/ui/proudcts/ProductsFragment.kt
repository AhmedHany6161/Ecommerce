package com.iti.team.ecommerce.ui.proudcts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
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


class ProductsFragment : Fragment() {

    private val arg:ProductsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.products_fragment, container, false)
        val productRecyclerView: RecyclerView = view.findViewById(R.id.products_rec)
        val brandRecyclerView: RecyclerView = view.findViewById(R.id.brand_rec)
        val search: SearchView = view.findViewById(R.id.product_search)
        val title: TextView = view.findViewById(R.id.textView)
        val back: ImageView = view.findViewById(R.id.product_back)

        val profile: LottieAnimationView = view.findViewById(R.id.shapeableImageView)
        title.text = arg.productType
        val viewModel: ProductsViewModel by viewModels()
        val productAdapter = ProductAdapter(ArrayList(), viewModel)
        val brandAdapter = BrandAdapter(ArrayList(), viewModel)
        setupProductRecyclerView(productRecyclerView, productAdapter)
        setupBrandRecyclerView(brandRecyclerView, brandAdapter)
        listeningForProducts(viewModel, productAdapter)
        setupSearch(search, viewModel)
        listeningForBrand(viewModel, brandAdapter)
        viewModel.getProductsFromType(arg.productType)
        listeningForLoginState(viewModel, profile)
        navigateToProfile(profile)
        listingToBack(back)
        listingToAddCart(viewModel)
        listeningForNavigate(viewModel)
//        val container = view.findViewById(R.id.shimmer_view_container) as ShimmerFrameLayout
//        container.startShimmer()
        return view
    }

    private fun navigateToProfile(profile: LottieAnimationView) {
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

    private fun listingToAddCart(viewModel: ProductsViewModel) {
        viewModel.addToCart.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun listeningForLoginState(
        viewModel: ProductsViewModel,
        profile: LottieAnimationView
    ) {

        if (viewModel.getLogInState()) {
            profile.setAnimation(R.raw.login_profile)
        } else {
            profile.setAnimation(R.raw.error_animation)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).bottomNavigation.isGone = true
    }


    private fun listeningForBrand(
        viewModel: ProductsViewModel,
        brandAdapter: BrandAdapter
    ) {
        viewModel.getBrandData().observe(viewLifecycleOwner, {
            brandAdapter.setData(it)
            brandAdapter.notifyDataSetChanged()
        })
    }

    private fun listeningForProducts(
        viewModel: ProductsViewModel,
        adapter: ProductAdapter
    ) {
        viewModel.getProductsData().observe(viewLifecycleOwner, {
            adapter.setData(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listeningForNavigate(
        viewModel: ProductsViewModel
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
        viewModel: ProductsViewModel
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
}