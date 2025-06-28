package com.example.showspotter.screens

//import com.example.showspotter.Authorization.GoogleSignInUtils
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.showspotter.GoogleSignInUtils
import com.example.showspotter.R
import com.example.showspotter.authorization.LogInUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.CoroutineScope

@Composable
fun LoginScreen(databaseReference: DatabaseReference,auth: FirebaseAuth,openHomeScreen:()->Unit,scope: CoroutineScope,launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?) {
    val context = LocalContext.current
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(R.drawable.image1),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
                    colorFilter = ColorFilter.tint(
                    color = Color.Black.copy(alpha = 0.7f),
            blendMode = BlendMode.Multiply
        )
        )
        Column(
            modifier = Modifier.fillMaxSize().padding(bottom=70.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {

            Text(
                text = "Welcome back!\nSign in to continue!",
                fontFamily = FontFamily(Font(R.font.interbold)),
                fontSize = 26.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp, bottom = 60.dp),
                color = Color.White
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp, bottom = 15.dp)
                    .height(60.dp)
                    .clickable(onClick = {

                    }),
                colors = CardDefaults.cardColors(Color(0xFFDBDEDE)),
                shape = RoundedCornerShape(5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            onClick = {
//                        login with google
                                GoogleSignInUtils.doGoogleSignIn(
                                    context = context,
                                    scope = scope,
                                    launcher = launcher,
                                    login = {
                                        if(databaseReference.child("users").child(auth.currentUser!!.uid).get().isSuccessful){
                                            Toast.makeText(context, "Welcome Back",Toast.LENGTH_SHORT).show()
                                        }
                                        else{
                                            databaseReference.child("users").child(auth.currentUser!!.uid).child("username").setValue(auth.currentUser!!.displayName)
                                        }
                                        openHomeScreen()
                                    }
                                )
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = null,
                        modifier = Modifier.size(25.dp),
                        alignment = Alignment.CenterStart
                    )
                    Text(
                        text = "Continue with google",
                        modifier = Modifier.padding(start = 10.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Text(text = "or", color = Color.Gray)

            OutlinedTextField(
                value = email, onValueChange = {
                    email = it
                },
                label = { Text("email") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                ),
                modifier = Modifier.padding(top = 15.dp)
            )

            OutlinedTextField(
                value = password, onValueChange = {
                    password = it
                },
                label = { Text("password") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                )
            )

            Text("Forget Password?",color=Color.Gray,modifier=Modifier.padding(top=15.dp).clickable(
                onClick = {
                    if (email.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Please enter your email address",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val auth = FirebaseAuth.getInstance()

                        auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        context,
                                        "Password reset email sent",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Error: ${task.exception?.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    }
                }
            ))

            Button(
                onClick = {
                    LogInUser(auth, email, password, context, openHomeScreen)
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(start = 35.dp, end = 35.dp, top = 60.dp)
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF265AE8),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Log in", fontSize = 16.sp)
            }
        }
    }
}