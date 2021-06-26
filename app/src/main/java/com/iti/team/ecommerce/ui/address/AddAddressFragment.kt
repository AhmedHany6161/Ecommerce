package com.iti.team.ecommerce.ui.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.iti.team.ecommerce.databinding.FragmentAddressBinding
import com.iti.team.ecommerce.model.data_classes.Address
import com.iti.team.ecommerce.utils.Constants

class AddAddressFragment:Fragment() {

    private lateinit var binding: FragmentAddressBinding
    private val viewModel by lazy {
        AddAddressViewModel(requireActivity().application)
    }
    private val arg:AddAddressFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressBinding.inflate(inflater)

        init()
        return binding.root
    }

    fun init(){
        binding.viewModel = viewModel
        observeData()
        viewModel.setAddress(arg.addressObjetString)
    }

    private fun checkEmptyFields(){
        viewModel.checkEmptyFields(binding.editTextFirstName.text.toString(),
            binding.editTextLastName.text.toString(),binding.editTextCity.text.toString(),
            binding.editTextAddress.text.toString(),binding.editTextZip.text.toString(),arg.actioTypr)
    }

    private fun observeData(){
        observeFirstName()
        observeLastName()
        observeCity()
        observeAddress()
        observeZip()
        buttonAddressClicked()
        observeAddressData()
        observeButtonBackClicked()
        observeButtonAddAddressClicked()
        observeError()
    }

    private fun buttonAddressClicked(){

        binding.addressButton.setOnClickListener {
//            binding.loading.visibility = View.VISIBLE
//            binding.addressButton.visibility = View.GONE
            checkEmptyFields()
        }
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
        viewModel.finishLoading.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(context,"Address Added successful.",Toast.LENGTH_SHORT).show()
                Navigation.findNavController(requireActivity(), com.iti.team.ecommerce.R.id.nav_host_fragment)
                    .popBackStack()
            }
        })
    }
    private fun observeError() {
        viewModel.error.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(context,"error happen please try again.",Toast.LENGTH_SHORT).show()
                binding.loading.visibility = View.GONE
                binding.addressButton.visibility = View.VISIBLE

            }
        })
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
    private fun observeAddressData(){
        viewModel.addressObject.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let {
                updateUi(it)
            }
        })
    }

    private fun updateUi(address: Address){
        binding.editTextAddress.setText( address.address)
        binding.editTextCity.setText( address.city)
        binding.editTextFirstName.setText( address.firstName)
        binding.editTextLastName.setText( address.lastName)
        binding.editTextZip.setText( address.zip)

    }
}