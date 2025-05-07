package com.watukazi.app.daraja

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*
import android.util.Base64
import com.watukazi.app.util.Constants

data class STKPushRequest(
    @SerializedName("BusinessShortCode") val businessShortCode: String,
    @SerializedName("Password") val password: String,
    @SerializedName("Timestamp") val timestamp: String,
    @SerializedName("TransactionType") val transactionType: String,
    @SerializedName("Amount") val amount: String,
    @SerializedName("PartyA") val partyA: String,
    @SerializedName("PartyB") val partyB: String,
    @SerializedName("PhoneNumber") val phoneNumber: String,
    @SerializedName("CallBackURL") val callBackURL: String,
    @SerializedName("AccountReference") val accountReference: String,
    @SerializedName("TransactionDesc") val transactionDesc: String
)

fun getTimestamp(): String {
    val formatter = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
    return formatter.format(Date())
}

fun generatePassword(): String {
    val timestamp = getTimestamp()
    val dataToEncode = Constants.BUSINESS_SHORT_CODE + Constants.PASSKEY + timestamp
    return Base64.encodeToString(dataToEncode.toByteArray(), Base64.NO_WRAP)
}
