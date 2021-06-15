package com.iti.team.ecommerce.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.iti.team.ecommerce.databinding.FragmentRegisterBinding
import com.iti.team.ecommerce.model.data_classes.Customer
import com.iti.team.ecommerce.model.data_classes.CustomerModel


class RegisterFragment : Fragment() {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = RegisterViewModel(requireActivity().application)
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        observeSuccessRigister()
        observeShowError()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginTxt.setOnClickListener {
            navigate()
        }
        binding.cirRegisterButton.setOnClickListener {
            registerUser()
        }
        binding.backToLogin.setOnClickListener {
            navigate()
        }
    }

    private fun observeSuccessRigister() {
        viewModel.successRegister.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(context, "successfully registered ", Toast.LENGTH_LONG).show()
                navigate()
            }
        })
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

    private fun navigate() {
//        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.registerFragment,LoginFragment())
//            .addToBackStack(null)
//            .commit()
        val action = RegisterFragmentDirections.actionFromRegisterToLoginFragment()
        findNavController().navigate(action)
    }



    private fun registerUser() {
        if (validateFirstName() && validateLastName() && validatePassword()
            && validateConfirmPass() && validatePhoneNum() && validateEmail()
        ) {
            val customerModel = createCustomer()
            viewModel.createCustomer(customerModel)
        }
    }

    private fun validateFirstName(): Boolean {
        val firstName = binding.editTextFirstName.text.toString()
        return if (firstName.isEmpty()) {
            binding.textInputFirstName.error = "Field cannot be empty"
            false
        } else {
            binding.textInputFirstName.error = null
            binding.textInputFirstName.isErrorEnabled = false
            true
        }
    }

    private fun validateLastName(): Boolean {
        val lastName = binding.editTextLastName.text.toString()
        return if (lastName.isEmpty()) {
            binding.textInputLastName.error = "Field cannot be empty"
            false
        } else {
            binding.textInputLastName.error = null
            binding.textInputLastName.isErrorEnabled = false
            true
        }
    }

    private fun validateEmail(): Boolean {
        val email = binding.editTextEmail.text.toString()
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        return if (email.isEmpty()) {
            binding.textInputEmail.error = "Field cannot be empty"
            false
        } else if (!email.matches(emailPattern)) {
            binding.textInputEmail.error = "Invalid email address"
            false
        } else {
            binding.textInputEmail.error = null
            binding.textInputEmail.isErrorEnabled = false
            true
        }
    }

    private fun validatePassword(): Boolean {
        val pass = binding.editTextPassword.text.toString()
        val passwordVal = "^" +  //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                //   "(?=.*[a-zA-Z])" +  //any letter
                //  "(?=.*[@#$%^&+=])" +  //at least 1 special character
                "(?=\\S+$)" +  //no white spaces
                ".{4,}"  //at least 4 characters
//                "$"
        return if (pass.isEmpty()) {
            binding.textInputPassword.error = "Field cannot be empty"
            false
        } else if (!pass.matches(passwordVal.toRegex())) {
            binding.textInputPassword.error = "Password is too weak"
            false
        } else {
            binding.textInputPassword.error = null
            binding.textInputPassword.isErrorEnabled = false
            true
        }
    }

    private fun validatePhoneNum(): Boolean {
        val phone = binding.editTextMobile.text.toString()
        return if (phone.isEmpty()) {
            binding.textInputMobile.error = "Field cannot be empty"
            false
        } else {
            binding.textInputMobile.error = null
            binding.textInputMobile.isErrorEnabled = false
            true
        }
    }

    private fun validateConfirmPass(): Boolean {
        val confirmPass = binding.editTextConfirmPassword.text.toString()
        return if (confirmPass.isEmpty()) {
            binding.textInputConfirmPassword.error = "Field cannot be empty"
            false
        } else if (!confirmPass.equals(binding.editTextPassword.text.toString())) {
            binding.textInputConfirmPassword.error = "Password not matching  "
            false
        } else {
            binding.textInputConfirmPassword.error = null
            binding.textInputConfirmPassword.isErrorEnabled = false
            true
        }

    }

    private fun createCustomer(): CustomerModel {
        val customer = Customer(
            null,
            binding.editTextEmail.text.toString(),
            binding.editTextMobile.text.toString(),
            binding.editTextFirstName.text.toString(),
            binding.editTextLastName.text.toString(),
            0,
            null,
            "EGP",
            binding.editTextPassword.text.toString(),
            null,
            null,
            binding.editTextPassword.text.toString(),
            binding.editTextConfirmPassword.text.toString()
        )
        return CustomerModel(customer, null)

    }
}