package com.iti.team.ecommerce.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.ui.MainActivity
import com.iti.team.ecommerce.ui.shop_products.ShopProductsDirections


class ProfileFragment : Fragment(){
   // , PopupMenu.OnMenuItemClickListener {


    private lateinit var email: TextView
    private lateinit var pleaseLogin: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var showAll: Button
    private lateinit var showAllOrders: TextView
    private lateinit var profile: LottieAnimationView
    private lateinit var cart: ImageView
    private lateinit var cartCount: TextView

    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var profile_setting: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)
        recyclerView = view.findViewById(R.id.profile_wishlist)
        profile = view.findViewById(R.id.profile_image)
        showAll = view.findViewById(R.id.profile_show_all_wish)
        cartCount = view.findViewById(R.id.shop_cart_badge)
        val refresh: SwipeRefreshLayout = view.findViewById(R.id.profile_swipe_refresh)
        val unpaid: TextView = view.findViewById(R.id.profile_unpaid)
        val paid: TextView = view.findViewById(R.id.profile_paid)
        val refund: TextView = view.findViewById(R.id.profile_refund)
        val pending: TextView = view.findViewById(R.id.profile_pending)
        showAllOrders = view.findViewById(R.id.profile_show_all_orders)
        email = view.findViewById(R.id.profile_email)
        cart = view.findViewById(R.id.profile_shop_cart)
        pleaseLogin = view.findViewById(R.id.profile_please_login)
        profile_setting = view.findViewById(R.id.profile_setting)
        profile_setting.setOnClickListener {
            //showPopup(it)
            navigate()
        }
        val viewModel: ProfileViewModel by viewModels()
        val wishListAdapter = ProfileWishAdapter(ArrayList(), viewModel)
        setupWishListRecyclerView(wishListAdapter)
        listForWishList(wishListAdapter)
        listeningForNavigate(viewModel)
        listingForAddCart()
        navigateToWishList()
        navigateToLogin()
        viewModel.checkOrders()
        swipToRefresh(refresh, viewModel)
        listingForOrderChanges(paid, unpaid, refund, pending, refresh)
        navigateToOrders(showAllOrders, viewModel)
        navigateToCart()
        observeCartCount()
        observeForRemoveFav()
        return view
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.edit_profile_item -> {
                Toast.makeText(context, "ItemSelected = $item", Toast.LENGTH_SHORT)
                    .show()
                navigateToEditProfile()
            }
            R.id.logout_item -> logout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeForRemoveFav() {
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

    private fun navigateToCart() {
        cart.setOnClickListener {
            if (viewModel.getLogInState()) {
                findNavController().navigate(R.id.shoppingPageFragment)
            } else {
                findNavController().navigate(R.id.loginFragment)
            }
        }
    }

    private fun navigateToOrders(showAllOrders: TextView, viewModel: ProfileViewModel) {
        showAllOrders.setOnClickListener {
            if (viewModel.getLogInState()) {
                findNavController().navigate(R.id.myOrdersFragment)
            } else {
                findNavController().navigate(R.id.loginFragment)
            }
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

    private fun navigateToLogin() {
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


    private fun listingForAddCart() {
        viewModel.addToCart.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun listForWishList(
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

    private fun observeCartCount() {
        viewModel.cartCount.observe(viewLifecycleOwner, {
            if (it == 0) {
                cartCount.visibility = View.GONE
            } else {
                cartCount.text = "$it"
                cartCount.visibility = View.VISIBLE
            }

        })
    }

    private fun listeningForLoginState(
    ) {
        if (viewModel.getLogInState()) {
            recyclerView.visibility = View.VISIBLE
            showAll.visibility = View.VISIBLE
            pleaseLogin.visibility = View.GONE
            email.text = "Hi , ${viewModel.getUserName()}"
            profile.setAnimation(R.raw.login_profile)
            showAllOrders.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.GONE
            showAll.visibility = View.GONE
            pleaseLogin.visibility = View.VISIBLE
            profile.setAnimation(R.raw.error_animation)

            showAllOrders.visibility = View.GONE
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
        listeningForLoginState()

    }
    private fun setupWishListRecyclerView(
        adapter: ProfileWishAdapter
    ) {
        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adapter
    }

//    private fun showPopup(view: View) {
//        val popup = context?.let { PopupMenu(it, view) }
//        if (popup != null) {
//            popup.inflate(R.menu.setting_menu)
//            popup.show()
//            popup.setOnMenuItemClickListener(this)
//
//        }
//    }

//    override fun onMenuItemClick(item: MenuItem?): Boolean {
//        when (item?.getItemId()) {
//            R.id.edit_profile_item -> {
//                navigateToEditProfile()
//                return true
//            }
//            R.id.logout_item -> {
//                logout()
//
//                return true
//            }
//            else->{
//                return false
//            }
//        }
//    }


    private fun logout() {
        viewModel.logout()
        findNavController().popBackStack()
        findNavController().navigate(R.id.profileFragment)

    }

    private fun navigateToEditProfile() {
        findNavController().navigate(R.id.loginFragment)
    }

    private fun navigate(){
        val action = ProfileFragmentDirections.actionProfileFragmentToSettingFragment()
        findNavController().navigate(action)
    }

}