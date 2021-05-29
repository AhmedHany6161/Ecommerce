package com.iti.team.ecommerce.ui.product_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.iti.team.ecommerce.databinding.FragmentProductDetailsBinding
import com.iti.team.ecommerce.model.data_classes.*

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations


class ProductDetailsFragment: Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var viewModel: ProductDetailsViewModel

    private lateinit var sliderAdapter: SliderAdapter

    private lateinit var productObject:ProductsModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(ProductDetailsViewModel::class.java)


        init()
        return binding.root
    }

    private fun init(){


        val vendors = listOf( Variants(price = "170.0",quantity = 3,requiresShipping = true,
            inventoryManagement ="ADDiDAS",taxable = true))
        val products = listOf(Products(productId = 6687366217926, productType = "SHOSE",
            description = "this is first product test !!!!!",title = "ADDIDAS",status = "true",
            vendor = "ADDiDAS",variants = vendors))
        productObject = ProductsModel(products)

       val images = listOf(
           Images("https://cdn.shopify.com/s/files/1/0567/9310/4582/products/44694ee386818f3276566210464cf341.jpg?v=1621288163")
           ,Images("https://cdn.shopify.com/s/files/1/0567/9310/4582/products/e8490702c423e6c62d356cace500822f.jpg?v=1621288163"),Images(""),Images(""))


        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM)
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding.imageSlider.startAutoCycle()

       // viewModel.setProduct(productObject)
        //viewModel.getProductImage()

        observeData()



    }

    private fun observeData(){
        observeImageSlider()
    }

    private fun  observeImageSlider(){
        viewModel.imageProduct.observe(viewLifecycleOwner,{
            sliderAdapter = SliderAdapter(it)
            binding.imageSlider.setSliderAdapter(sliderAdapter)
        })
    }

    companion object {

    }

}