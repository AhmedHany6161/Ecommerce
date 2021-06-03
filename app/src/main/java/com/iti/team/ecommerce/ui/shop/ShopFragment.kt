package com.iti.team.ecommerce.ui.shop

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.iti.team.ecommerce.databinding.FragmentShopBinding

class ShopFragment : Fragment() {

    private lateinit var viewModel: ShopViewModel
    private lateinit var binding: FragmentShopBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        init()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
    private fun init(){
        binding.viewModel = viewModel
        itemsClicked()
        observeData()

    }

    private fun itemsClicked(){
        binding.tShirtLayout.subCategoryCard.setOnClickListener {
            navigate("T-SHIRTS")
        }

        binding.shoesLayout.subCategoryCard.setOnClickListener {
            navigate("SHOES")
        }

        binding.accessoryLayout.subCategoryCard.setOnClickListener {
            navigate("ACCESSORIES")
        }
    }

    private fun observeData(){
        observeShowErrorDialog()
        observeShowSuccessDialog()
    }

    private fun observeShowSuccessDialog(){
        viewModel.showSuccessDialog.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let {
                Log.i("observeSuccessDialog", it)
                val dialog = DiscountDialog.newInstance(viewModel,it)
                dialog.show(this.childFragmentManager, "SuccessDialog")
            }

        })
    }
    private fun observeShowErrorDialog(){
        viewModel.showErrorDialog.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let {
                Log.i("observeErrorDialog", it)
                val dialog = DiscountDialog.newInstance(viewModel,it)
                dialog.show(this.childFragmentManager, "ErrorDialog")
            }

        })
    }

    private fun navigate(productType: String){
        val action = ShopFragmentDirections.actionShopFragmentToProducts(productType)
        findNavController().navigate(action)
    }
    companion object {

    }
}