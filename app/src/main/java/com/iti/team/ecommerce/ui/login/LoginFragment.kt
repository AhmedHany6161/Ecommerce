package com.iti.team.ecommerce.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import bolts.AppLinkNavigation.navigate
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.databinding.FragmentLoginBinding

class LoginFragment: Fragment()  {

    private lateinit var viewModel: LoginViewModel
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
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        return binding.root
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
        val navController = findNavController()
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED ->navController.navigate(
                    R.id.categoriesFragment)

                else -> {
                    binding.googleBtn.setOnClickListener { launchSignInGoogle() }
                    binding.facebookBtn.setOnClickListener { launchSignInFacebook() }
                }
            }
        })
    }
}