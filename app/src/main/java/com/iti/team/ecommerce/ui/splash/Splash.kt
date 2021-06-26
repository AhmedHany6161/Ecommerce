package com.iti.team.ecommerce.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.databinding.FragmentSplashBinding

class Splash : Fragment() {
    private lateinit var binding:FragmentSplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentSplashBinding.inflate(inflater, container, false)
        setAnim()
        return binding.root
    }



    private fun setAnim() {
        binding.part1Txt.setAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.left_to_right
            )
        )
        binding.part2Txt.setAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.down_to_up
            )
        )
        binding.part3Txt.setAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.right_to_left
            )
        )
    }


}