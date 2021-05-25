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
        val recyclerView: RecyclerView = view.findViewById(R.id.products_rec)
        val search: SearchView = view.findViewById(R.id.product_search)
        val adapter = ProductAdapter(ArrayList())
        val viewModel: ProductsViewModel by viewModels()
        setupRecyclerView(recyclerView, adapter)
        listeningForProducts(viewModel, adapter)
        setupSearch(search, viewModel)
        return view
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

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        adapter: ProductAdapter
    ) {
        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = gridLayoutManager
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