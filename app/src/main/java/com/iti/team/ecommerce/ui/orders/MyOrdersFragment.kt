package com.iti.team.ecommerce.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.ui.MainActivity

class MyOrdersFragment : Fragment() {
    private lateinit var noData: LottieAnimationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var card:CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.my_orders_fragment, container, false)
        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)
        val viewModel: MyOrdersViewModel by viewModels()
        val refresh: SwipeRefreshLayout = view.findViewById(R.id.order_swipe)
        recyclerView = view.findViewById(R.id.order_rec)
        card = view.findViewById(R.id.myOrder_card)
        val backBTN: ImageView = view.findViewById(R.id.my_order_back)
        noData = view.findViewById(R.id.order_no_data)
        val adapter = MyOrderAdapter(ArrayList())
        listingToBack(backBTN)
        setupOrdersListRecyclerView(adapter)
        swipToRefresh(refresh, viewModel)
        viewModel.getOrders()
        listingForOrderChanges(viewModel, refresh, adapter)
        onTabSelected(tabLayout, viewModel)
        return view
    }

    private fun listingToBack(backBTN: ImageView) {
        backBTN.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun onTabSelected(
        tabLayout: TabLayout,
        viewModel: MyOrdersViewModel
    ) {
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewModel.filterByStatus(tab.text.toString().lowercase())
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun swipToRefresh(
        refresh: SwipeRefreshLayout,
        viewModel: MyOrdersViewModel
    ) {
        refresh.setOnRefreshListener {
            viewModel.getOrders()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).bottomNavigation.isGone = true
    }

    private fun listingForOrderChanges(
        viewModel: MyOrdersViewModel,
        refresh: SwipeRefreshLayout,
        adapter: MyOrderAdapter
    ) {
        viewModel.orders.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                adapter.setData(it)
                adapter.notifyDataSetChanged()
                noData.visibility = View.GONE
                card.visibility = View.VISIBLE
            }else{
                noData.visibility = View.VISIBLE
                card.visibility = View.GONE
            }
            refresh.isRefreshing = false
        })
    }

    private fun setupOrdersListRecyclerView(
        adapter: MyOrderAdapter) {
        val gridLayoutManager = LinearLayoutManager(context)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adapter
    }
}