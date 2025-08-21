package com.example.showspotter.designs

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.showspotter.R
import com.example.showspotter.tmdbapidataclass.Movie.MovieDetailsData
import com.example.showspotter.tmdbapidataclass.Series.SeriesDetailsOneData
import com.example.showspotter.viewmodels.TMDBViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

@SuppressLint("SuspiciousIndentation")
@Composable
fun UserTemplate(TMDBViewModel: TMDBViewModel, isMovie: Boolean, id:Int, auth: FirebaseAuth, databaseReference: DatabaseReference){//movieId
    val context = LocalContext.current
    var movieDetail:MovieDetailsData? =  null
    var seriesDetail: SeriesDetailsOneData? =  null
    if(isMovie) {
        TMDBViewModel.getMovieDetailById(id)
        movieDetail = TMDBViewModel.getMovieDetailsById.collectAsState().value
    }
    else{
        TMDBViewModel.getSeriesDetailsById(id)
        seriesDetail = TMDBViewModel.getSeriesDetailsById.collectAsState().value
    }
    var favourite by remember{
        mutableStateOf(false)
    }
    var watchlist by remember{
        mutableStateOf(false)
    }
    var addToList by remember{
        mutableStateOf(false)
    }

    var dataType = "movie" //series or movie, saved is series or movie
    if(!isMovie){
        dataType = "series"
    }


    //watchlist
    val watchRef = databaseReference.child("users").child(auth.currentUser?.uid ?: "NA")
        .child("watchlist").child(dataType).child(id.toString())

    watchRef.get().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val snapshot = task.result
            if (snapshot.exists()) {
                // ID is present in the branch
                watchlist = true
            }
        }
    }
    var watchbg: Color = Color.Transparent
        if(watchlist) {
            watchbg = Color(0xFF103661)
        }



    //addtolist
    var addbg: Color = Color.Transparent
        if(addToList) {
            addbg = Color(0xFF103661)
        }




    //favourite
    val favRef = databaseReference.child("users").child(auth.currentUser?.uid ?: "NA")
        .child("favourite").child(dataType).child(id.toString())

    favRef.get().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val snapshot = task.result
            if (snapshot.exists()) {
                // ID is present in the branch
                favourite = true
            }
        }
    }

    var favbg: Color = Color.Transparent
        if(favourite) {
            favbg = Color(0xFF103661)
        }



    Row(
        Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 20.dp)
            .background(Color(0xFF2e2d2d), shape = RoundedCornerShape(10.dp)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            Modifier
                .fillMaxHeight(1f)
                .padding(start = 10.dp, end = 10.dp)
                .background(favbg, shape = RoundedCornerShape(10.dp))
                .padding(10.dp)
                .clickable(onClick = {
                    //favourite
                    favourite = !favourite
                     // fav true means added // false means removed
                    if (favourite) { // add to data base // current user can never be null here
                        if(isMovie) {
                                databaseReference.child("users")
                                    .child(auth.currentUser?.uid ?: "NA")
                                    .child("favourite").child(dataType).child(id.toString())
                                    .setValue(movieDetail) // just make as child named movie as id will be unique
                            //push create a auto. unique id where i can reside data
                            Toast.makeText(context, "Added to favourite", Toast.LENGTH_SHORT).show()
                        }
                        else{
                                databaseReference.child("users")
                                    .child(auth.currentUser?.uid ?: "NA")
                                    .child("favourite").child(dataType).child(id.toString())
                                    .setValue(seriesDetail) // just make as child named movie as id will be unique
                            Toast.makeText(context, "Added to favourite", Toast.LENGTH_SHORT).show()

                        }
                    }
                    else{ // if favourite is false remove from database if present, obviously it will be present
                        val dbRef =
                            databaseReference.child("users").child(auth.currentUser?.uid ?: "NA")
                                .child("favourite").child(dataType)
                        dbRef.child(id.toString()).removeValue()
                        Toast.makeText(context, "Removed from favourite", Toast.LENGTH_SHORT).show()
                    }


                }),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(R.drawable.favourite), contentDescription = "favourite",
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .size(40.dp)
            )
            Text(
                "Favourite",
                fontSize = 12.sp,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.robotomedium))
            )
        }
        Column(
            Modifier
                .fillMaxHeight(1f)
                .padding(start = 10.dp, end = 10.dp)
                .background(watchbg, shape = RoundedCornerShape(10.dp))
                .padding(10.dp)
                .clickable(onClick = {
                    //watchlist
                    watchlist = !watchlist
                    if (watchlist) { // add to data base // current user can never be null here
                        if(isMovie) {
                                databaseReference.child("users")
                                    .child(auth.currentUser?.uid ?: "NA")
                                    .child("watchlist").child(dataType).child(id.toString())
                                    .setValue(movieDetail) // just make as child named movie as id will be unique
                            //push create a auto. unique id where i can reside data
                            Toast.makeText(context, "Added to watchlist", Toast.LENGTH_SHORT).show()
                        }
                        else{
                                databaseReference.child("users")
                                    .child(auth.currentUser?.uid ?: "NA")
                                    .child("watchlist").child(dataType).child(id.toString())
                                    .setValue(seriesDetail) // just make as child named movie as id will be unique
                        }
                    }
                    else{ // if favourite is false remove from database if present, obviously it will be present
                        val dbRef =
                            databaseReference.child("users").child(auth.currentUser?.uid ?: "NA")
                                .child("watchlist").child(dataType)
                        dbRef.child(id.toString()).removeValue()
                        Toast.makeText(context, "Removed from watchlist", Toast.LENGTH_SHORT).show()

                    }


                }),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(R.drawable.bookmark), contentDescription = "bookmark",
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .size(40.dp)
            )
            Text(
                "Watchlist",
                fontSize = 12.sp,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.robotomedium))
            )
        }
    }
}