package com.iti.team.ecommerce.ui.shop_bag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.ui.MainActivity

class ShoppingPageFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shop_bag, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.shop_bag_recycler)
        val title: TextView = view.findViewById(R.id.textView)
        val total_price :TextView = view.findViewById(R.id.price)
        title.text = "Shopping Bag"
        val cheak_Btn : Button = view.findViewById(R.id.cheakout_btn)
        val favorite_image :ImageView = view.findViewById(R.id.favorite_image)
        val viewModel: ShoppingPageViewModel by viewModels()
        val shoppingPageAdapter = ShoppingPageAdapter(ArrayList(), viewModel,context)
        setupShopListRecyclerView(recyclerView, shoppingPageAdapter)
        observeButtonBackClicked(viewModel)
        listForShopList(viewModel, shoppingPageAdapter)
        ObserveData(viewModel, view)
        viewModel.total_price().observe(this,{
            total_price.text = "EGP ${viewModel.total}"
        })
        cheak_Btn.setOnClickListener {
            if(viewModel.total ==0.0){
                val action = ShoppingPageFragmentDirections.actionFromShopBagToShopFragment()
                findNavController().navigate(action)
            }else{
                //navigate to order
            }
        }
        favorite_image.setOnClickListener {
            val action = ShoppingPageFragmentDirections.actionFromShopBagToWishlist()
            findNavController().navigate(action)
        }

        return view
    }

    private fun ObserveData(viewModel: ShoppingPageViewModel ,view: View){
        val empty :ImageView = view.findViewById(R.id.emptystatus_img)
        val empty_txt :TextView =view.findViewById(R.id.txt_empty)
        val cheak_Btn : Button = view.findViewById(R.id.cheakout_btn)
        viewModel.getCardList().observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                empty.visibility = View.VISIBLE
                empty_txt.visibility = View.VISIBLE
                cheak_Btn.text="SHOP NOW"
                // Toast.makeText(this, "no data: ", Toast.LENGTH_LONG).show()
            } else {
                empty.visibility = View.INVISIBLE
                empty_txt.visibility = View.INVISIBLE
                cheak_Btn.text="CHECKOUT"
            }
        }

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