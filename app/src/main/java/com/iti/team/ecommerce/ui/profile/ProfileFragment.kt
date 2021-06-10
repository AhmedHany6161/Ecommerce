package com.iti.team.ecommerce.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.google.firebase.auth.FirebaseAuth
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.ui.MainActivity
import com.iti.team.ecommerce.ui.wish.WishListAdapter
import com.iti.team.ecommerce.ui.wish.WishListFragmentDirections
import com.iti.team.ecommerce.ui.wish.WishListViewModel

class ProfileFragment : Fragment() {


    private lateinit var  email: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.profile_wishlist)
        val profile: LottieAnimationView = view.findViewById(R.id.profile_image)
        val showAll: Button = view.findViewById(R.id.profile_show_all_wish)
         email = view.findViewById(R.id.profile_email)
        val viewModel: ProfileViewModel by viewModels()
        val wishListAdapter = ProfileWishAdapter(ArrayList(), viewModel)
        setupWishListRecyclerView(recyclerView, wishListAdapter)
        listForWishList(viewModel, wishListAdapter)
        listeningForNavigate(viewModel)
        listeningForLoginState(viewModel, profile)
        listingForAddCart(viewModel)
        showAll.setOnClickListener {
            findNavController().navigate(R.id.wishListFragment)
        }
        return view
    }

    private fun listingForAddCart(viewModel: ProfileViewModel) {
        viewModel.addToCart.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun listForWishList(
        viewModel: ProfileViewModel,
        wishListAdapter: ProfileWishAdapter
    ) {
        viewModel.getWishLis().observe(viewLifecycleOwner, {
            wishListAdapter.setData(it)
            wishListAdapter.notifyDataSetChanged()
        })
    }

    private fun listeningForNavigate(
        viewModel: ProfileViewModel
    ) {
        viewModel.navigateToDetails.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { it1 ->
                it1.second?.let { it2 -> navigate(it1.first, it2) }
            }
        })
    }

    private fun listeningForLoginState(
        viewModel: ProfileViewModel,
        profile: LottieAnimationView
    ) {
        if (viewModel.getLogInState()) {
            email.text = "Hi , ${FirebaseAuth.getInstance().currentUser}"
            profile.setAnimation(R.raw.login_profile)
        } else {
            profile.setAnimation(R.raw.error_animation)
        }
    }

    private fun navigate(productObject: String, inWish: Boolean) {
        val action = ProfileFragmentDirections
            .actionProfileFragmentToProductDetailsFragment(productObject, inWish)
        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).bottomNavigation.isGone = false
    }
    private fun setupWishListRecyclerView(
        recyclerView: RecyclerView,
        adapter: ProfileWishAdapter
    ) {
        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adapter
    }



}