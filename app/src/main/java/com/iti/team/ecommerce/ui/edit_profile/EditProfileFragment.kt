package com.iti.team.ecommerce.ui.edit_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.iti.team.ecommerce.databinding.EditProfileFragmentBinding
import com.iti.team.ecommerce.model.data_classes.Customer
import com.iti.team.ecommerce.model.data_classes.CustomerModel
import com.iti.team.ecommerce.model.data_classes.EditCustomer
import com.iti.team.ecommerce.model.data_classes.EditCustomerModel
import com.iti.team.ecommerce.ui.login.LoginViewModel

class EditProfileFragment : Fragment() {

    private lateinit var viewModel: EditProfileViewModel
    private lateinit var binding: EditProfileFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = EditProfileViewModel(requireActivity().application)
        binding = EditProfileFragmentBinding.inflate(inflater, container, false)
        viewModel.getData()
        observeData()
        observeShowError()
        binding.updateBtn.setOnClickListener {
            viewModel.customer_ID.observe(viewLifecycleOwner, {
                it?.let {
                    if (checkEmpty()) {
                        viewModel.updateCustomer(it, createCustomer())
                        //navigate
                        findNavController().popBackStack()

                    }
                }
            })
        }

        observeData()

        return binding.root
    }
    private fun observeShowError() {
        viewModel.setError.observe(viewLifecycleOwner, {
            it?.let {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_INDEFINITE)
                    .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                    .setAction("Ok") {
                    }.show()
            }
        })
    }

    private fun checkEmpty(): Boolean {
        if (binding.firstName.text.toString().isEmpty()) {
            binding.firstName.setError("Field can’t be empty")
            return false
        } else if (binding.lastName.text.toString().isEmpty()) {
            binding.lastName.setError("Field can’t be empty")
            return false
        } else if (binding.email.text.toString().isEmpty()) {
            binding.email.setError("Field can’t be empty")
            return false
        } else if (binding.phone.text.toString().isEmpty()) {
            binding.phone.setError("Field can’t be empty")
            return false
        } else {
            return true
        }

    }

    private fun observeData() {
        observeFirstName()
        observeLastName()
        observeFullName()
        observeEmail()
        observePhone()
    }

    private fun observePhone() {
        viewModel.phone_number.observe(viewLifecycleOwner, {
            binding.phone.setText(viewModel.phone_number.value)
        })
    }

    private fun observeEmail() {
        viewModel.email.observe(viewLifecycleOwner, {
            binding.email.setText(viewModel.email.value)
            binding.txtEmail.setText(viewModel.email.value)
        })
    }

    private fun observeFullName() {
        viewModel.full_name.observe(viewLifecycleOwner, {
            binding.fullName.setText(viewModel.full_name.value)
        })
    }

    private fun observeLastName() {
        viewModel.last_name.observe(viewLifecycleOwner, {
            binding.lastName.setText(viewModel.last_name.value)

        })
    }

    private fun observeFirstName() {
        viewModel.first_name.observe(viewLifecycleOwner, {
            binding.firstName.setText(viewModel.first_name.value)
        })
    }

    private fun createCustomer(): EditCustomerModel {

        val customer: EditCustomer = EditCustomer(
            viewModel.customer_ID.value,
            binding.email.getText().toString(),
            binding.phone.getText().toString(),
            binding.firstName.getText().toString(),
            binding.lastName.getText().toString()
        )
        val customerModel: EditCustomerModel = EditCustomerModel(customer, null)
        return customerModel
    }
}