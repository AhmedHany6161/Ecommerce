package com.iti.team.ecommerce.ui.payment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import com.iti.team.ecommerce.databinding.FragmentCheckoutBinding
import com.iti.team.ecommerce.databinding.FragmentShopProductsBinding
import com.iti.team.ecommerce.ui.shop_products.ShopProductsArgs
import com.iti.team.ecommerce.ui.shop_products.ShopProductsViewModel
import com.iti.team.ecommerce.utils.Json
import com.iti.team.ecommerce.utils.PaymentsUtil
import com.iti.team.ecommerce.utils.microsToString
import kotlinx.android.synthetic.main.fragment_checkout.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import kotlin.math.roundToLong

class Payment: Fragment() {
    private lateinit var binding:FragmentCheckoutBinding
    private lateinit var paymentsClient: PaymentsClient
    //private val shippingCost = (90 * 1000000).toLong()
    private val args:PaymentArgs by navArgs()

    private lateinit var garmentList: JSONArray
    private lateinit var selectedGarment: JSONObject


    private val viewModel by lazy {
        PaymentViewModel(requireActivity().application)
    }

    /**
     * Arbitrarily-picked constant integer you define to track a request for payment data activity.
     *
     * @value #LOAD_PAYMENT_DATA_REQUEST_CODE
     */
    private val LOAD_PAYMENT_DATA_REQUEST_CODE = 991
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCheckoutBinding.inflate(inflater)
        // Set up the mock information for our item in the UI.
        selectedGarment = fetchRandomGarment()
        displayGarment(selectedGarment)
        Log.i("args",args.totalPrice)

        // Initialize a Google Pay API client for an environment suitable for testing.
        // It's recommended to create the PaymentsClient object inside of the onCreate method.
        paymentsClient = PaymentsUtil.createPaymentsClient(requireActivity())
        possiblyShowGooglePayButton()

        binding.googlePayButton.layout.setOnClickListener {
            Log.i("googlePayButton","clicked")
            requestPayment()
        }

        init()

