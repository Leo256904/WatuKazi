package com.example.watukazi.ui.theme.screens.dashboard

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.watukazi.R
import com.example.watukazi.navigation.ROUTE_ADD_WORKER
import com.example.watukazi.navigation.ROUTE_WORKER_SELECTION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    val selectedItem = remember { mutableStateOf(0) }
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.Cyan) {
                NavigationBarItem(
                    selected = selectedItem.value == 0,
                    onClick = {
                        selectedItem.value = 0
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:0718337346")
                        }
                        context.startActivity(intent)
                    },
                    icon = { Icon(Icons.Filled.Phone, contentDescription = "Phone") },
                    label = { Text(text = "Phone") },
                    alwaysShowLabel = true,
                )
                NavigationBarItem(
                    selected = selectedItem.value == 1,
                    onClick = {
                        selectedItem.value = 1
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:info@emobilis.ac.ke")
                            putExtra(Intent.EXTRA_SUBJECT, "feedback")
                            putExtra(Intent.EXTRA_TEXT, "Hello, I would like to request for a casual labourer?")
                        }
                        context.startActivity(intent)
                    },
                    icon = { Icon(Icons.Filled.Email, contentDescription = "Email") },
                    label = { Text(text = "Email") },
                    alwaysShowLabel = true,
                )
                NavigationBarItem(
                    selected = selectedItem.value == 2,
                    onClick = {
                        selectedItem.value = 2
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "Download app here: https://www.download.com")
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    },
                    icon = { Icon(Icons.Filled.Share, contentDescription = "Share") },
                    label = { Text(text = "Share") },
                    alwaysShowLabel = true,
                )
            }
        }
    ) { innerPadding ->
        Box {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "Dashboard background image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.padding(innerPadding)
            )
        }

        Column {
            TopAppBar(
                title = { Text(text = "WatuKazi Mobile App") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Filled.Home, contentDescription = "Home")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Person, contentDescription = "Profile")
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Close, contentDescription = "Logout")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Cyan,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )

            // First Row
            Row {
                DashboardCard(text = "Workers") {
                    navController.navigate(ROUTE_ADD_WORKER)
                }
                DashboardCard(text = "Clients")
                DashboardCard(text = "Reports")
            }

            // Second Row
            Row {
                DashboardCard(text = "Orders")
                DashboardCard(text = "Payments")
                DashboardCard(text = "Settings")
            }

            // Third Row — New "Select Worker" card
            Row {
                DashboardCard(text = "Select Worker") {
                    navController.navigate(ROUTE_WORKER_SELECTION)
                }
            }
        }
    }
}

@Composable
fun DashboardCard(text: String, onClick: (() -> Unit)? = null) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(Color.Red)
    ) {
        Box(
            modifier = Modifier
                .height(100.dp)
                .padding(25.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(rememberNavController())
}
