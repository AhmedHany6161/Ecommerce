package com.iti.team.ecommerce.ui.product_details

import android.os.Bundle
import android.view.*
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.databinding.FragmentProductDetailsBinding
import com.iti.team.ecommerce.ui.MainActivity

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations


class ProductDetailsFragment: Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var viewModel: ProductDetailsViewModel

    private lateinit var sliderAdapter: SliderAdapter

    private lateinit var moreFragment: MoreMenu
    private lateinit var manager:FragmentManager
    private lateinit var transaction: FragmentTransaction

    private val args : ProductDetailsFragmentArgs by navArgs()
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
        manager = parentFragmentManager
        moreFragment = MoreMenu()
        transaction = manager.beginTransaction()
        transaction.add(R.id.more_fragment,moreFragment,"MORE_FRAGMENT")
        transaction.commit()
        binding.viewModel = viewModel
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM)
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding.imageSlider.startAutoCycle()
        viewModel.setProduct( args.productObject)

        observeData()
        setMenu()



    }

    private fun observeData(){
        observeImageSlider()
        observeButtonBackClicked()
    }

    private fun  observeImageSlider(){
        viewModel.imageProduct.observe(viewLifecycleOwner,{
            sliderAdapter = SliderAdapter(it)
            binding.imageSlider.setSliderAdapter(sliderAdapter)
        })
    }

    private fun observeButtonBackClicked(){
        viewModel.buttonBackClicked.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .popBackStack()
            }
        })
    }

    private fun setMenu(){

    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).bottomNavigation.isGone = true
    }


    companion object {

    }

}