        return binding.root
    }

    private fun init(){
        binding.viewModel = viewModel

        observeData()
    }

    private fun observeData(){
        observeButtonBackClicked()
    }

    private fun observeButtonBackClicked(){
        viewModel.buttonBackClicked.observe(viewLifecycleOwner,{
            it.getContentIfNotHandled()?.let {
                Navigation.findNavController(requireActivity(), com.iti.team.ecommerce.R.id.nav_host_fragment)
                    .popBackStack()
            }
        })
    }
    /**
     * Determine the viewer's ability to pay with a payment method supported by your app and display a
     * Google Pay payment button.
     *
     * @see [](https://developers.google.com/android/reference/com/google/android/gms/wallet/PaymentsClient.html.isReadyToPay
    ) */
    private fun possiblyShowGooglePayButton() {

        val isReadyToPayJson = PaymentsUtil.isReadyToPayRequest() ?: return
        val request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString()) ?: return

        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        val task = paymentsClient.isReadyToPay(request)
        task.addOnCompleteListener { completedTask ->
            try {
                completedTask.getResult(ApiException::class.java)?.let(::setGooglePayAvailable)
                Log.i("googlePayButton","addOnCompleteListener")
            } catch (exception: ApiException) {
                // Process error
                Log.w("isReadyToPay failed", exception)
            }
        }
    }
    /**
     * If isReadyToPay returned `true`, show the button and hide the "checking" text. Otherwise,
     * notify the user that Google Pay is not available. Please adjust to fit in with your current
     * user flow. You are not required to explicitly let the user know if isReadyToPay returns `false`.
     *
     * @param available isReadyToPay API response.
     */
    private fun setGooglePayAvailable(available: Boolean) {
        if (available) {
            googlePayButton.visibility = View.VISIBLE
        } else {
            Toast.makeText(
                context,
                "Unfortunately, Google Pay is not available on this device",
                Toast.LENGTH_LONG).show();
        }
    }

    private fun requestPayment() {

        // Disables the button to prevent multiple clicks.
        googlePayButton.isClickable = false

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
//        val garmentPriceMicros = (selectedGarment.getDouble("price") * 1000000).roundToLong()
//        val price = (garmentPriceMicros + shippingCost).microsToString()
//        Log.i("price",price)
//        Log.i("price",shippingCost.toString())

        val price = (1.0).toString()
        val paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(price)
        if (paymentDataRequestJson == null) {
            Log.e("RequestPayment", "Can't fetch payment data request")
            return
        }
        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString())

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        if (request != null) {
            AutoResolveHelper.resolveTask(
                paymentsClient.loadPaymentData(request), requireActivity(), LOAD_PAYMENT_DATA_REQUEST_CODE)
            googlePayButton.isClickable = true
        }
    }


    /**
     * Handle a resolved activity from the Google Pay payment sheet.
     *
     * @param requestCode Request code originally supplied to AutoResolveHelper in requestPayment().
     * @param resultCode Result code returned by the Google Pay API.
     * @param data Intent from the Google Pay API containing payment or error data.
     * @see [Getting a result
     * from an Activity](https://developer.android.com/training/basics/intents/result)
     */
     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            // value passed in AutoResolveHelper
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK ->{
                        data?.let { intent ->
                            PaymentData.getFromIntent(intent)?.let(::handlePaymentSuccess)
                            Log.i("googlePayButton","RESULT_OK")
                        }
                        Log.i("onActivityResult","success")
                    }

                    Activity.RESULT_CANCELED -> {
                        // Nothing to do here normally - the user simply cancelled without selecting a
                        // payment method.
                        Log.i("googlePayButton","RESULT_CANCELED")
                    }

                    AutoResolveHelper.RESULT_ERROR -> {
                        AutoResolveHelper.getStatusFromIntent(data)?.let {
                            handleError(it.statusCode)
                        }
                        Log.i("googlePayButton","RESULT_ERROR")
                    }
                }
                // Re-enables the Google Pay payment button.
                googlePayButton.isClickable = true
            }

        }
        googlePayButton.isClickable = true
    }
    /**
     * PaymentData response object contains the payment information, as well as any additional
     * requested information, such as billing and shipping address.
     *
     * @param paymentData A response object returned by Google after a payer approves payment.
     * @see [Payment
     * Data](https://developers.google.com/pay/api/android/reference/object.PaymentData)
     */
    private fun handlePaymentSuccess(paymentData: PaymentData) {
        val paymentInformation = paymentData.toJson() ?: return

        try {
            // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
            val paymentMethodData = JSONObject(paymentInformation).getJSONObject("paymentMethodData")

            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".
            if (paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("type") == "PAYMENT_GATEWAY" && paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("token") == "examplePaymentMethodToken") {

                AlertDialog.Builder(requireActivity())
                    .setTitle("Warning")
                    .setMessage("Gateway name set to \"example\" - please modify " +
                            "Constants.java and replace it with your own gateway.")
                    .setPositiveButton("OK", null)
                    .create()
                    .show()
            }

            val billingName = paymentMethodData.getJSONObject("info")
                .getJSONObject("billingAddress").getString("name")
            Log.d("BillingName", billingName)

            Toast.makeText(context, getString(R.string.wallet_buy_button_place_holder, billingName), Toast.LENGTH_LONG).show()

            // Logging token string.
            Log.d("GooglePaymentToken", paymentMethodData
                .getJSONObject("tokenizationData")
                .getString("token"))

        } catch (e: JSONException) {
            Log.e("handlePaymentSuccess", "Error: " + e.toString())
        }

    }

    /**
     * At this stage, the user has already seen a popup informing them an error occurred. Normally,
     * only logging is required.
     *
     * @param statusCode will hold the value of any constant from CommonStatusCode or one of the
     * WalletConstants.ERROR_CODE_* constants.
     * @see [
     * Wallet Constants Library](https://developers.google.com/android/reference/com/google/android/gms/wallet/WalletConstants.constant-summary)
     */
    private fun handleError(statusCode: Int) {
        Log.w("loadPaymentData failed", String.format("Error code: %d", statusCode))
    }

    private fun fetchRandomGarment() : JSONObject {
        if (!::garmentList.isInitialized) {
            garmentList = Json.readFromResources(requireActivity(), com.iti.team.ecommerce.R.raw.row)
        }

        val randomIndex:Int = Math.round(Math.random() * (garmentList.length() - 1)).toInt()
        return garmentList.getJSONObject(randomIndex)
    }

    private fun displayGarment(garment:JSONObject) {

    }
}