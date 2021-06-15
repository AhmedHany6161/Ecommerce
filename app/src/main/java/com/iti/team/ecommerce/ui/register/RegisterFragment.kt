package com.iti.team.ecommerce.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
            Toast.makeText(context, "error:", Toast.LENGTH_LONG).show()
            it?.let {
                Toast.makeText(context, "error:", Toast.LENGTH_LONG).show()
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
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


    companion object {

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
        val firstName = binding.editTextFirstName.getText().toString()
        if (firstName.isEmpty()) {
            binding.textInputFirstName.setError("Field cannot be empty")
            return false
        } else {
            binding.textInputFirstName.setError(null)
            binding.textInputFirstName.setErrorEnabled(false)
            return true
        }
    }

    private fun validateLastName(): Boolean {
        val lastName = binding.editTextLastName.text.toString()
        if (lastName.isEmpty()) {
            binding.textInputLastName.setError("Field cannot be empty")
            return false
        } else {
            binding.textInputLastName.setError(null)
            binding.textInputLastName.setErrorEnabled(false)
            return true
        }
    }

    private fun validateEmail(): Boolean {
        val email = binding.editTextEmail.text.toString()
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        return if (email.isEmpty()) {
            binding.textInputEmail.setError("Field cannot be empty")
            false
        } else if (!email.matches(emailPattern)) {
            binding.textInputEmail.setError("Invalid email address")
            false
        } else {
            binding.textInputEmail.error = null
            binding.textInputEmail.setErrorEnabled(false)
            true
        }
    }

    private fun validatePassword(): Boolean {
        val pass = binding.editTextPassword.getText().toString()
        val passwordVal = "^" +  //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                //   "(?=.*[a-zA-Z])" +  //any letter
                //  "(?=.*[@#$%^&+=])" +  //at least 1 special character
                "(?=\\S+$)" +  //no white spaces
                ".{4,}"  //at least 4 characters
//                "$"
        return if (pass.isEmpty()) {
            binding.textInputPassword.setError("Field cannot be empty")
            false
        } else if (!pass.matches(passwordVal.toRegex())) {
            binding.textInputPassword.setError("Password is too weak")
            false
        } else {
            binding.textInputPassword.setError(null)
            binding.textInputPassword.setErrorEnabled(false)
            true
        }
    }

    private fun validatePhoneNum(): Boolean {
        val phone = binding.editTextMobile.text.toString()
        return if (phone.isEmpty()) {
            binding.textInputMobile.setError("Field cannot be empty")
            false
        } else {
            binding.textInputMobile.setError(null)
            binding.textInputMobile.setErrorEnabled(false)
            true
        }
    }

    private fun validateConfirmPass(): Boolean {
        val confirmPass = binding.editTextConfirmPassword.text.toString()
        return if (confirmPass.isEmpty()) {
            binding.textInputConfirmPassword.setError("Field cannot be empty")
            false
        } else if (!confirmPass.equals(binding.editTextPassword.text.toString())) {
            binding.textInputConfirmPassword.setError("Password not matching  ")
            false
        } else {
            binding.textInputConfirmPassword.setError(null)
            binding.textInputConfirmPassword.setErrorEnabled(false)
            true
        }

    }

    private fun createCustomer(): CustomerModel {

        val customer = Customer(
            null,
            binding.editTextEmail.getText().toString(),
            binding.editTextMobile.getText().toString(),
            binding.editTextFirstName.getText().toString(),
            binding.editTextLastName.getText().toString(),
            0,
            null,
            "EGP",
            binding.editTextPassword.getText().toString(),
            null,
            null,
            binding.editTextPassword.getText().toString(),
            binding.editTextConfirmPassword.getText().toString()
        )
        return CustomerModel(customer, null)

    }
}