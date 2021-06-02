package com.iti.team.ecommerce.ui.shop

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.iti.team.ecommerce.databinding.DiscountDialogFragmentBinding

class DiscountDialog(private val viewModel: ShopViewModel): DialogFragment() {


    private lateinit var binding: DiscountDialogFragmentBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(activity)
        binding = DiscountDialogFragmentBinding.inflate(inflater)

        binding.viewModel = viewModel

        val builder = AlertDialog.Builder(requireActivity())


        builder.setView(binding.root)

        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.buttonCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        return alertDialog
    }

    companion object{
        fun newInstance(viewModel: ShopViewModel) = DiscountDialog(viewModel)
    }
}