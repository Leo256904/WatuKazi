package com.example.watukazi.ui.theme.screens.workers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.watukazi.navigation.ROUTE_UPDATE_WORKER
import com.watukazi.app.models.WorkerModel
import com.watukazi.app.viewmodel.WorkerViewModel

@Composable
fun ViewWorkers(navController: NavHostController) {
    val context = LocalContext.current
    val workerRepository = WorkerViewModel<Any>()

    val emptyUploadState = remember {
        val description = ""
        mutableStateOf(WorkerModel("", "", "", "", "", "", "", description = description))
    }

    val emptyUploadListState = remember {
        mutableStateListOf<WorkerModel>()
    }

    // This populates the list in emptyUploadListState via Firebase call
    workerRepository.viewWorkers(emptyUploadState, emptyUploadListState, context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "All Workers",
            fontSize = 30.sp,
            fontFamily = FontFamily.SansSerif,
            color = Color.Black,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(emptyUploadListState) { worker ->
                WorkerItem(
                    workername = worker.workername,
                    workerskill = worker.workerskill,
                    workerphonenumber = worker.workerphonenumber,
                    workerrate = worker.workerrate,
                    desc = worker.desc,
                    workerId = worker.workerId,
                    imageUrl = worker.imageUrl,
                    navController = navController,
                    workerRepository = workerRepository
                )
            }
        }
    }
}

@Composable
fun WorkerItem(
    workername: String,
    workerskill: String,
    workerphonenumber: String,
    workerrate: String,
    desc: String,
    workerId: String,
    imageUrl: String,
    navController: NavHostController,
    workerRepository: WorkerViewModel<Any>
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.Gray)
    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            Column {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(160.dp)
                        .height(160.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            workerRepository.deleteWorker(context, workerId, navController)
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(Color.Red),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "REMOVE",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            navController.navigate("$ROUTE_UPDATE_WORKER/$workerId")
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(Color.Green),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "UPDATE",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
            ) {
                WorkerInfoText(label = "Name", value = workername)
                WorkerInfoText(label = "Skill", value = workerskill)
                WorkerInfoText(label = "Phone", value = workerphonenumber)
                WorkerInfoText(label = "Rate", value = workerrate)
                WorkerInfoText(label = "Desc", value = desc)
            }
        }
    }
}

@Composable
fun WorkerInfoText(label: String, value: String) {
    Text(
        text = "$label:",
        color = Color.Black,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = value,
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Preview
@Composable
fun ViewWorkersPreview() {
    ViewWorkers(navController = rememberNavController())
}

