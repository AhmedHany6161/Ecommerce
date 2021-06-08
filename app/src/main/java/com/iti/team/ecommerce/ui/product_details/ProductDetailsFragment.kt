package com.iti.team.ecommerce.ui.product_details

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.databinding.FragmentProductDetailsBinding
import com.iti.team.ecommerce.ui.MainActivity
import com.iti.team.ecommerce.ui.shop.ShopFragmentDirections
import com.iti.team.ecommerce.ui.shop_products.ShopProductsViewModel

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations


class ProductDetailsFragment: Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding

    private val viewModel by lazy {
        ProductDetailsViewModel(requireActivity().application)
    }

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


        init()
        return binding.root
    }

    private fun init(){
        manager = parentFragmentManager
        moreFragment = MoreMenu(viewModel)
        transaction = manager.beginTransaction()
        transaction.add(R.id.more_fragment,moreFragment,"MORE_FRAGMENT")
        transaction.commit()
        binding.viewModel = viewModel
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM)
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding.imageSlider.startAutoCycle()
        viewModel.setProduct( args.productObject)
        viewModel.inWish(args.inWish)

        observeData()
        setMenu()



    }

    private fun observeData(){
        observeImageSlider()
        observeButtonBackClicked()
        //observeInWish()
        observeToLogin()

    }

//    fun observeInWish(){
//        viewModel.inWish.observe(viewLifecycleOwner,{
//            it.getContentIfNotHandled()?.let {
//                binding.favorite.setColorFilter(Color.RED,android.graphics.PorterDuff.Mode.MULTIPLY)
//            }
//        })
//    }
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

    private fun observeToLogin(){
        viewModel.navigateToLogin.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let {
                val navigate = ProductDetailsFragmentDirections
                    .actionProductDetailsFragmentToLoginFragment()
                findNavController().navigate(navigate)
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