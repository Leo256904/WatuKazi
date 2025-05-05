package com.example.watukazi.ui.theme.screens.workers

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.watukazi.R
import com.example.watukazi.models.WorkerModel
import com.google.firebase.database.*
import com.watukazi.app.models.WorkerModel
import com.watukazi.app.viewmodel.WorkerViewModel

@Composable
fun UpdateworkerScreen(navController: NavController, productId: String) {
    val context = LocalContext.current
    val imageUri = rememberSaveable { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imageUri.value = it }
    }

    var workername by remember { mutableStateOf("") }
    var workerskill by remember { mutableStateOf("") }
    var workerphonenumber by remember { mutableStateOf("") }
    var workerrate by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    val currentDataRef = FirebaseDatabase.getInstance().getReference("Products/$productId")

    DisposableEffect(Unit) {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val product = snapshot.getValue(WorkerModel::class.java)
                product?.let {
                    workername = it.workername
                    workerskill = it.workerskill
                    workerphonenumber = it.workerphonenumber
                    workerrate = it.workerrate
                    desc = it.desc
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
            }
        }
        currentDataRef.addValueEventListener(listener)
        onDispose { currentDataRef.removeEventListener(listener) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .padding(10.dp)
                .size(200.dp)
        ) {
            AsyncImage(
                model = imageUri.value ?: R.drawable.ic_person,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .clickable { launcher.launch("image/*") }
            )
        }

        Text(text = "Attach product image")

        OutlinedTextField(
            value = workername,
            onValueChange = { newWorkername -> workername = newWorkername },
            label = { Text(text = "Worker Name") },
            placeholder = { Text(text = "Please enter worker name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = workerskill,
            onValueChange = { newWorkerskill -> workerskill = newWorkerskill },
            label = { Text(text = "Worker Skill") },
            placeholder = { Text(text = "Please enter worker skill") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = workerphonenumber,
            onValueChange = { newWorkerphonenumber -> workerphonenumber = newWorkerphonenumber },
            label = { Text(text = "Worker Phone Number") },
            placeholder = { Text(text = "Please enter worker phone number") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = workerrate,
            onValueChange = { newWorkerRate -> workerrate = newWorkerRate },
            label = { Text(text = "Worker Rate Price") },
            placeholder = { Text(text = "Please enter worker rate price") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = desc,
            onValueChange = { newDesc -> desc = newDesc },
            label = { Text(text = "Brief description") },
            placeholder = { Text(text = "Please enter product description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            singleLine = false
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                // Optional: Navigate to another screen or refresh list
            }) {
                Text(text = "All Products")
            }

            Button(onClick = {
                val productRepository = WorkerViewModel()
                productRepository.updateWorker(
                    context = context,
                    navController = navController,
                    workername = workername,
                    workerskill = workerskill,
                    workerphonenumber = workerphonenumber,
                    workerrate = workerrate,
                    desc = desc,
                    productId = productId
                )
            }) {
                Text(text = "UPDATE")
            }
        }
    }
}
