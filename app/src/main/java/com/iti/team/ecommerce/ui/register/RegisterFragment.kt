package com.iti.team.ecommerce.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.databinding.FragmentRegisterBinding
import com.iti.team.ecommerce.model.data_classes.Customer
import com.iti.team.ecommerce.model.data_classes.CustomerModel


class RegisterFragment: Fragment() {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding:FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
     val RC_SIGN_IN=123


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        auth = Firebase.auth
        googleSignInClient = GoogleSignIn.getClient(requireContext(), getGSO())

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                //handle error
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    //handle success
                } else {
                    //handle error
                }
            }
    }

    private fun getGSO(): GoogleSignInOptions {
        return  GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = RegisterViewModel()
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.googleSingInBtn.setOnClickListener(View.OnClickListener {
            googleSignIn()
        })
        binding.cirRegisterButton.setOnClickListener(View.OnClickListener {
            registerUser()
        })
    }

    companion object {

    }

    private fun googleSignIn() {
        try{
        val signInIntent: Intent = googleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
        }catch (e:Exception ){
        e.printStackTrace();
        }
    }
   private fun registerUser() {
        if ( validateFirstName() && validateLastName() && validatePassword()
            && validateConfirmPass()&& validatePhoneNo() && validateEmail() ) {
            val customerModel= createCustomer()
            viewModel.createCustomer(customerModel)
            Toast.makeText(context, "registerd", Toast.LENGTH_LONG).show()

        }
        else{
            Toast.makeText(context, "not registerd", Toast.LENGTH_LONG).show()

        }
    }
    private fun validateFirstName(): Boolean {
        val firstName: String = binding.editTextFirstName.getText().toString()
        return if (firstName.isEmpty()) {
            binding.textInputFirstName.setError("Field cannot be empty")
          return false
        } else {
            binding.textInputFirstName.setError(null)
            binding.textInputFirstName.setErrorEnabled(false)
           return true
        }
    }
    private fun validateLastName(): Boolean {
        val lastName: String = binding.editTextLastName.getText().toString()
        return if (lastName.isEmpty()) {
            binding.textInputLastName.setError("Field cannot be empty")
            return false
        } else {
            binding.textInputLastName.setError(null)
            binding.textInputLastName.setErrorEnabled(false)
            return true
        }
    }
    private fun validateEmail(): Boolean {
        val email: String = binding.editTextEmail.getText().toString()
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        return if (email.isEmpty()) {
            binding.textInputEmail.setError("Field cannot be empty")
            false
        } else if (!email.matches(emailPattern)) {
            binding.textInputEmail.setError("Invalid email address")
            false
        } else {
            binding.textInputEmail.setError(null) 
            binding.textInputEmail.setErrorEnabled(false)
            true
        }
    }
    private fun validatePassword(): Boolean {
        val pass: String = binding.editTextPassword.getText().toString()
        val passwordVal = "^" +  //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +  //any letter
                "(?=.*[@#$%^&+=])" +  //at least 1 special character
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
    private fun validatePhoneNo(): Boolean {
        val phone: String = binding.editTextMobile.getText().toString()
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
        val confirmPass: String = binding.editTextConfirmPassword.getText().toString()
        return if (confirmPass.isEmpty()) {
            binding.textInputConfirmPassword.setError("Field cannot be empty")
            false
        }
        else if (!confirmPass.equals(binding.editTextPassword.getText().toString())) {
            binding.textInputConfirmPassword.setError("Password not matching  ")
            false
        }
        else {
            binding.textInputConfirmPassword.setError(null)
            binding.textInputConfirmPassword.setErrorEnabled(false)
            true
        }

    }
    private fun createCustomer(): CustomerModel{

        val customer:Customer= Customer(null,binding.editTextEmail.getText().toString(),binding.editTextMobile.getText().toString(),
            binding.editTextFirstName.getText().toString() , binding.editTextLastName.getText().toString(),0,null,
             "EGP",null,null,binding.editTextPassword.getText().toString(),binding.editTextConfirmPassword.getText().toString())
        val customerModel:CustomerModel=CustomerModel(customer,null)
        return customerModel
    }
}