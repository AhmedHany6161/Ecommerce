package com.iti.team.ecommerce.ui.wish

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.ui.MainActivity

class WishListFragment : Fragment() {
    private lateinit var noData: LottieAnimationView
    private lateinit var mainAnimation: LottieAnimationView
    private lateinit var recyclerView:RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.products_fragment, container, false)
         recyclerView= view.findViewById(R.id.products_rec)
        val search: SearchView = view.findViewById(R.id.product_search)
        val title: TextView = view.findViewById(R.id.textView)
        val profile: LottieAnimationView = view.findViewById(R.id.shapeableImageView)
        val back: ImageView = view.findViewById(R.id.product_back)
        title.text = "Wish List"
        noData = view.findViewById(R.id.no_network_result)
        mainAnimation = view.findViewById(R.id.stop_animation)
        noData.setAnimation(R.raw.no_datar)
        val viewModel: WishListViewModel by viewModels()
        val wishListAdapter = WishListAdapter(ArrayList(), viewModel)
        setupWishListRecyclerView(wishListAdapter)
        setupSearch(search, viewModel)
        listForWishList(viewModel, wishListAdapter)
        listeningForNavigate(viewModel)
        listeningForLoginState(viewModel, profile)
        listingForAddToCart(viewModel)
        listingForProfileClick(profile)
        listingToBack(back)
        observeForRemoveFav(viewModel)
        return view
    }

    private fun observeForRemoveFav(viewModel: WishListViewModel) {
        viewModel.remove.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { it1 ->
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Remove")
                    .setMessage("Are you sure to remove from wish list ?")
                    .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                        dialog.cancel()
                    }
                    .setNegativeButton(resources.getString(R.string.yes)) { dialog, which ->
                        viewModel.removeFromWishList(it1)
                        dialog.cancel()

                    }.show()
            }
        })
    }

    private fun listingToBack(back: ImageView) {
        back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun listingForAddToCart(viewModel: WishListViewModel) {
        viewModel.addToCart.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun listingForProfileClick(profile: LottieAnimationView) {
        profile.setOnClickListener {
            findNavController().popBackStack()
            findNavController().navigate(R.id.profileFragment)
        }
    }

    private fun listForWishList(
        viewModel: WishListViewModel,
        wishListAdapter: WishListAdapter
    ) {
        viewModel.getWishLis().observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                recyclerView.visibility = View.VISIBLE
                noData.visibility = View.INVISIBLE
                mainAnimation.visibility = View.VISIBLE
                wishListAdapter.setData(it)
                wishListAdapter.notifyDataSetChanged()
            } else {
                recyclerView.visibility = View.INVISIBLE
                noData.visibility = View.VISIBLE
                mainAnimation.visibility = View.INVISIBLE
            }
        })
    }

    private fun listeningForNavigate(
        viewModel: WishListViewModel
    ) {
        viewModel.navigateToDetails.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { it1 ->
                it1.second?.let { it2 -> navigate(it1.first, it2) }
            }
        })
    }

    private fun listeningForLoginState(
        viewModel: WishListViewModel,
        profile: LottieAnimationView
    ) {
        if (viewModel.getLogInState()) {
            profile.setAnimation(R.raw.login_profile)
        } else {
            profile.setAnimation(R.raw.error_animation)
        }
    }

    private fun navigate(productObject: String, inWish: Boolean) {
        val action = WishListFragmentDirections
            .actionWishListFragmentToProductDetailsFragment(productObject, inWish)
        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).bottomNavigation.isGone = true
    }

    private fun setupWishListRecyclerView(
        adapter: WishListAdapter
    ) {
        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adapter
    }


    private fun setupSearch(
        search: SearchView,
        viewModel: WishListViewModel
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

}