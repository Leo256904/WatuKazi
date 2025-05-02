package com.watukazi.app.viewmodel

import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Worker
import com.google.firebase.firestore.FirebaseFirestore
import com.watukazi.app.daraja.STKPushRequest
import com.watukazi.app.models.Worker
import com.watukazi.app.models.STKPushRequest
import com.watukazi.app.models.AccessTokenResponse
import com.watukazi.app.models.STKPushResponse
import com.watukazi.app.network.RetrofitSafaricomClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class WorkerViewModel : ViewModel() {

    private val _workerList = MutableStateFlow<List<Worker>>(emptyList())
    val workerList = _workerList.asStateFlow()

    private val db = FirebaseFirestore.getInstance()

    fun loadWorkersFromFirebase() {
        db.collection("workers")
            .get()
            .addOnSuccessListener { result ->
                val workers = result.map { doc ->
                }
                Worker(
                    id = doc.id,
                    name = doc.getString("name") ?: "",
                    skill = doc.getString("skill") ?: "",
                    rate = doc.getString("rate") ?: "0",
                    phone = doc.getString("phone") ?: ""
                )
                _workerList.value = workers
            }
            .addOnFailureListener {
                Log.e("FIRESTORE", "Error fetching workers: ${it.message}")
            }
    }

    fun deleteWorker(workerId: String) {
        db.collection("workers").document(workerId)
            .delete()
            .addOnSuccessListener {
                loadWorkersFromFirebase() // Refresh list
            }
            .addOnFailureListener {
                Log.e("FIRESTORE", "Error deleting worker: ${it.message}")
            }
    }

    fun initiateSTKPush(phone: String, amount: String = "1") {
        val consumerKey = "PLQyzax3E5NwaER5aKR17QCsKWsGWQ3HwPh6qAfM3aVYAns9"
        val consumerSecret = "PaIzn3WxcOebHUsnxLeJ0ZwA3bfgNPxYCcX0VAa8Bt00T94cKf6jndbeAGL3kK4t"
        val passkey = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919"

        val basicAuth = "Basic " + Base64.encodeToString(
            "$consumerKey:$consumerSecret".toByteArray(), Base64.NO_WRAP
        )

        RetrofitSafaricomClient.api.getAccessToken(basicAuth).enqueue(object : Callback<AccessTokenResponse> {
            override fun onResponse(call: Call<AccessTokenResponse>, response: Response<AccessTokenResponse>) {
                if (response.isSuccessful) {
                    val accessToken = "Bearer ${response.body()?.access_token}"
                    val timestamp = getTimestamp()
                    val password = Base64.encodeToString(
                        ("174379$passkey$timestamp").toByteArray(), Base64.NO_WRAP
                    )

                    val stkPushRequest = STKPushRequest(
                        BusinessShortCode = "174379",
                        Password = password,
                        Timestamp = timestamp,
                        TransactionType = "CustomerPayBillOnline",
                        Amount = amount,
                        PartyA = "600977",
                        PartyB = "600000",
                        PhoneNumber = "0718337346",
                        CallBackURL = "https://mywatukazi.mock/callback",
                        AccountReference = "WatuKazi",
                        TransactionDesc = "Payment for labor"
                    )

                    RetrofitSafaricomClient.api.initiateStkPush(accessToken, stkPushRequest)
                        .enqueue(object : Callback<STKPushResponse> {
                            override fun onResponse(call: Call<STKPushResponse>, response: Response<STKPushResponse>) {
                                if (response.isSuccessful) {
                                    Log.d("MPESA", "STK Push success: ${response.body()?.CustomerMessage}")
                                } else {
                                    Log.e("MPESA", "STK Push error: ${response.message()}")
                                }
                            }

                            override fun onFailure(call: Call<STKPushResponse>, t: Throwable) {
                                Log.e("MPESA", "STK Push failed: ${t.message}")
                            }
                        })
                } else {
                    Log.e("TOKEN", "Access token fetch failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                Log.e("TOKEN", "Token fetch error: ${t.message}")
            }
        })
    }

    private fun getTimestamp(): String {
        val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        return sdf.format(Date())
    }
}

