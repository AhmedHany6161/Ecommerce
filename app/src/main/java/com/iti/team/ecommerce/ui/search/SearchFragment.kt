package com.iti.team.ecommerce.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.iti.team.ecommerce.databinding.FragmentSearchBinding
import com.iti.team.ecommerce.model.local.room.OfflineDatabase
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.ui.MainActivity

class SearchFragment: Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel:SearchViewModel by viewModels {
        SearchViewModelFactory( ModelRepository(
            OfflineDatabase.getInstance(requireActivity().application),
            requireActivity().applicationContext),   ModelRepository(
            OfflineDatabase.getInstance(requireActivity().application),
            requireActivity().applicationContext)
        )
    }
    private val arg:SearchFragmentArgs by navArgs()

    private val inputMethodManager by lazy{
         requireActivity()
        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

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
        binding.searchView.requestFocus()
        //inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.RESULT_UNCHANGED_HIDDEN)
        setUpSearchView()
        setUpRecyclerView()
        navigateToDetails()
        observeToLogin()
    }
    private fun setUpRecyclerView(){
        binding.productRecycler.layoutManager = GridLayoutManager(context,2)
    }

    private fun setUpSearchView(){
        binding.searchView.isIconified = false
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { Log.i("setUpSearchView", it) }
                inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                query?.let { viewModel.getData(it, arg.listProductType) }
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

    private fun observeToLogin(){
        viewModel.navigateToLogin.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let {
                val navigate = SearchFragmentDirections.actionSearchFragmentToLoginFragment()
                findNavController().navigate(navigate)
            }
        })
    }

    private fun navigateToDetails(
    ) {
        viewModel.navigateToDetails.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {it1->
                it1.second?.let { it2 -> navigate(it1.first, it2) }
            }
        })
    }


    private fun navigate(productObject: String,inWish:Boolean){
        val action = SearchFragmentDirections
            .actionSearchFragmentToProductDetailsFragment(productObject,inWish)
        findNavController().navigate(action)
    }

}