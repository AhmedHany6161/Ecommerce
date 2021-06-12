package com.iti.team.ecommerce.ui.shop_bag

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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.databinding.FragmentShopBagBinding
import com.iti.team.ecommerce.ui.MainActivity
import com.iti.team.ecommerce.ui.wish.WishListAdapter
import com.iti.team.ecommerce.ui.wish.WishListViewModel

class ShoppingPageFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shop_bag, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.shop_bag_recycler)
        val title: TextView = view.findViewById(R.id.textView)
        var total_price :TextView = view.findViewById(R.id.price)
        title.text = "Shopping Bag"
        val viewModel: ShoppingPageViewModel by viewModels()
        val shoppingPageAdapter = ShoppingPageAdapter(ArrayList(), viewModel,context)
        setupShopListRecyclerView(recyclerView, shoppingPageAdapter)
        observeButtonBackClicked(viewModel)
        listForShopList(viewModel, shoppingPageAdapter)
        viewModel.total_price().observe(this,{
            total_price.text = "EGP ${viewModel.total}"
        })

        return view
    }

    private fun listForShopList(
        viewModel: ShoppingPageViewModel,
        shoppingPageAdapter: ShoppingPageAdapter
    ) {
        viewModel.getCardList().observe(viewLifecycleOwner, {
            shoppingPageAdapter.setData(it)
            shoppingPageAdapter.notifyDataSetChanged()

        })
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).bottomNavigation.isGone = true
    }

    private fun setupShopListRecyclerView(
        recyclerView: RecyclerView,
        adapter: ShoppingPageAdapter
    ) {
        val gridLayoutManager = GridLayoutManager(context, 1)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adapter
    }

    private fun observeButtonBackClicked(viewModel: ShoppingPageViewModel){
        viewModel.buttonBackClicked.observe(viewLifecycleOwner,{
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .popBackStack()

        })
    }


}