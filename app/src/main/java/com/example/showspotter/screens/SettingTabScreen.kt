package com.example.showspotter.screens

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.showspotter.R
import com.example.showspotter.designs.BottomNavigatorDesign
import java.io.File


@Composable
fun SettingTabScreen(
    navController: NavController,
    goToHomeScreen: () -> Unit,
    goToMovieTabScreen: () -> Unit,
    goToSeriesTabScreen: () -> Unit,
    goToSearchScreen: () -> Unit,
    goToProfileScreen: () -> Unit,
    goToSettingTabScreen:()->Unit
) {
    val context = LocalContext.current
    val appVersion = getAppVersion(context)
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1f1f1f))
                .verticalScroll(rememberScrollState())
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 45.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "profile",
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .size(35.dp)
                        .background(Color(0xFF121212), shape = CircleShape)
                        .padding(8.dp)
                        .clip(CircleShape)
                        .clickable { goToProfileScreen() }
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.appicon),
                        contentDescription = "appicon",
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        text = "MovieSaga",
                        fontSize = 13.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.interbold))
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.searchicon),
                    contentDescription = "search",
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .size(35.dp)
                        .background(Color(0xFF121212), shape = CircleShape)
                        .clip(CircleShape)
                        .padding(8.dp)
                        .clickable { goToSearchScreen() }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Settings",
                fontSize = 22.sp,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.interbold)),
                modifier = Modifier.padding(20.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Contact Support
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
                    .height(70.dp)
                    .background(Color(0xFF2e2d2d), shape = RoundedCornerShape(10.dp))
                    .clickable {
                        val emailIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "message/rfc822"
                            putExtra(Intent.EXTRA_EMAIL, arrayOf("aniiketom70@gmail.com"))
                            putExtra(Intent.EXTRA_SUBJECT, "Support Request")
                            putExtra(Intent.EXTRA_TEXT, "We will help you shortly,\n\n Please Explain your Issue")
                        }
                        context.startActivity(Intent.createChooser(emailIntent, "Send Email"))
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Contact Support",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.interbold)),
                    modifier = Modifier.padding(20.dp)
                )
                Image(
                    painterResource(id = R.drawable.back), contentDescription = null,
                    Modifier
                        .padding(end = 10.dp)
                        .rotate(180f)
                        .size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Clear Cache Section
            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp).height(70.dp)
                    .background(Color(0xFF2e2d2d), shape = RoundedCornerShape(10.dp))
                    .clickable { showDialog = true },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Clear Cache",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.interbold)),
                    modifier = Modifier.padding(20.dp)
                )
                Image(
                    painterResource(id = R.drawable.back), contentDescription = null,
                    Modifier.padding(end = 10.dp).rotate(180f).size(20.dp)
                )
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        TextButton(onClick = {
                            clearAppCache(context)
                            Toast.makeText(context, "Cache Cleared", Toast.LENGTH_SHORT).show()
                            showDialog = false
                        }) {
                            Text("Yes", color = Color.White)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("No", color = Color.White)
                        }
                    },
                    title = { Text("Clear Cache", color = Color.White) },
                    text = { Text("Are you sure you want to clear the cache?", color = Color.White) },
                    containerColor = Color(0xFF2e2d2d)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // App Version Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
                    .height(70.dp)
                    .background(Color(0xFF2e2d2d), shape = RoundedCornerShape(10.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.padding(start = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "App Version: $appVersion",
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }


            Spacer(modifier = Modifier.height(100.dp))
        }
    }

    BottomNavigatorDesign(
        navController,
        goToHomeScreen,
        goToMovieTabScreen,
        goToSeriesTabScreen,
        goToSettingTabScreen
    )
}
// Function to clear app cache
fun clearAppCache(context: Context) {
    try {
        val cacheDir: File = context.cacheDir
        cacheDir.deleteRecursively()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// Function to get app version
fun getAppVersion(context: Context): String {
    return try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName ?: "Unknown"
    } catch (_: PackageManager.NameNotFoundException) {
        "Unknown"
    }
}
