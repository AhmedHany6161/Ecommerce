package com.iti.team.ecommerce.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.databinding.FragmentAboutBinding
import com.iti.team.ecommerce.databinding.SettingFragmentBinding

class AboutFragment : Fragment()  {

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

   private fun init(){
        binding.backImage.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .popBackStack()
        }
    }
}