package com.watukazi.app.daraja

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*
import android.util.Base64
import com.watukazi.app.util.Constants

data class STKPushRequest(
    @SerializedName("BusinessShortCode") val businessShortCode: String = Constants.BUSINESS_SHORT_CODE,
    @SerializedName("Password") val password: String = generatePassword(),
    @SerializedName("Timestamp") val timestamp: String = getTimestamp(),
    @SerializedName("TransactionType") val transactionType: String = Constants.TRANSACTION_TYPE,
    @SerializedName("Amount") val amount: String,
    @SerializedName("PartyA") val partyA: String, // Customer phone number (e.g., 254718337346)
    @SerializedName("PartyB") val partyB: String = Constants.BUSINESS_SHORT_CODE,
    @SerializedName("PhoneNumber") val phoneNumber: String, // Must match PartyA
    @SerializedName("CallBackURL") val callBackURL: String = Constants.CALLBACK_URL,
    @SerializedName("AccountReference") val accountReference: String = "WatuKaziApp",
    @SerializedName("TransactionDesc") val transactionDesc: String = "Payment for casual labour",
    val BusinessShortCode: String,
    val Password: String,
    val Timestamp: String,
    val TransactionType: String,
    val Amount: String,
    val PartyB: String,
    val PartyA: String,
    val PhoneNumber: String,
    val CallBackURL: String,
    val AccountReference: String,
    val TransactionDesc: String
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


