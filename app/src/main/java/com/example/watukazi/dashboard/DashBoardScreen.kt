package com.watukazi.app.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.watukazi.app.auth.AuthViewModel
import com.watukazi.app.models.Worker
import com.watukazi.app.viewmodel.WorkerViewModel

@Composable
fun DashboardScreen(
    viewModel: AuthViewModel,
    workerViewModel: WorkerViewModel = viewModel()
) {
    val workers by workerViewModel.workerList.collectAsState()

    // Fetch workers from Firebase
    LaunchedEffect(Unit) {
        workerViewModel.loadWorkersFromFirebase()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("Available Workers", style = MaterialTheme.typography.titleLarge)

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(workers) { worker ->
                WorkerCard(worker, workerViewModel)
            }
        }
    }
}

@Composable
fun WorkerCard(worker: Worker, workerViewModel: WorkerViewModel) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Name: ${worker.name}")
            Text("Skill: ${worker.skill}")
            Text("Rate: KES ${worker.rate}")
            Text("Phone: ${worker.phone}")

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        val formattedPhone = formatPhone(worker.phone)
                        workerViewModel.initiateSTKPush(
                            phone = formattedPhone,
                            amount = worker.rate
                        )
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text("Hire & Pay")
                }

                Button(
                    onClick = {
                        workerViewModel.deleteWorker(worker.id)
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            }
        }
    }
}

fun formatPhone(localPhone: String): String {
    // Converts 0712345678 -> 254712345678
    return if (localPhone.startsWith("0")) {
        "254" + localPhone.drop(1)
    } else localPhone
}


