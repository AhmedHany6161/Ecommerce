package com.iti.team.ecommerce.ui.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import com.iti.team.ecommerce.R
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
        //checkEmptyFields()
        observeData()
    }

    private fun checkEmptyFields(){
        viewModel.checkEmptyFields(binding.editTextFirstName.text.toString(),
            binding.editTextLastName.text.toString(),binding.editTextCity.text.toString(),
            binding.editTextAddress.text.toString(),binding.editTextZip.text.toString(),"")
    }

    private fun observeData(){
        observeFirstName()
        observeLastName()
        observeCity()
        observeAddress()
        observeZip()
        buttonAddressClicked()
    }

    private fun buttonAddressClicked(){
        binding.addressButton.setOnClickListener {
            checkEmptyFields()
        }
    }

    private fun observeFirstName(){
        viewModel.firstName.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let {
                binding.editTextFirstName.error = "first name can not be empty."
            }
        })
    }
    private fun observeLastName(){
        viewModel.lastName.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let {
                binding.editTextLastName.error = "last name can not be empty."

            }
        })
    }
    private fun observeCity(){
        viewModel.city.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let {
                binding.editTextCity.error = "city can not be empty."
            }
        })
    }
    private fun observeAddress(){
        viewModel.address.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let {
                binding.editTextAddress.error = "address can not be empty."
            }
        })
    }
    private fun observeZip(){
        viewModel.zip.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let {
                binding.editTextZip.error = "zip code can not be empty."
            }
        })
    }
}