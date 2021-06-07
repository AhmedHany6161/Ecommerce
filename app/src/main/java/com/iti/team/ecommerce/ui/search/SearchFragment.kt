package com.iti.team.ecommerce.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.iti.team.ecommerce.databinding.FragmentSearchBinding
import com.iti.team.ecommerce.ui.MainActivity
import com.iti.team.ecommerce.ui.shop_products.ShopProductsViewModel

class SearchFragment: Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by lazy {
        SearchViewModel(requireActivity().application)
    }
    private val arg:SearchFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        init()

        return binding.root
    }

    private fun init(){
        binding.viewModel = viewModel
        setUpSearchView()
        setUpRecyclerView()
    }
    private fun setUpRecyclerView(){
        binding.productRecycler.layoutManager = GridLayoutManager(context,2)
    }

    private fun setUpSearchView(){
        binding.searchView.isIconified = false
        binding.searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { Log.i("setUpSearchView", it) }
                val inputMethodManager =  requireActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken,0)
                query?.let {viewModel.getData(it,arg.listProductType)}
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).bottomNavigation.isGone = true
    }
}