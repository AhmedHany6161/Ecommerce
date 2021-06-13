package com.iti.team.ecommerce.ui.shop_bag

import android.os.Bundle
import android.util.Log
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
import androidx.room.TypeConverter
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.databinding.FragmentShopBagBinding
import com.iti.team.ecommerce.model.data_classes.Product
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.ui.MainActivity
import com.iti.team.ecommerce.utils.Constants
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types
import kotlinx.android.synthetic.main.fragment_shop_bag.view.*

class ShoppingPageFragment: Fragment() {

    private lateinit var binding:FragmentShopBagBinding
    val viewModel: ShoppingPageViewModel by viewModels()
    private  var productList:List<Product> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shop_bag, container, false)
        binding = FragmentShopBagBinding.inflate(inflater)
        binding.textView.text = "Shopping Bag"

        binding.viewModel = viewModel
        val shoppingPageAdapter = ShoppingPageAdapter(ArrayList(), viewModel,context)
        setupShopListRecyclerView(shoppingPageAdapter)
        observeButtonBackClicked(viewModel)
        listForShopList(viewModel, shoppingPageAdapter)
        ObserveData(viewModel)
        viewModel.total_price().observe(viewLifecycleOwner,{
            binding.price.text = "EGP ${viewModel.total}"
        })
        binding.cheakoutBtn.setOnClickListener {
            if(viewModel.total ==0.0){
                val action = ShoppingPageFragmentDirections.actionFromShopBagToShopFragment()
                findNavController().navigate(action)
            }else{
                val action = ShoppingPageFragmentDirections.
                actionShoppingPageFragmentToPayment(viewModel.total.toString(),"")
                findNavController().navigate(action)
            }
        }
        binding.favoriteImage.setOnClickListener {
            val action = ShoppingPageFragmentDirections.actionFromShopBagToWishlist()
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun ObserveData(viewModel: ShoppingPageViewModel){
        viewModel.getCardList().observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.emptystatusImg.visibility = View.VISIBLE
                binding.txtEmpty.visibility = View.VISIBLE
                binding.cheakoutBtn.text="SHOP NOW"
                // Toast.makeText(this, "no data: ", Toast.LENGTH_LONG).show()
            } else {
                it?.let {  productList = it }
                binding.emptystatusImg.visibility = View.INVISIBLE
                binding.txtEmpty.visibility = View.INVISIBLE
                binding.cheakoutBtn.text="CHECKOUT"
            }
        }
        observeCheckoutButton()

    }

    private fun observeCheckoutButton(){
        viewModel.buttonCheckoutClicked.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let {
                if(viewModel.total ==0.0){
                    val action = ShoppingPageFragmentDirections.actionFromShopBagToShopFragment()
                    findNavController().navigate(action)
                }else{
                    val action = ShoppingPageFragmentDirections.
                    actionShoppingPageFragmentToPayment(viewModel.total.toString(),convertObjectToString(productList))
                    findNavController().navigate(action)
                }

            }
        })

    }

    private fun convertObjectToString(productObject: List<Product>):String{
         val productListType = Types.newParameterizedType(List::class.java, Product::class.java)
         val adapterProductList: JsonAdapter<List<Product>> = Constants.moshi.adapter(productListType)
        return adapterProductList.toJson(productObject)

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
        adapter: ShoppingPageAdapter
    ) {
        val gridLayoutManager = GridLayoutManager(context, 1)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        binding.shopBagRecycler.layoutManager = gridLayoutManager
        binding.shopBagRecycler.adapter = adapter
    }

    private fun observeButtonBackClicked(viewModel: ShoppingPageViewModel){
        viewModel.buttonBackClicked.observe(viewLifecycleOwner,{
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .popBackStack()

        })
    }




}