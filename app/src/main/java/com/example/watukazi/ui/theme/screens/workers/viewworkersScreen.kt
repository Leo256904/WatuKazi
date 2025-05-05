package com.example.watukazi.ui.theme.screens.workers

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.work.Worker
import coil.compose.AsyncImage
import com.example.watukazi.data.ProductViewModel
import com.example.watukazi.R
import com.example.watukazi.models.WorkerModel
import com.example.watukazi.navigation.ROUTE_UPDATE_WORKER
import com.example.watukazi.navigation.ROUTE_UPDATE_WORKER
import com.watukazi.app.viewmodel.WorkerViewModel


@Composable

fun ViewWorkers(navController: NavHostController){


    val context = LocalContext.current


    val workerRepository = WorkerViewModel()


    val emptyUploadState = remember {


        mutableStateOf(


            WorkerModel("","","","","",""))


    }


    val emptyUploadListState = remember {


        mutableStateListOf<WorkerModel>()


    }


    val workers = workerRepository.viewWorkers(


        emptyUploadState,emptyUploadListState, context)


    Column (modifier = Modifier.fillMaxSize(),


        horizontalAlignment = Alignment.CenterHorizontally){


        Column(


            modifier = Modifier.fillMaxSize(),


            horizontalAlignment = Alignment.CenterHorizontally


        ) {


            Text(text = "All Workers",


                fontSize = 30.sp,


                fontFamily = FontFamily.SansSerif,


                color= Color.Black)


            Spacer(modifier = Modifier.height(20.dp))





            LazyColumn(){


                items(workers){


                    WorkerItem(


                        workername = it.workername,


                        workerskill = it.workerskill,

                        workerphonenumber = it.workerphonenumber,


                        workerrate = it.workerrate,


                        desc = it.desc,


                        workerId = it.workerId,


                        imageUrl = it.imageUrl,


                        navController = navController,


                        workerRepository = workerRepository





                    )


                }





            }


        }


    }


}


@Composable


fun ProductItem(workername:String,workerskill:String,workerphonenumber:String,workerrate:String,


                desc: String,workerId:String,imageUrl: String,navController: NavHostController,


                workerRepository: WorkerViewModel


){


    val context = LocalContext.current


    Column (modifier = Modifier.fillMaxWidth()){


        Card (modifier = Modifier


            .padding(10.dp)


            .height(210.dp),


            shape = MaterialTheme.shapes.medium,


            colors = CardDefaults.cardColors


                (containerColor = Color.Gray))


        {


            Row {


                Column {


                    AsyncImage(


                        model = imageUrl,


                        contentDescription = null,


                        contentScale = ContentScale.Crop,


                        modifier = Modifier


                            .width(200.dp)


                            .height(150.dp)


                            .padding(10.dp)


                    )





                    Row(horizontalArrangement = Arrangement.SpaceBetween) {


                        Button(onClick = {


                            workerRepository.deleteWorker(context,workerId,navController)





                        },


                            shape = RoundedCornerShape(10.dp),


                            colors = ButtonDefaults.buttonColors(Color.Red)


                        ) {


                            Text(text = "REMOVE",


                                color = Color.Black,


                                fontWeight = FontWeight.Bold,


                                fontSize = 16.sp)


                        }


                        Button(onClick = {


                            navController.navigate("$ROUTE_UPDATE_WORKER/$workerId")


                        },


                            shape = RoundedCornerShape(10.dp),


                            colors = ButtonDefaults.buttonColors(Color.Green)


                        ) {


                            Text(text = "UPDATE",


                                color = Color.Black,


                                fontWeight = FontWeight.Bold,


                                fontSize = 16.sp)


                        }


                    }





                }


                Column (modifier = Modifier


                    .padding(vertical = 10.dp, horizontal = 10.dp)


                    .verticalScroll(rememberScrollState())){


                    Text(text = "WORKER NAME",


                        color = Color.Black,


                        fontSize = 16.sp,


                        fontWeight = FontWeight.Bold)


                    Text(text = workername,


                        color = Color.White,


                        fontSize = 28.sp,


                        fontWeight = FontWeight.Bold)


                    Text(text = "WORKER SKILL",


                        color = Color.Black,


                        fontSize = 16.sp,


                        fontWeight = FontWeight.Bold)


                    Text(text = workerskill,


                        color = Color.White,


                        fontSize = 28.sp,


                        fontWeight = FontWeight.Bold)

                    Text(text = "WORKER PHONE NUMBER",


                        color = Color.Black,


                        fontSize = 16.sp,


                        fontWeight = FontWeight.Bold)


                    Text(text = workerphonenumber,


                        color = Color.White,


                        fontSize = 28.sp,


                        fontWeight = FontWeight.Bold)



                    Text(text = "WORKER RATE",


                        color = Color.Black,


                        fontSize = 16.sp,


                        fontWeight = FontWeight.Bold)


                    Text(text = workerrate,


                        color = Color.White,


                        fontSize = 28.sp,


                        fontWeight = FontWeight.Bold)


                    Text(text = "DESC",


                        color = Color.Black,


                        fontSize = 16.sp,


                        fontWeight = FontWeight.Bold)


                    Text(text = desc,


                        color = Color.White,


                        fontSize = 28.sp,


                        fontWeight = FontWeight.Bold)


                }


            }


        }


    }


}


@Preview


@Composable


fun ViewWorkersPreview(){


    ViewWorkers(rememberNavController())


}