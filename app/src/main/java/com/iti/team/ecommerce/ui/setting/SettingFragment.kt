package com.iti.team.ecommerce.ui.setting

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.databinding.SettingFragmentBinding
import com.iti.team.ecommerce.model.local.room.OfflineDatabase
import com.iti.team.ecommerce.model.reposatory.ModelRepository

class SettingFragment : Fragment() {

    private val viewModel: SettingViewModel by viewModels()
    private lateinit var binding: SettingFragmentBinding
    private lateinit var modelRepository: ModelRepository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingFragmentBinding.inflate(inflater, container, false)
        initUI()
        return binding.root
    }

    private fun init(){
        if (modelRepository.isLogin()){
            binding.logoutTxt.visibility = View.VISIBLE
        }else{
            binding.logoutTxt.visibility = View.GONE
        }
    }

    private fun initUI() {
       modelRepository = ModelRepository(OfflineDatabase.getInstance(requireActivity().application),
            requireActivity().application)

        init()

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
            navigateToAbout()
        }
    }

    private fun navigateToAbout() {
            findNavController().navigate(R.id.aboutFragment)
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
        if (modelRepository.isLogin()){
            viewModel.logout()
        }else{
            findNavController().popBackStack()
            findNavController().navigate(R.id.profileFragment)
        }

    }

    private fun navigateToEditProfile() {
        if (modelRepository.isLogin()){
            findNavController().navigate(R.id.editProfileFragment)
        }else{
            findNavController().navigate(R.id.loginFragment)
        }

    }

    private fun navigateToAddrees() {
        if (modelRepository.isLogin()){
            findNavController().navigate(R.id.addressFragment2)
        }else{
            findNavController().navigate(R.id.loginFragment)
        }
    }

    companion object {

    }

}