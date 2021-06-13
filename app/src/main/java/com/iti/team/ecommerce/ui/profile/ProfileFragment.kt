package com.iti.team.ecommerce.ui.profile

import android.content.SharedPreferences
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
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.model.local.preferances.MySharedPreference
import com.iti.team.ecommerce.model.local.preferances.PreferenceDataSource
import com.iti.team.ecommerce.ui.MainActivity


class ProfileFragment : Fragment() {


    private lateinit var email: TextView
    private lateinit var pleaseLogin: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var showAll: Button
    private lateinit var profile_setting: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)
        recyclerView = view.findViewById(R.id.profile_wishlist)
        val profile: LottieAnimationView = view.findViewById(R.id.profile_image)
        showAll = view.findViewById(R.id.profile_show_all_wish)
        email = view.findViewById(R.id.profile_email)
        pleaseLogin = view.findViewById(R.id.profile_please_login)
        profile_setting = view.findViewById(R.id.profile_setting)
        profile_setting.setOnClickListener({
            showPopup(it)
        })

        val viewModel: ProfileViewModel by viewModels()
        val wishListAdapter = ProfileWishAdapter(ArrayList(), viewModel)
        setupWishListRecyclerView(wishListAdapter)
        listForWishList(viewModel, wishListAdapter)
        listeningForNavigate(viewModel)
        listeningForLoginState(viewModel, profile)
        listingForAddCart(viewModel)
        navigateToWishList()
        navigateToLogin(viewModel)
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

    private fun logout() {
    // val pref = PreferenceDataSource(MySharedPreference())

    }

    private fun navigateToEditProfile() {
        findNavController().navigate(R.id.loginFragment)
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

    private fun showPopup(view: View) {
        val popup = context?.let { PopupMenu(it, view) }
        if (popup != null) {
            popup.inflate(R.menu.setting_menu)
            popup.show()
        }
    }

}