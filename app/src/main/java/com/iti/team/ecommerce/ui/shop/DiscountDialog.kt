package com.iti.team.ecommerce.ui.shop

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.iti.team.ecommerce.databinding.DiscountDialogFragmentBinding

class DiscountDialog(private val discount:String,private val viewModel: ShopViewModel): DialogFragment() {


    private lateinit var binding: DiscountDialogFragmentBinding
    private lateinit var clipboard:ClipboardManager
    private lateinit var clip: ClipData
    private lateinit var alertDialog:AlertDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(activity)
        binding = DiscountDialogFragmentBinding.inflate(inflater)

        binding.viewModel = viewModel

        init()
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))



        return alertDialog
    }


    private fun init(){
        buttonClicked()
        observeData()

    }
    private fun buttonClicked(){
        binding.buttonCancel.setOnClickListener {
            alertDialog.dismiss()
        }
    }
    private fun observeData(){
        viewModel.copyAction.observe(this,{
            it.getContentIfNotHandled()?.let {
                clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE)
                        as ClipboardManager
                clip = ClipData.newPlainText("simple text",discount)
                clip.description
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context,"copy to clipboard",Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }
        })
    }

    companion object{
        fun newInstance(viewModel: ShopViewModel,discount:String) =
            DiscountDialog(discount,viewModel)
    }
}