
package com.watukazi.app.auth

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import com.google.firebase.auth.FirebaseAuth
import com.watukazi.app.models.UserModel

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var isLoggedIn by mutableStateOf(false)
        private set

    var currentUser by mutableStateOf<UserModel?>(null)

    init {
        auth.currentUser?.let {
            currentUser = UserModel(it.email ?: "")
            isLoggedIn = true
        }
    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    currentUser = UserModel(email)
                    isLoggedIn = true
                }
            }
    }

    fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    currentUser = UserModel(email)
                    isLoggedIn = true
                }
            }
    }
}
