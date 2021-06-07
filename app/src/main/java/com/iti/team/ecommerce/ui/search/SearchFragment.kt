package com.iti.team.ecommerce.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.iti.team.ecommerce.databinding.FragmentSearchBinding
import com.iti.team.ecommerce.ui.MainActivity

class SearchFragment: Fragment() {

    private lateinit var binding: FragmentSearchBinding

    val arg: SearchFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).bottomNavigation.isGone = true
    }
}