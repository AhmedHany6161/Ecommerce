package com.iti.team.ecommerce.ui.setting

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iti.team.ecommerce.R

class SettingFragment : Fragment() {

    private lateinit var viewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        return inflater.inflate(R.layout.setting_fragment, container, false)
    }

    companion object {

    }

}