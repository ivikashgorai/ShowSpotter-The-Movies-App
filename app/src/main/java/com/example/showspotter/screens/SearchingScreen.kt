package com.example.showspotter.screens

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.showspotter.R
import com.example.showspotter.designs.LazyRowMoviesDesign
import com.example.showspotter.designs.LazyRowSeriesDesign
import com.example.showspotter.tmdbapidataclass.Movie.PopularTopRatedTrendingOnTheAirMoviesData
import com.example.showspotter.tmdbapidataclass.Series.PopularTopRatedTrendingOnTheAirSeriesData
import com.example.showspotter.viewmodels.TMDBViewModel

@Composable
fun SearchingScreen(
    TMDBViewModel: TMDBViewModel,
    goToMovieDescScreen: (id: Int) -> Unit,
    goToSeriesDescScreen: (id: Int) -> Unit,
    goToProfileScreen:()->Unit
) {
    var text by remember { mutableStateOf("") }
    var done by remember {
        mutableStateOf(false)
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
                .verticalScroll(rememberScrollState())
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 45.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "notification",
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .size(35.dp)
                        .background(Color(0xFF121212), shape = CircleShape)
                        .padding(8.dp)
                        .clip(CircleShape)
                        .clickable(onClick = {
                            goToProfileScreen()
                        })
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.appicon),
                        contentDescription = "appicon",
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        text = "ShowSpotter",
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
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Search",fontFamily= FontFamily(Font(R.font.interfont)),fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .padding(top = 10.dp, start = 10.dp, end = 5.dp),
                    shape = RoundedCornerShape(10.dp),
                 colors = OutlinedTextFieldDefaults.colors(
                     focusedTextColor = Color.White,
                     unfocusedTextColor = Color.White
                 ),
                    maxLines = 3,
                )
                    Button(
                        onClick = {
                            TMDBViewModel.getSearchedMovie(text)
                            TMDBViewModel.getSearchedSeries(text)
                            done = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp, end = 5.dp)
                            .height(60.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = Color(0xFF0B2B50)
                        )
                    ) {
                        Text(
                            "Search",
                            fontFamily = FontFamily(Font(R.font.interfont)),
                            fontSize = 11.sp
                        )
                    }
                }
            if(done) {
                val searchedMovie: PopularTopRatedTrendingOnTheAirMoviesData? = TMDBViewModel.getSearchedMovie.collectAsState().value
                val searchedSeries: PopularTopRatedTrendingOnTheAirSeriesData? = TMDBViewModel.getSearchedSeries.collectAsState().value
                if(searchedMovie?.results?.size !=0) {
                    LazyRowMoviesDesign(movies = searchedMovie, "Movies", goToMovieDescScreen)
                }
                if(searchedSeries?.results?.size !=0) {
                    LazyRowSeriesDesign(searchedSeries, "Series", goToSeriesDescScreen)
                    Spacer(modifier = Modifier.padding(top = 100.dp))
                }
            }
            }
        }
}

