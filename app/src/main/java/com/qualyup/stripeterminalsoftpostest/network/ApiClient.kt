package com.qualyup.stripeterminalsoftpostest.network

import com.qualyup.stripeterminalsoftpostest.BuildConfig
import com.qualyup.stripeterminalsoftpostest.model.PaymentIntentCreationResponse
import com.stripe.stripeterminal.external.models.ConnectionTokenException
import okhttp3.OkHttpClient
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

/**
 * The `ApiClient` is a singleton object used to make calls to our backend and return their results
 */
object ApiClient {

    private val client = OkHttpClient.Builder()
        .build()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.EXAMPLE_BACKEND_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service: BackendService = retrofit.create(BackendService::class.java)

    @Throws(ConnectionTokenException::class)
    internal fun createConnectionToken(): String {
        try {
            val result = service.getConnectionToken().execute()
            if (result.isSuccessful && result.body() != null) {
                return result.body()!!.secret
            } else {
                throw ConnectionTokenException("Creating connection token failed")
            }
        } catch (e: IOException) {
            throw ConnectionTokenException("Creating connection token failed", e)
        }
    }

    internal fun createLocation(
        displayName: String?,
        city: String?,
        country: String?,
        line1: String?,
        line2: String?,
        postalCode: String?,
        state: String?,
    ) {
        TODO("Call Backend application to create location")
    }

    internal fun capturePaymentIntent(id: String) {
        service.capturePaymentIntent(id).execute()
    }

    internal fun cancelPaymentIntent(
        id: String,
        callback: Callback<Void>
    ) {
        service.cancelPaymentIntent(id).enqueue(callback)
    }

    /**
     * This method is calling the example backend (https://github.com/stripe/example-terminal-backend)
     * to create paymentIntent for Internet based readers, for example WisePOS E. For your own application, you
     * should create paymentIntent in your own merchant backend.
     */
    internal fun createPaymentIntent(
        amount: Long,
        currency: String,
        extendedAuth: Boolean,
        incrementalAuth: Boolean,
        callback: Callback<PaymentIntentCreationResponse>
    ) {
        val createPaymentIntentParams = buildMap<String, String> {
            put("amount", amount.toString())
            put("currency", currency)

            if (extendedAuth) {
                put("payment_method_options[card_present[request_extended_authorization]]", "true")
            }
            if (incrementalAuth) {
                put("payment_method_options[card_present[request_incremental_authorization_support]]", "true")
            }
        }

        service.createPaymentIntent(createPaymentIntentParams).enqueue(callback)
    }
}
