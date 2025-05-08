package com.watukazi.app.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.database.*
import com.watukazi.app.models.*
import com.watukazi.app.api.ImgurApiService
import com.watukazi.app.daraja.STKPushRequest
import com.watukazi.app.network.RetrofitSafaricomClient
import com.example.watukazi.navigation.ROUTE_VIEW_WORKERS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class WorkerViewModel<T> : ViewModel() {
    private val database = FirebaseDatabase.getInstance().reference.child("Workers")

    private fun getImgurService(): ImgurApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().addInterceptor(logging).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.imgur.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ImgurApiService::class.java)
    }

    private fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File.createTempFile("temp_image", ".jpg", context.cacheDir)
            file.outputStream().use { output -> inputStream?.copyTo(output) }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun uploadWorkerWithImage(
        uri: Uri,
        context: Context,
        workername: String,
        workerskill: String,
        workerphonenumber: String,
        workerrate: String,
        desc: String,
        navController: NavController
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val file = getFileFromUri(context, uri)
                if (file == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Failed to process image", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("image", file.name, reqFile)

                val response = getImgurService().uploadImage(body).execute()

                if (response.isSuccessful) {
                    val imageUrl = response.body()?.data?.link ?: ""
                    val workerId = database.push().key ?: ""
                    val description = ""
                    val worker = WorkerModel(
                        workername,
                        workerskill,
                        workerrate,
                        workerphonenumber,
                        desc,
                        imageUrl,
                        workerId,
                        description = description
                    )

                    database.child(workerId).setValue(worker)
                        .addOnSuccessListener {
                            viewModelScope.launch {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Worker saved successfully", Toast.LENGTH_SHORT).show()
                                    navController.navigate(ROUTE_VIEW_WORKERS)
                                }
                            }
                        }.addOnFailureListener {
                            viewModelScope.launch {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Failed to save worker", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Exception: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun initiateSTKPush(phone: String, amount: String = "1") {
        val consumerKey = "bfb279f9aa9bdbcf4b65f26c72d1b7b4"
        val consumerSecret = "9b2197c3343b8b4e6f3f31e7b20e2d95"
        val passkey = "bfb279f9aa9bdbcf4b65f26c72d1b7b4f41b6b22f6b7f72d38b5a4fc6f2c3b2c"


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
                        businessShortCode = "174379",
                        password = password,
                        timestamp = timestamp,
                        transactionType = "CustomerPayBillOnline",
                        amount = amount,
                        partyA = phone,
                        partyB = "174379",
                        phoneNumber = "254718337346",
                        callBackURL = "https://mywatukazi.mock/callback",
                        accountReference = "WatuKazi",
                        transactionDesc = "Payment for labor"
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

    fun updateWorker(
        context: Context,
        navController: NavController,
        workername: String,
        workerskill: String,
        workerphonenumber: String,
        workerrate: String,
        desc: String,
        workerId: String
    ) {
        val updatedWorker = mapOf(
            "workername" to workername,
            "workerskill" to workerskill,
            "workerphonenumber" to workerphonenumber,
            "workerrate" to workerrate,
            "desc" to desc
        )

        database.child(workerId).updateChildren(updatedWorker)
            .addOnSuccessListener {
                Toast.makeText(context, "Worker updated", Toast.LENGTH_SHORT).show()
                navController.navigate(ROUTE_VIEW_WORKERS)
            }
            .addOnFailureListener {
                Toast.makeText(context, "Update failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun deleteWorker(context: Context, workerId: String, navController: NavHostController) {
        database.child(workerId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Worker deleted", Toast.LENGTH_SHORT).show()
                navController.navigate(ROUTE_VIEW_WORKERS)
            }
            .addOnFailureListener {
                Toast.makeText(context, "Delete failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun viewWorkers(
        emptyUploadState: MutableState<WorkerModel>,
        emptyUploadListState: SnapshotStateList<WorkerModel>,
        context: Context
    ) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                emptyUploadListState.clear()
                for (child in snapshot.children) {
                    val worker = child.getValue(WorkerModel::class.java)
                    worker?.let { emptyUploadListState.add(it) }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
