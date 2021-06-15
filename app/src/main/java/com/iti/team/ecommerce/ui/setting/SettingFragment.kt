package com.iti.team.ecommerce.ui.setting

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.databinding.FragmentRegisterBinding
import com.iti.team.ecommerce.databinding.SettingFragmentBinding

class SettingFragment : Fragment() {

    private lateinit var viewModel: SettingViewModel
    private lateinit var binding: SettingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        binding = SettingFragmentBinding.inflate(inflater, container, false)
        initUI()
        return binding.root
    }

    private fun initUI() {
        binding.addressTxt.setOnClickListener {
          navigateToAddrees()
        }
        binding.editProfileTxt.setOnClickListener {
            navigateToEditProfile()
        }
        binding.logoutTxt.setOnClickListener {
            logout()
        }
        binding.ratingTxt.setOnClickListener {
            navigateToPlayStore()
        }
        binding.aboutTxt.setOnClickListener {
            //viewModel.showDialoge()
        }
    }

    private fun navigateToPlayStore() {
        try {
          //  startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
            var playstoreuri:Uri=Uri.parse("https://play.google.com/store/apps/details?id=manigautam.app.myplaystoreratingapp")
            startActivity(Intent(Intent.ACTION_VIEW,playstoreuri))
        } catch (e: ActivityNotFoundException) {
          //  startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
        }
    }

    private fun logout() {
        TODO("Not yet implemented")
    }

    private fun navigateToEditProfile() {
        TODO("Not yet implemented")
    }

    private fun navigateToAddrees() {
        TODO("Not yet implemented")
    }

    companion object {

    }

}