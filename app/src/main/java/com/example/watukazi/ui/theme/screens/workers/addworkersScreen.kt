package com.example.watukazi.ui.theme.screens.workers

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.watukazi.R
import com.watukazi.app.viewmodel.WorkerViewModel

@Composable
fun AddworkerScreen(navController: NavController) {
    val context = LocalContext.current
    val imageUri = rememberSaveable() { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imageUri.value = it }
    }

    var workername by remember { mutableStateOf("") }
    var workerskill by remember { mutableStateOf("") }
    var workerphonenumber by remember { mutableStateOf("") }
    var workerrate by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    val workerViewModel: WorkerViewModel<Any?> = viewModel()

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

        Card(
            shape = CircleShape,
            modifier = Modifier
                .padding(30.dp)
                .size(180.dp)
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

        Text(text = "Attach worker image")

        OutlinedTextField(
            value = workername,
            onValueChange = { newworkername -> workername = newworkername },
            label = { Text(text = "Enter Worker Name") },
            placeholder = { Text(text = "Please enter worker name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = workerskill,
            onValueChange = { newworkerskill -> workerskill = newworkerskill },
            label = { Text(text = "Enter Worker Skill") },
            placeholder = { Text(text = "Please enter worker skill") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = workerphonenumber,
            onValueChange = { newworkerphonenumber -> workerphonenumber = newworkerphonenumber },
            label = { Text(text = "Enter Phone Number") },
            placeholder = { Text(text = "Please enter phone number") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = workerrate,
            onValueChange = { newworkerrate -> workerrate = newworkerrate },
            label = { Text(text = "Enter Worker Rate Price") },
            placeholder = { Text(text = "Please enter worker rate price") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = desc,
            onValueChange = { newDesc -> desc = newDesc },
            label = { Text(text = "Brief description") },
            placeholder = { Text(text = "Please enter worker description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            singleLine = false
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { /* TODO: Handle Home navigation */ },
                colors = ButtonDefaults.buttonColors(Color.Black)
            ) {
                Text(text = "HOME")
            }

            Button(
                onClick = {
                    imageUri.value?.let {
                        workerViewModel.uploadWorkerWithImage(
                            it,
                            context,
                            workername,
                            workerskill,
                            workerphonenumber,
                            workerrate,
                            desc,
                            navController
                        )
                    } ?: Toast.makeText(context, "Please pick an image", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(Color.Green)
            ) {
                Text(text = "SAVE")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddworkerScreenPreview() {
    AddworkerScreen(rememberNavController())
}
