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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.watukazi.R
import com.google.firebase.database.*
import com.watukazi.app.models.WorkerModel
import com.watukazi.app.viewmodel.WorkerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateWorkerScreen(
    navController: NavController,
    workerId: String,
    workerViewModel: WorkerViewModel<Any?> = WorkerViewModel() // Ideally use hiltViewModel()
) {
    val context = LocalContext.current
    val imageUri = rememberSaveable { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { imageUri.value = it }
    }

    var workername by remember { mutableStateOf("") }
    var workerskill by remember { mutableStateOf("") }
    var workerphonenumber by remember { mutableStateOf("") }
    var workerrate by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    val workerRef = FirebaseDatabase.getInstance().getReference("Workers/$workerId")

    DisposableEffect(workerId) {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val worker = snapshot.getValue(WorkerModel::class.java)
                worker?.let {
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

        workerRef.addValueEventListener(listener)
        onDispose { workerRef.removeEventListener(listener) }
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

        Text("Attach Worker Image")

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = workername,
            onValueChange = { workername = it },
            label = { Text("Worker Name") },
            placeholder = { Text("Enter worker name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = workerskill,
            onValueChange = { workerskill = it },
            label = { Text("Worker Skill") },
            placeholder = { Text("Enter worker skill") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = workerphonenumber,
            onValueChange = { workerphonenumber = it },
            label = { Text("Phone Number") },
            placeholder = { Text("Enter phone number") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = workerrate,
            onValueChange = { workerrate = it },
            label = { Text("Rate") },
            placeholder = { Text("KES/hr or per job") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = desc,
            onValueChange = { desc = it },
            label = { Text("Description") },
            placeholder = { Text("Brief job description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            singleLine = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                navController.popBackStack()
            }) {
                Text("Back")
            }

            Button(onClick = {
                workerViewModel.updateWorker(
                    context = context,
                    navController = navController,
                    workername = workername,
                    workerskill = workerskill,
                    workerphonenumber = workerphonenumber,
                    workerrate = workerrate,
                    desc = desc,
                    productId = workerId
                )
            }) {
                Text("UPDATE")
            }
        }
    }
}
