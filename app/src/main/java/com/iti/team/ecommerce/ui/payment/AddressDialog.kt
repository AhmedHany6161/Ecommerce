package com.iti.team.ecommerce.ui.payment

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.iti.team.ecommerce.databinding.AddAddressDialogBinding
import com.iti.team.ecommerce.databinding.DiscountDialogFragmentBinding
import com.iti.team.ecommerce.model.data_classes.Product
import com.iti.team.ecommerce.ui.shop.ShopViewModel
import com.iti.team.ecommerce.ui.shop_products.ShopProductsViewModel

class AddressDialog (private val orderWithDiscount:Boolean, private val productList:List<Product> ,
private val financialStatus:String): DialogFragment() {


    private lateinit var binding: AddAddressDialogBinding
    private lateinit var alertDialog: AlertDialog

    private val viewModel by lazy {
        AddressDialogViewModel(requireActivity().application)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(activity)
        binding = AddAddressDialogBinding.inflate(inflater)


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
        binding.buttonAdd.setOnClickListener{
            if (binding.textAddress.text.isNullOrBlank()){
                Toast.makeText(context,"Address field is required",Toast.LENGTH_SHORT).show()
            }else{
                viewModel.addOrder(orderWithDiscount,productList,
                    "authorized",binding.textAddress.text.toString())
            }

        }
    }
    private fun observeData(){
        observeAddressAdded()
    }

    private fun observeAddressAdded(){
        viewModel.addressAdded.observe(this,{
            it.getContentIfNotHandled()?.let {
                Toast.makeText(context,"Address Added Successfully",Toast.LENGTH_SHORT).show()
                dismiss()
            }
        })
    }

    companion object{
        fun newInstance(orderWithDiscount:Boolean, productList:List<Product> ,
                        financialStatus:String) =
            AddressDialog(orderWithDiscount,productList,financialStatus)
    }
}