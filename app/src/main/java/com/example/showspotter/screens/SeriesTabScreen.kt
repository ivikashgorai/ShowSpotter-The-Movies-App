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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.navigation.NavController
import com.example.showspotter.R
import com.example.showspotter.designs.BottomNavigatorDesign
import com.example.showspotter.designs.LazyRowSeriesDesign
import com.example.showspotter.viewmodels.TMDBViewModel

@Composable
fun SeriesTabScreen(
    navController: NavController,
    TMDBViewModel: TMDBViewModel,
    goToHomeScreen: () -> Unit,
    goToMovieTabScreen: () -> Unit,
    goToSeriesTabScreen: () -> Unit,
    goToSeriesDescScreen: (id: Int) -> Unit,
    goToSearchScreen:()->Unit,
    goToProfileScreen:()->Unit,
    goToSettingTabScreen:()->Unit
) {
    var timeWindow by remember{
        mutableStateOf("week")
    }
    if(TMDBViewModel.getPopularSeries.collectAsState().value==null) {
        TMDBViewModel.getPopularMovies()
    }

    if(TMDBViewModel.getTopRatedSeries.collectAsState().value==null) {
        TMDBViewModel.getTopRatedSeries()
    }
    if(TMDBViewModel.getTrendingSeries.collectAsState().value==null){
        TMDBViewModel.getTrendingSeries(timeWindow)
    }
    if(TMDBViewModel.getOnTheAirSeries.collectAsState().value==null){
        TMDBViewModel.getOnTheAirSeries()
    }
    val popularSeries = TMDBViewModel.getPopularSeries.collectAsState().value
    val topRatedSeries = TMDBViewModel.getTopRatedSeries.collectAsState().value
    val nowPlayingSeries = TMDBViewModel.getTrendingSeries.collectAsState().value
    val onTheAirSeries = TMDBViewModel.getOnTheAirSeries.collectAsState().value

    Box( modifier = Modifier
        .fillMaxSize()
        .background(Color.Black) ) {
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
                        .clickable(
                            onClick = {
                                goToSearchScreen()
                            }
                        )
                )
            }
            // movies list

            if (topRatedSeries== null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyRowSeriesDesign(topRatedSeries, "Top Rated", goToSeriesDescScreen)
            }
            if (onTheAirSeries == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyRowSeriesDesign(onTheAirSeries, "On The Air", goToSeriesDescScreen)
            }

            if (popularSeries == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyRowSeriesDesign(popularSeries, "Popular Movies", goToSeriesDescScreen)
            }

            if (nowPlayingSeries == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyRowSeriesDesign(nowPlayingSeries, "Upcoming Movies", goToSeriesDescScreen)
                Spacer(modifier = Modifier.padding(bottom = 100.dp))
            }


        }
    }
    BottomNavigatorDesign(navController,goToHomeScreen, goToMovieTabScreen, goToSeriesTabScreen,goToSettingTabScreen)
}