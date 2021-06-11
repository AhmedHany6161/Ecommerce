package com.iti.team.ecommerce.ui.payment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.iti.team.ecommerce.BuildConfig
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.databinding.FragmentPaymentPaytabsBinding
import com.payment.paymentsdk.PaymentSdkActivity.Companion.startCardPayment
import com.payment.paymentsdk.PaymentSdkConfigBuilder
import com.payment.paymentsdk.integrationmodels.*
import com.payment.paymentsdk.sharedclasses.interfaces.CallbackPaymentInterface

class PayTabFragment: Fragment(), CallbackPaymentInterface {

    private lateinit var binding: FragmentPaymentPaytabsBinding

    private var token: String? = null
    private var transRef: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentPaytabsBinding.inflate(inflater)
        init()
        return binding.root
    }
    private fun init(){
        binding.version.text = BuildConfig.VERSION_NAME
        binding.amount.setText("1.0")
        binding.currency.setSelection(7)
        binding.language.setSelection(1)
        binding.tokeniseType.setSelection(1)
        binding.mid.setText("123")
        binding.serverKey.setText("##########")
        binding.clientKey.setText("##########")
        binding.street.setText("address street")
        binding.city.setText("Dubai")
        binding.state.setText("3510")
        binding.country.setText("EGP")
        binding.postalCode.setText("54321")
        binding.shippingEmail.setText("email1@domain.com")
        binding.shippingName.setText("name1 last1")
        binding.shippingPhone.setText("1234")
        binding.streetBilling.setText("street2")
        binding.cityBilling.setText("Dubai")
        binding.stateBilling.setText("3510")
        binding.countryBilling.setText("AE")
        binding.postalCodeBilling.setText("12345")
        binding.billingEmail.setText("email1@domain.com")
        binding.billingName.setText("first last")
        binding.billingPhone.setText("45")
      payButtonClicked()

    }

    private fun payButtonClicked(){
      binding.pay.setOnClickListener {
          val configData = generatePayTabsConfigurationDetails()
          startCardPayment(requireActivity(), configData, this)

      }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun generatePayTabsConfigurationDetails(): PaymentSdkConfigurationDetails {
        val locale = getLocale()
        val transactionTitle = binding.transactionTitle.text.toString()
        val orderId = binding.cartId.text.toString()
        val billingData = PaymentSdkBillingDetails(
            "Mahalla", "EG",
            binding.billingEmail.text.toString(), binding.billingName.text.toString(),
            binding.billingPhone.text.toString(), binding.stateBilling.text.toString(),
            binding.streetBilling.text.toString(), "31951"
        )
        val shippingData = PaymentSdkShippingDetails(
            "Mahalla", "EG",
            binding.shippingEmail.text.toString(), binding.shippingName.text.toString(),
            binding.shippingPhone.text.toString(), binding.state.text.toString(),
            binding.street.text.toString(), "31951"
        )
        val configData = PaymentSdkConfigBuilder("70414", 	"S9JNTDT2HB-JBRGLTGWTH-9ZKWJWWWBK",
            "C6KMKQ-6DQR62-RTB7TV-KQ2PMG", 3.1, "EGP")
            .setCartId("4244b9fd-c7e9-4f16-8d3c-4fe7bf6c48ca")
            .setCartDescription("Dummy Order 35925502061445345")
            .setLanguageCode(locale)
            .setMerchantCountryCode("EG")
            .setBillingData(billingData)
            .setShippingData(shippingData)
            .setTransactionType(if (binding.transactionType.selectedItemPosition <2) PaymentSdkTransactionType.SALE else PaymentSdkTransactionType.AUTH)
            .setTransactionClass(PaymentSdkTransactionClass.ECOM)
            .setCartId(orderId)
            .setAlternativePaymentMethods(getSelectedApms())
            .setTokenise(getTokeniseType(),PaymentSdkTokenFormat.AlphaNum20Format())
            .setTokenisationData(token, transRef)
            .showBillingInfo(binding.completeBillingInfo.isChecked)
            .showShippingInfo(binding.completeShippingInfo.isChecked)
            .forceShippingInfo(binding.forceShippingValidation.isChecked)
            .setScreenTitle(transactionTitle)
        if (binding.showMerchantLogo.isChecked) {
            configData.setMerchantIcon(resources.getDrawable(R.drawable.payment_sdk_adcb_logo,context?.theme))
        }


        return configData.build()
    }
    private fun getSelectedApms(): List<PaymentSdkApms> {
        val apms = mutableListOf<PaymentSdkApms>()
        addApmToList(apms, PaymentSdkApms.STC_PAY, binding.apmStcPay.isChecked)
        addApmToList(apms, PaymentSdkApms.UNION_PAY, binding.apmUnionpay.isChecked)
        addApmToList(apms, PaymentSdkApms.VALU, binding.apmValu.isChecked)
        addApmToList(apms, PaymentSdkApms.KNET_DEBIT, binding.apmKnetDebit.isChecked)
        addApmToList(apms, PaymentSdkApms.KNET_CREDIT, binding.apmKnet.isChecked)
        addApmToList(apms, PaymentSdkApms.FAWRY, binding.apmFawry.isChecked)
        addApmToList(apms, PaymentSdkApms.OMAN_NET, binding.apmOmannet.isChecked)
        addApmToList(apms, PaymentSdkApms.MEEZA_QR, binding.apmMeezaQr.isChecked)
        return apms
    }

    private fun addApmToList(
        list: MutableList<PaymentSdkApms>,
        apm: PaymentSdkApms,
        checked: Boolean
    ) {
        if (checked)
            list.add(apm)
    }
    private fun getTokeniseType(): PaymentSdkTokenise {
        return when (binding.tokeniseType.selectedItemPosition) {
            1 -> PaymentSdkTokenise.NONE
            2 -> PaymentSdkTokenise.MERCHANT_MANDATORY
            3 -> PaymentSdkTokenise.USER_MANDATORY
            4 -> PaymentSdkTokenise.USER_OPTIONAL
            else -> PaymentSdkTokenise.NONE
        }
    }
    private fun getLocale() =
        when (binding.language.selectedItemPosition) {
            1 -> PaymentSdkLanguageCode.EN
            2 -> PaymentSdkLanguageCode.AR
            else -> PaymentSdkLanguageCode.DEFAULT
        }
    override fun onError(error: PaymentSdkError) {
        Log.e("onError","${error.msg}")
        Toast.makeText(context, "error ${error.msg}", Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentCancel() {
        Toast.makeText(context, "onPaymentCancel", Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentFinish(paymentSdkTransactionDetails: PaymentSdkTransactionDetails) {
        token = paymentSdkTransactionDetails.token
        transRef = paymentSdkTransactionDetails.transactionReference
        Log.i("eeee", "onPaymentFinish: $paymentSdkTransactionDetails")
        Toast.makeText(
            context,
            paymentSdkTransactionDetails.paymentResult?.responseMessage ?: "PaymentFinish",
            Toast.LENGTH_SHORT
        ).show()
        Log.d("onPaymentFinish", "onPaymentFinish: $paymentSdkTransactionDetails")
    }

}