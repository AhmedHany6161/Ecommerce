package com.iti.team.ecommerce.ui.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import com.iti.team.ecommerce.databinding.FragmentAddressBinding
import com.iti.team.ecommerce.model.data_classes.Address
import com.iti.team.ecommerce.model.data_classes.AddressModel

class AddressFragment:Fragment() {

    private lateinit var binding: FragmentAddressBinding
    private val viewModel by lazy {
        AddressViewModel(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressBinding.inflate(inflater)

        init()
        return binding.root
    }

    fun init(){
        //viewModel.getAddress(5276695265478,6480020340934)
        val address = AddressModel(Address("mahalla3","cairo","test","test","12348"))
        //viewModel.addAddress(5276695265478,address)
        //viewModel.updateAddress(5276695265478,6480300343494,address)
        viewModel.deleteAddress(5276695265478,6480300343494)
    }
}