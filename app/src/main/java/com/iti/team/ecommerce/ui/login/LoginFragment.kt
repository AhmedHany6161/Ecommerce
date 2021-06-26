package com.iti.team.ecommerce.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import bolts.AppLinkNavigation.navigate
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.databinding.FragmentLoginBinding
import com.iti.team.ecommerce.ui.MainActivity

class LoginFragment: Fragment()  {

    private  val viewModel: LoginViewModel by viewModels()
    private lateinit var binding : FragmentLoginBinding

    private companion object {
        private const val SIGN_IN_REQUEST_CODE =100
        private const val TAG ="GOOGLE_SIGN_IN_TAG"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        init()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).bottomNavigation.isGone = true
    }
    fun init(){
        binding.cirLoginButton.setOnClickListener {
            validateEmail()
        }
        binding.goToRegister.setOnClickListener {
            animationToRegister()
        }
        binding.registerNow.setOnClickListener {
            animationToRegister()
        }

    }

    private fun validateEmail() {

        val email: String = binding.editTextEmail.getText().toString()
        viewModel.emailResult(email)
        observeEmptyEmail(email)
    }
    private fun observeEmptyEmail(email :String){
        viewModel.isEmailEmpty().observe(viewLifecycleOwner,{
            when(it){
                false ->{
                    binding.textInputEmail.setError("Field can’t be empty")
                }
                true ->{
                    viewModel.login(email)

                    binding.progress.visibility = VISIBLE
                    binding.textInputEmail.setError(null)
                    binding.textInputEmail.setErrorEnabled(false)
                    observeValidation()
                }
            }
        })
    }

    private fun observeValidation() {
        viewModel.isValidEmail().observe(viewLifecycleOwner,{
            when(it){
                false ->{ binding.textInputEmail.setError("Invalid email address")

                    binding.progress.visibility = GONE}
                true ->{
                    binding.textInputEmail.setError(null)
                    binding.textInputEmail.setErrorEnabled(false)
                    validaePassword()
                }
            }

        })
    }

    private fun validaePassword() {
        val pass = binding.editTextPassword.getText().toString()
        viewModel.passwordResult(pass)
        viewModel.isPassEmpty().observe(viewLifecycleOwner,{
            when(it) {
                false -> {
                    binding.progress.visibility = GONE
                    binding.textInputPassword.setError("Field can’t be empty")
                }
                true -> {
                    if (! pass.equals(viewModel.password)) {
                        binding.progress.visibility = GONE
                        binding.textInputPassword.setError("Wrong Password")
                    } else {

                        binding.textInputPassword.setError(null)
                        binding.textInputPassword.setErrorEnabled(false)
                        navigation()
                    }

                }
            }
        })
    }


    private fun animationToRegister() {
        val action = LoginFragmentDirections.actionFromLoginFragmentToRegisterFragment()
        findNavController().navigate(action)

    }

    private fun navigation() {
        binding.progress.visibility = GONE
        findNavController().popBackStack()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuthenticationState()
        binding.googleBtn.setOnClickListener { launchSignInGoogle() }
        binding.facebookBtn.setOnClickListener { launchSignInFacebook() }
    }

    private fun launchSignInGoogle() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build()))
                .build(),
            LoginFragment.SIGN_IN_REQUEST_CODE
        )
    }
    private fun launchSignInFacebook() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(arrayListOf(AuthUI.IdpConfig.FacebookBuilder().build()))
                .build(),
            LoginFragment.SIGN_IN_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // User successfully signed in
                Log.i(TAG, "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }

    }

    private fun observeAuthenticationState() {
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> findNavController().popBackStack()

                else -> {
                    binding.googleBtn.setOnClickListener { launchSignInGoogle() }
                    binding.facebookBtn.setOnClickListener { launchSignInFacebook() }
                }
            }
        })
    }
}