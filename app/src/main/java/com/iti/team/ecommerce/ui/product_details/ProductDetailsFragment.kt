package com.iti.team.ecommerce.ui.product_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.iti.team.ecommerce.databinding.FragmentProductDetailsBinding
import com.iti.team.ecommerce.model.data_classes.*

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations


class ProductDetailsFragment: Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var viewModel: ProductDetailsViewModel

    private lateinit var sliderAdapter: SliderAdapter

    private val args : ProductDetailsFragmentArgs by navArgs()
    private lateinit var productObject: Products
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

        binding.viewModel = viewModel




        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM)
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding.imageSlider.startAutoCycle()
        viewModel.setProduct( args.productObject)

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