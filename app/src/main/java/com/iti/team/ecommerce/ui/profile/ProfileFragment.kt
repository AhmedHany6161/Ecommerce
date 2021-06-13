package com.iti.team.ecommerce.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.ui.MainActivity

class ProfileFragment : Fragment() {


    private lateinit var email: TextView
    private lateinit var pleaseLogin: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var showAll: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)
        recyclerView = view.findViewById(R.id.profile_wishlist)
        val profile: LottieAnimationView = view.findViewById(R.id.profile_image)
        showAll = view.findViewById(R.id.profile_show_all_wish)
        val refresh: SwipeRefreshLayout = view.findViewById(R.id.profile_swipe_refresh)
        val unpaid: TextView = view.findViewById(R.id.profile_unpaid)
        val paid: TextView = view.findViewById(R.id.profile_paid)
        val refund: TextView = view.findViewById(R.id.profile_refund)
        val pending: TextView = view.findViewById(R.id.profile_pending)
        val showAllOrders: TextView = view.findViewById(R.id.profile_show_all_orders)

        email = view.findViewById(R.id.profile_email)
        pleaseLogin = view.findViewById(R.id.profile_please_login)
        val viewModel: ProfileViewModel by viewModels()
        val wishListAdapter = ProfileWishAdapter(ArrayList(), viewModel)
        setupWishListRecyclerView(wishListAdapter)
        listForWishList(viewModel, wishListAdapter)
        listeningForNavigate(viewModel)
        listeningForLoginState(viewModel, profile)
        listingForAddCart(viewModel)
        navigateToWishList()
        navigateToLogin(viewModel)
        viewModel.checkOrders()
        swipToRefresh(refresh, viewModel)
        listingForOrderChanges(viewModel, paid, unpaid, refund, pending, refresh)
        navigateToOrders(showAllOrders)
        return view
    }

    private fun navigateToOrders(showAllOrders: TextView) {
        showAllOrders.setOnClickListener {
            findNavController().navigate(R.id.myOrdersFragment)
        }
    }

    private fun swipToRefresh(
        refresh: SwipeRefreshLayout,
        viewModel: ProfileViewModel
    ) {
        refresh.setOnRefreshListener {
            viewModel.checkOrders()
        }
    }

    private fun listingForOrderChanges(
        viewModel: ProfileViewModel,
        paid: TextView,
        unpaid: TextView,
        refund: TextView,
        pending: TextView,
        refresh: SwipeRefreshLayout
    ) {
        viewModel.orders.observe(viewLifecycleOwner, {
            val tokens = it.split(",")
            paid.text = tokens[0]
            unpaid.text = tokens[1]
            refund.text = tokens[2]
            pending.text = tokens[3]
            refresh.isRefreshing = false
        })
    }

    private fun navigateToWishList() {
        showAll.setOnClickListener {
            findNavController().navigate(R.id.wishListFragment)
        }
    }

    private fun navigateToLogin(viewModel: ProfileViewModel) {
        email.setOnClickListener {
            if (!viewModel.getLogInState()) {
                findNavController().navigate(R.id.loginFragment)
            }
        }
        pleaseLogin.setOnClickListener {
            if (!viewModel.getLogInState()) {
                findNavController().navigate(R.id.loginFragment)
            }
        }
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
            recyclerView.visibility = View.VISIBLE
            showAll.visibility = View.VISIBLE
            pleaseLogin.visibility = View.GONE
            email.text = "Hi , ${FirebaseAuth.getInstance().currentUser}"
            profile.setAnimation(R.raw.login_profile)
        } else {
            recyclerView.visibility = View.GONE
            showAll.visibility = View.GONE
            pleaseLogin.visibility = View.VISIBLE
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
        (activity as MainActivity).bottomNavigation.show(3)

    }
    private fun setupWishListRecyclerView(
        adapter: ProfileWishAdapter
    ) {
        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adapter
    }



}