package com.iti.team.ecommerce.ui.proudcts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iti.team.ecommerce.R


class Products : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.products_fragment, container, false)
        val productRecyclerView: RecyclerView = view.findViewById(R.id.products_rec)
        val brandRecyclerView: RecyclerView = view.findViewById(R.id.brand_rec)
        val search: SearchView = view.findViewById(R.id.product_search)
        val productAdapter = ProductAdapter(ArrayList())
        val viewModel: ProductsViewModel by viewModels()
        val brandAdapter = BrandAdapter(ArrayList(),viewModel)
        setupProductRecyclerView(productRecyclerView, productAdapter)
        setupBrandRecyclerView(brandRecyclerView,brandAdapter)
        listeningForProducts(viewModel, productAdapter)
        setupSearch(search, viewModel)
        listeningForBrand(viewModel, brandAdapter)
        return view
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

}