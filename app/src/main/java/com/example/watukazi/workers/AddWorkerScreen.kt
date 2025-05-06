package com.watukazi.app.workers

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.watukazi.app.models.WorkerModel
import java.util.*

@Composable
fun AddWorkerScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var skill by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    val db = FirebaseFirestore.getInstance()

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        OutlinedTextField(value = skill, onValueChange = { skill = it }, label = { Text("Skill") })
        OutlinedTextField(value = rate, onValueChange = { rate = it }, label = { Text("Rate") })
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") })

        Button(onClick = {
            val workerId = UUID.randomUUID().toString()
            val worker = WorkerModel(
                id = workerId,
                name = name,
                skill = skill,
                rate = rate,
                phone = phone
            )
            db.collection("workers").document(workerId).set(worker)
        }) {
            Text("Add Worker")
        }
    }
}


