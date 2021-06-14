package com.iti.team.ecommerce.ui.orders

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.iti.team.ecommerce.R

class MyOrdersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.my_orders_fragment, container, false)
        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)
        val viewModel: MyOrdersViewModel by viewModels()
        val refresh: SwipeRefreshLayout = view.findViewById(R.id.order_swipe)
        val recyclerView: RecyclerView = view.findViewById(R.id.order_rec)
        val backBTN: ImageView = view.findViewById(R.id.my_order_back)
        val adapter = MyOrderAdapter(ArrayList())
        backBTN.setOnClickListener {
            findNavController().popBackStack()
        }
        setupOrdersListRecyclerView(adapter, recyclerView)
        swipToRefresh(refresh, viewModel)
        viewModel.getOrders()
        listingForOrderChanges(viewModel, refresh, adapter)
        onTabSelected(tabLayout, viewModel)
        return view
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

    private fun listingForOrderChanges(
        viewModel: MyOrdersViewModel,
        refresh: SwipeRefreshLayout,
        adapter: MyOrderAdapter
    ) {
        viewModel.orders.observe(viewLifecycleOwner, {
            adapter.setData(it)
            adapter.notifyDataSetChanged()
            refresh.isRefreshing = false
        })
    }

    private fun setupOrdersListRecyclerView(
        adapter: MyOrderAdapter,
        recyclerView: RecyclerView
    ) {
        val gridLayoutManager = LinearLayoutManager(context)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adapter
    }
}