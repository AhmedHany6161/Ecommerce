package com.iti.team.ecommerce.ui.address

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.iti.team.ecommerce.databinding.FragmentShippingAddressBinding
import com.iti.team.ecommerce.ui.shop.ShopFragmentDirections
import com.iti.team.ecommerce.utils.Constants

class AddressFragment: Fragment() {

    private lateinit var binding: FragmentShippingAddressBinding
    private val viewModel by lazy {
        AddressViewModel(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShippingAddressBinding.inflate(inflater)

        init()
        return binding.root
    }

    private fun init(){

        binding.viewModel = viewModel
        viewModel.getAddress()

        observeData()
    }

    private fun observeData() {
        observeButtonBackClicked()
        observeButtonAddAddressClicked()
        observeButtonEditClicked()
    }

    private fun observeButtonBackClicked() {
        viewModel.buttonBackClicked.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                Navigation.findNavController(requireActivity(), com.iti.team.ecommerce.R.id.nav_host_fragment)
                    .popBackStack()
            }
        })
    }

    private fun observeButtonAddAddressClicked() {
        viewModel.buttonAddAddressClicked.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                val navigate = AddressFragmentDirections.actionAddressFragment2ToAddressFragment("",actioTypr = Constants.ADD)
                findNavController().navigate(navigate)
            }
        })
    }

    private fun observeButtonEditClicked() {
        viewModel.editButtonClicked.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                val navigate = AddressFragmentDirections.actionAddressFragment2ToAddressFragment(it,actioTypr = Constants.EDIT)
                findNavController().navigate(navigate)
            }
        })
    }


//    override fun onResume() {
//        super.onResume()
//        Log.i("onResume","onResume")
//        viewModel.getAddress()
//    }


}
