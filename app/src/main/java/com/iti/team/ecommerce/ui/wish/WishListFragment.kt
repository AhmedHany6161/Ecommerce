package com.iti.team.ecommerce.ui.wish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.ui.MainActivity
import com.iti.team.ecommerce.ui.proudcts.BrandAdapter
import com.iti.team.ecommerce.ui.proudcts.ProductAdapter
import com.iti.team.ecommerce.ui.proudcts.ProductsViewModel

class WishListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.products_fragment, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.products_rec)
        val search: SearchView = view.findViewById(R.id.product_search)
        val title: TextView = view.findViewById(R.id.textView)
        title.text = "Wish List"
        val viewModel: WishListViewModel by viewModels()
        val wishListAdapter = WishListAdapter(ArrayList(), viewModel)
        setupWishListRecyclerView(recyclerView, wishListAdapter)
        setupSearch(search, viewModel)
        listForWishList(viewModel, wishListAdapter)
        return view
    }

    private fun listForWishList(
        viewModel: WishListViewModel,
        wishListAdapter: WishListAdapter
    ) {
        viewModel.getWishLis().observe(viewLifecycleOwner, {
            wishListAdapter.setData(it)
            wishListAdapter.notifyDataSetChanged()
        })
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).bottomNavigation.isGone = true
    }

    private fun setupWishListRecyclerView(
        recyclerView: RecyclerView,
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