package com.example.showspotter.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.showspotter.R
import com.example.showspotter.designs.LazyRowFavouriteWatchlistSeriesDesign
import com.example.showspotter.designs.LazyRowMoviesFavouriteWatchListDesign
import com.example.showspotter.tmdbapidataclass.Movie.MovieDetailsData
import com.example.showspotter.tmdbapidataclass.Series.SeriesDetailsOneData
import com.example.showspotter.viewmodels.TMDBViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

@Composable
fun FavouriteScreen(auth: FirebaseAuth, databaseReference: DatabaseReference, goToBackStack:()->Unit, goToMovieDescScreen:(id:Int)->Unit, goToSeriesDescScreen:(id:Int)->Unit){
    val context = LocalContext.current

    //for movie
    val favMovieList = remember { mutableStateListOf<MovieDetailsData?>() }
    // Get reference to the Firebase database
    val favMoviesRef = databaseReference.child("users").child(auth.currentUser?.uid ?: "NA")
        .child("favourite").child("movie")

    // Fetch favorite movies
    LaunchedEffect(Unit) {
        favMoviesRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                if (dataSnapshot.exists()) {
                    // List to hold all movie details
                    val favMovieDetailsList = mutableListOf<MovieDetailsData>()

                    // Loop through all the children under the "movie" node (each child is a movie entry by ID)
                    for (movieSnapshot in dataSnapshot.children) {
                        // movieSnapshot.key will be the movie ID (like 539972, 762509)
                        // movieSnapshot.value will be the movie details, which is of type MovieDetailsData
                        val movieDetails = movieSnapshot.getValue(MovieDetailsData::class.java)

                        // If movieDetails is not null, add it to the list
                        if (movieDetails != null) {
                            favMovieDetailsList.add(movieDetails)
                        }
                    }
                    // Update the state list
                    favMovieList.clear()
                    favMovieList.addAll(favMovieDetailsList)
                    // Show success message
                }
            } else {
                Toast.makeText(context, "Failed to fetch favorite movies", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    for series
    val favSeriesList = remember { mutableStateListOf<SeriesDetailsOneData?>() }
    // Get reference to the Firebase database
    val favSeriesRef = databaseReference.child("users").child(auth.currentUser?.uid ?: "NA")
        .child("favourite").child("series")

    // Fetch favorite movies
    LaunchedEffect(Unit) {
        favSeriesRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                if (dataSnapshot.exists()) {
                    // List to hold all movie details
                    val favSeriesDetailsList = mutableListOf<SeriesDetailsOneData>()

                    // Loop through all the children under the "movie" node (each child is a movie entry by ID)
                    for (movieSnapshot in dataSnapshot.children) {
                        // movieSnapshot.key will be the movie ID (like 539972, 762509)
                        // movieSnapshot.value will be the movie details, which is of type MovieDetailsData
                        val seriesDetails = movieSnapshot.getValue(SeriesDetailsOneData::class.java)

                        // If movieDetails is not null, add it to the list
                        if (seriesDetails != null) {
                            favSeriesDetailsList.add(seriesDetails)
                        }
                    }
                    // Update the state list
                    favSeriesList.clear()
                    favSeriesList.addAll(favSeriesDetailsList)
                    // Show success message
                }
            } else {
                Toast.makeText(context, "Failed to fetch favorite movies", Toast.LENGTH_SHORT).show()
            }
        }
    }



    Box( modifier = Modifier
        .fillMaxSize()
        .background(Color.Black) ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1f1f1f))
                .verticalScroll(rememberScrollState())
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
                LazyRowMoviesFavouriteWatchListDesign(
                    favMovieList,
                    "Favourite Movies",
                    goToMovieDescScreen
                )
            LazyRowFavouriteWatchlistSeriesDesign(favSeriesList,"Favourite Series", goToSeriesDescScreen)
        }
    }
}

