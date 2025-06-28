package com.example.showspotter.authorization

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

fun SignUpUser(
    databaseReference: DatabaseReference,
    auth: FirebaseAuth,
    username: String,
    email: String,
    password: String,
    context: Context,
    signSuccessGoToLoginScreen: () -> Unit
) {

    if (email.isNotBlank() && password.isNotBlank()) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task -> // this is a callback, gets executed when sign up is done, sign up can take some time
                if (task.isSuccessful) {
                    Toast.makeText(context, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                    if(databaseReference.child("users").child(auth.currentUser!!.uid).get().isSuccessful){
                        Toast.makeText(context, "Welcome Back",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        databaseReference.child("users").child(auth.currentUser!!.uid).child("username").setValue(username)
                        Toast.makeText(context, "New User",Toast.LENGTH_SHORT).show()
                    }
                    signSuccessGoToLoginScreen()
                } else {
                    Toast.makeText(
                        context,
                        "Sign Up Failed ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }
}