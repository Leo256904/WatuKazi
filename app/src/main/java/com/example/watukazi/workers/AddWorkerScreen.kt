package com.watukazi.app.workers

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.watukazi.R
import com.google.firebase.firestore.FirebaseFirestore
import com.watukazi.app.models.WorkerModel
import java.util.*

@Composable
fun AddWorkerScreen(navController: NavHostController) {
    val context = LocalContext.current
    val imageUri = rememberSaveable() { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imageUri.value = it }
    }

    var name by remember { mutableStateOf("") }
    var skill by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val db = FirebaseFirestore.getInstance()

    fun validateInputs(): Boolean {
        return name.isNotEmpty() && skill.isNotEmpty() && rate.isNotEmpty() && phone.isNotEmpty()
    }

    fun addWorker() {
        if (!validateInputs()) {
            errorMessage = "Please fill in all fields"
            return
        }

        isLoading = true
        val workerId = UUID.randomUUID().toString()
        val worker = WorkerModel(
            id = workerId,
            name = name,
            skill = skill,
            rate = rate,
            phone = phone,
            description = description
        )

        db.collection("workers").document(workerId).set(worker)
            .addOnSuccessListener {
                isLoading = false
                Toast.makeText(context, "Worker added successfully", Toast.LENGTH_SHORT).show()
                navController.popBackStack() // Navigate back after successful addition
            }
            .addOnFailureListener { exception ->
                isLoading = false
                errorMessage = "Failed to add worker: ${exception.localizedMessage}"
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add New Worker",
            fontSize = 40.sp,
            color = Color.Blue,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(Color.White)
                .padding(15.dp)
                .fillMaxWidth()
        )

        // Profile image section
        Card(shape = CircleShape, modifier = Modifier.padding(30.dp).size(180.dp)) {
            AsyncImage(
                model = imageUri.value ?: R.drawable.ic_person,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .clickable { launcher.launch("image/*") }
            )
        }

        Text(text = "Attach Worker Image")

        // Input fields
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter Name") },
            placeholder = { Text("Please enter worker's name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = skill,
            onValueChange = { skill = it },
            label = { Text("Enter Skill") },
            placeholder = { Text("Please enter worker's skill") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = rate,
            onValueChange = { rate = it },
            label = { Text("Enter Rate") },
            placeholder = { Text("Please enter worker's rate") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Enter Phone Number") },
            placeholder = { Text("Please enter worker's phone number") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Brief Description") },
            placeholder = { Text("Please enter a brief description") },
            modifier = Modifier.fillMaxWidth().height(150.dp),
            singleLine = false
        )

        // Error message
        if (errorMessage != null) {
            Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { navController.popBackStack() }, colors = ButtonDefaults.buttonColors(Color.Black)) {
                Text(text = "HOME")
            }

            Button(
                onClick = {
                    imageUri.value?.let {
                        addWorker()
                    } ?: Toast.makeText(context, "Please pick an image", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(Color.Green)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text(text = "SAVE")
                }
            }
        }
    }
}
