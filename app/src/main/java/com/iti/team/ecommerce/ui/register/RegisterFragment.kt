package com.iti.team.ecommerce.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.ui.main.MainViewModel

class RegisterFragment: Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    companion object {

    }
}