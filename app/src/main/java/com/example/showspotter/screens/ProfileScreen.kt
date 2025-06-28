package com.example.showspotter.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.example.showspotter.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference


@Composable
fun Profile(
    databaseReference: DatabaseReference,
    auth: FirebaseAuth,
    goToOnBoardingScreen: () -> Unit,
    goToBackStack: () -> Unit,
    goToFavouriteScreen:()->Unit,
    goToWatchlistScreen:()->Unit,
    goToPremiumTabScreen:()->Unit
) {
    //if already login then show logout button
    var name by remember { //username
        mutableStateOf("")
    }
    if (auth.currentUser == null) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color(0xFF1f1f1f)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Login/SignUp to TMDb account",
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.interbold)),
                color = Color(0xFFE0E0E0),
                modifier = Modifier.padding(bottom = 20.dp)
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .height(70.dp)
                    .background(Color(0xFF2e2d2d), shape = RoundedCornerShape(10.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painterResource(id = R.drawable.star),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.2f)
                        .size(40.dp)
                )
                Text(
                    "Rate Movies and TV shows",
                    color = Color(0xFFE0E0E0),
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.interfont)),
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .height(70.dp)
                    .background(Color(0xFF2e2d2d), shape = RoundedCornerShape(10.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painterResource(id = R.drawable.bookmark),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.2f)
                        .size(40.dp)
                )
                Text(
                    "Manage your watchlist",
                    color = Color(0xFFE0E0E0),
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.interfont)),
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .height(70.dp)
                    .background(Color(0xFF2e2d2d), shape = RoundedCornerShape(10.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painterResource(id = R.drawable.premium),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.2f)
                        .size(40.dp)
                )
                Text(
                    "Get Premium Features",
                    color = Color(0xFFE0E0E0),
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.interfont)),
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .height(70.dp)
                    .background(Color(0xFF2e2d2d), shape = RoundedCornerShape(10.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painterResource(id = R.drawable.list),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.2f)
                        .size(40.dp)
                )
                Text(
                    "Create your list of movies and series",
                    color = Color(0xFFE0E0E0),
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.interfont)),
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Button(
                onClick = {
                    goToOnBoardingScreen()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 30.dp)
                    .size(70.dp)
            ) {
                Text(
                    "Login/SignUp",
                    color = Color(0xFFE0E0E0),
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.interfont)),
                    modifier = Modifier.fillMaxWidth(0.8f),
                    textAlign = TextAlign.Center
                )
            }
        }

    } else {
        val dbRef = databaseReference.child("users").child(auth.currentUser!!.uid)
        dbRef.child("username").get()
            .addOnSuccessListener { snapshot ->
                val username = snapshot.getValue(String::class.java)
                if (!username.isNullOrEmpty()) {
                    // If username exists in database, show it
                    name = username
                } else {
                    // If username doesn't exist, use Gmail Profile Name
                    val googleName = auth.currentUser?.displayName
                    if (!googleName.isNullOrEmpty()) {
                        name = googleName
                    } else {
                        // If Gmail Profile Name also missing, show default
                        name = "Unknown User"
                    }
                }
            }
            .addOnFailureListener {
                // If Firebase fails, fallback to Gmail Name
                val googleName = auth.currentUser?.displayName
                if (!googleName.isNullOrEmpty()) {
                    name = googleName
                } else {
                    name = "Unknown User"
                }
            }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1f1f1f))
            ) {
                Spacer(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .background(Color.Black)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .fillMaxHeight(), verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.back), contentDescription =
                            "back",
                        modifier = Modifier
                            .padding(start = 13.dp)
                            .size(16.dp)
                            .clickable(
                                onClick = {
                                    goToBackStack()
                                }
                            )
                    )
                    //more content on top Row
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(Color(0xFF2e2d2d)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val user = auth.currentUser
                    val profilePicUrl = user?.photoUrl

                    if (user != null && profilePicUrl != null) {
                        // Show Gmail DP if signed in with Gmail OR Firebase DP if available
                        Image(
                            painter = rememberAsyncImagePainter(model = profilePicUrl),
                            contentDescription = null,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(50.dp))
                        )
                    } else {
                        // Show App Icon if no login or no profile picture available
                        Image(
                            painterResource(R.drawable.appicon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(50.dp))
                        )
                    }


                    Spacer(modifier = Modifier.size(16.dp))

                    Column {
                        Text(
                            "Hello,",
                            color = Color(0xFFE0E0E0),
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.interfont)),
                        )
                        Text(
                            text = name,
                            color = Color(0xFFE0E0E0),
                            fontSize = 25.sp,
                            fontFamily = FontFamily(Font(R.font.interfont)),
                        )
                    }
                }
                var alertBox by remember {
                    mutableStateOf(false)
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(Modifier.padding(top = 20.dp)) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp)
                                .height(70.dp)
                                .background(Color(0xFF2e2d2d), shape = RoundedCornerShape(10.dp))
                                .clickable(onClick = {
                                    goToFavouriteScreen()
                                }),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.padding(start = 10.dp),
                                horizontalArrangement = Arrangement.spacedBy(15.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painterResource(id = R.drawable.favourite),
                                    contentDescription = null,
                                    Modifier.size(40.dp)
                                )
                                Text(
                                    "Favourite",
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.interbold)),
                                    color = Color(0xFFE0E0E0)
                                )
                            }
                            Image(
                                painterResource(id = R.drawable.back), contentDescription = null,
                                Modifier
                                    .padding(end = 10.dp)
                                    .rotate(180f)
                                    .size(20.dp)
                            )
                        }


                        Spacer(modifier = Modifier.height(10.dp))


                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp)
                                .height(70.dp)
                                .background(Color(0xFF2e2d2d), shape = RoundedCornerShape(10.dp))
                                .clickable(onClick = {
                                    goToWatchlistScreen()
                                }),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.padding(start = 10.dp),
                                horizontalArrangement = Arrangement.spacedBy(15.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painterResource(id = R.drawable.bookmark),
                                    contentDescription = null,
                                    Modifier.size(40.dp)
                                )
                                Text(
                                    "Watchlist",
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.interbold)),
                                    color = Color(0xFFE0E0E0)
                                )
                            }
                            Image(
                                painterResource(id = R.drawable.back), contentDescription = null,
                                Modifier
                                    .padding(end = 10.dp)
                                    .rotate(180f)
                                    .size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp)
                                .height(70.dp)
                                .background(Color(0xFF2e2d2d), shape = RoundedCornerShape(10.dp))
                                .clickable(onClick = {
                                    goToPremiumTabScreen()
                                }),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.padding(start = 10.dp),
                                horizontalArrangement = Arrangement.spacedBy(15.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painterResource(id = R.drawable.premium),
                                    contentDescription = null,
                                    Modifier.size(40.dp)
                                )
                                Text(
                                    "Premium",
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.interbold)),
                                    color = Color(0xFFE0E0E0)
                                )
                            }
                            Image(
                                painterResource(id = R.drawable.back), contentDescription = null,
                                Modifier
                                    .padding(end = 10.dp)
                                    .rotate(180f)
                                    .size(20.dp)
                            )
                        }


                    }
                    Button(
                        onClick = {
                            alertBox = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 30.dp)
                            .size(70.dp)
                            .align(Alignment.BottomEnd)
                    ) {
                        Text(
                            "Log Out",
                            color = Color(0xFFE0E0E0),
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.interfont)),
                            modifier = Modifier.fillMaxWidth(0.8f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                if (alertBox) {
                    AlertDialog(
                        onDismissRequest = { alertBox = false },
                        confirmButton = {
                            Button(onClick = {
                                auth.signOut()
                                goToOnBoardingScreen()
                            }) {
                                Text(text = "Yes")
                            }
                        },
                        dismissButton = {
                            Button(onClick = {
                                alertBox = false
                            }) {
                                Text(text = "NO")
                            }
                        },
                        title = {
                            Text("Confirm Logout?")
                        },
                        text = {
                            Text("Are you sure you want to logout?")
                        }

                    )

                }

            }
        }
    }
}

