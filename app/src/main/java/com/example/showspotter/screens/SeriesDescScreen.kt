package com.example.showspotter.screens

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.example.showspotter.R
import com.example.showspotter.designs.DotPageIndicator
import com.example.showspotter.designs.UserTemplate
import com.example.showspotter.designs.YouTubePlayerVideos
import com.example.showspotter.designs.YouTubePlayerWithPreview
import com.example.showspotter.tmdbapidataclass.Series.SeriesCreditsOneData
import com.example.showspotter.tmdbapidataclass.Series.SeriesDetailsOneData
import com.example.showspotter.tmdbapidataclass.Series.SeriesVideosOneData
import com.example.showspotter.viewmodels.TMDBViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SeriesDescScreen(databaseReference: DatabaseReference, auth:FirebaseAuth, TMDBViewModel: TMDBViewModel, id: Int, goToBackStack:()->Unit, goToAllVideosScreen:(Int)->Unit) {
    val context = LocalContext.current
    TMDBViewModel.getSeriesDetailsById(id)
    val seriesDetail: SeriesDetailsOneData? = TMDBViewModel.getSeriesDetailsById.collectAsState().value

    TMDBViewModel.getSeriesCreditsById(id)
    val seriesCredits: SeriesCreditsOneData? = TMDBViewModel.getSeriesCreditsById.collectAsState().value

    TMDBViewModel.getSeriesVideosById(id)
    val seriesVideos: SeriesVideosOneData? = TMDBViewModel.getSeriesVideosById.collectAsState().value
//
//    viewModel.getMovieReleaseDatesAndCertificationsById(id)
//    val movieReleaseAndCertification: MovieReleaseDateAndCertification? = viewModel.getMovieReleaseDatesAndCertificationsById.collectAsState().value


    if (seriesDetail != null) {
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
                val trailer = seriesVideos?.results?.filter {
                    it.type == "Trailer" && it.site == "YouTube"
                }

//             Pager state with the size of filtered trailers
                val pagerState = rememberPagerState(pageCount = { trailer?.size ?: 0 })

                // Horizontal Pager
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom=10.dp)
                        .height(200.dp)
                ) {
                    if (trailer?.size !=0) {
                        HorizontalPager(state = pagerState) { page ->
                            // Retrieve the current trailer
                            val currentTrailer = trailer?.get(page)
                            // Display YouTube Thumbnail
                            YouTubePlayerWithPreview(videoId = currentTrailer?.key ?: "NA")
                        }
                        Text(
                            text = "Play Trailer",
                            color = Color.White,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(end = 8.dp, bottom = 5.dp)
                                .align(Alignment.BottomEnd),
                            fontSize = 12.sp,
                            fontFamily = FontFamily(
                                Font(R.font.robotolight),
                            )
                            )
                    }
                    else{
                        Text(
                            text = "No Trailer Available",
                            color = Color.White,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(end = 8.dp, bottom = 5.dp)
                                .align(Alignment.Center),
                            fontSize = 18.sp,
                            fontFamily = FontFamily(
                                Font(R.font.robotomedium),
                            )
                        )
                    }
                }

                DotPageIndicator(
                    totalDots = trailer?.size ?: 0,
                    selectedIndex = pagerState.currentPage
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    text = seriesDetail.name, //movie title
                    color = Color(0xFFE0E0E0),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.interbold)),
                    fontSize = 20.sp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,

                    ) {
                    Text(
                        text = seriesDetail.genres[0].name,
                        color = Color.Gray,
                        fontSize = 13.sp,
                        fontFamily = FontFamily(Font(R.font.interfont))
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF505050))

                    )
//                    val certificate = movieReleaseAndCertification?.results?.filter {
//                        it.iso_3166_1 == "US"
//                    }
//                    if(certificate?.size !=0) {
//                        Text(
//                            text = certificate?.get(0)?.release_dates[0]?.certification ?: "NA",
//                            color = Color.Gray,
//                            fontSize = 13.sp,
//                            fontFamily = FontFamily(Font(R.font.interfont))
//                        )
//                        Spacer(
//                            modifier = Modifier
//                                .padding(horizontal = 3.dp)
//                                .size(5.dp)
//                                .clip(CircleShape)
//                                .background(Color(0xFF505050))
//                        )
//                        Text(
//                            text = certificate?.get(0)?.release_dates[0]?.release_date?.substring(
//                                0,
//                                4
//                            ) ?: "NA",
//                            color = Color.Gray,
//                            fontSize = 13.sp,
//                            fontFamily = FontFamily(Font(R.font.interfont))
//                        )
//                    }
//                    Spacer(
//                        modifier = Modifier
//                            .padding(horizontal = 3.dp)
//                            .size(5.dp)
//                            .clip(CircleShape)
//                            .background(Color(0xFF505050))
//                    )
//                    val hour = movieDetail.runtime / 60
//                    val minute = movieDetail.runtime % 60
//                    Text(
//                        text = "$hour hr $minute min",
//                        color = Color.Gray,
//                        fontSize = 13.sp,
//                        fontFamily = FontFamily(Font(R.font.interfont))
//                    )
                }

                Text( // overview
                    text = seriesDetail.overview,
                    color = Color(0xFFE0E0E0),
                    modifier = Modifier.padding(start = 20.dp, end = 15.dp, top = 5.dp)
                )
                var genre = ""
                seriesDetail.genres.forEach {
                    genre = genre + it.name + ", "
                }
                genre = genre.substring(0, genre.length - 2)

                Text(
                    text = "Full Genre: $genre",
                    color = Color(0xFFE0E0E0),
                    modifier = Modifier.padding(start = 20.dp, end = 15.dp, top = 10.dp)
                )

                if (seriesCredits != null) {
                    var starCast = ""
                    seriesCredits.cast.take(5).forEach {
                        starCast = starCast + it.name + ", "
                    }
                    if(starCast.isNotEmpty()) {
                        starCast = starCast.substring(0, starCast.length - 2)
                        Text( //star cast
                            text = "Starring: $starCast",
                            color = Color(0xFFE0E0E0),
                            modifier = Modifier.padding(start = 20.dp, end = 15.dp, top = 5.dp)
                        )
                    }




                    val director =
                        seriesCredits.crew.filter {
                            it.known_for_department == "Directing"
                        }
                            .map {
                                it.name
                            }


                    if(director.isNotEmpty()) {
                        Text( //Directpr
                            text = "Director: ${director.get(0)}",
                            color = Color(0xFFE0E0E0),
                            modifier = Modifier.padding(start = 20.dp, end = 15.dp, top = 5.dp)
                        )
                    }
                }

                if(auth.currentUser!=null) {
                    Spacer(modifier = Modifier.padding(top=15.dp))
                    UserTemplate(TMDBViewModel,false,id,auth,databaseReference)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF2e2d2d))

                ) {
                    Text(
                        "About Series",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(start = 10.dp, top = 5.dp, bottom = 3.dp)
                            .background(Color(0xFF232323), shape = RoundedCornerShape(10.dp))
                            .padding(8.dp),
                        color = Color(0xFFE0E0E0),
                        fontFamily = FontFamily(Font(R.font.interfont))
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(start = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) { // all website links for movie
                        TMDBViewModel.getMovieLinksById(id)
                        val movieLinks = TMDBViewModel.getMovieLinksById.collectAsState().value
                        Image(
                            painter = painterResource(id = R.drawable.imdb),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(5.dp)
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable(
                                    onClick = {
                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://www.imdb.com/title/${movieLinks?.imdb_id}")
                                        )
                                        context.startActivity(intent)
                                    }
                                ),
                            contentScale = ContentScale.Crop
                        )
                        Image(
                            painter = painterResource(id = R.drawable.facebook),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(5.dp)
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable(
                                    onClick = {
                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://www.facebook.com/${movieLinks?.facebook_id}")
                                        )
                                        context.startActivity(intent)
                                    }
                                ),
                            contentScale = ContentScale.Crop
                        )
                        Image(
                            painter = painterResource(id = R.drawable.twitter),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(5.dp)
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable(
                                    onClick = {
                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://www.twitter.com/${movieLinks?.twitter_id}")
                                        )
                                        context.startActivity(intent)
                                    }
                                ),
                            contentScale = ContentScale.Crop
                        )
                        Image(
                            painter = painterResource(id = R.drawable.instagram),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(5.dp)
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable(
                                    onClick = {
                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://www.instagram.com/${movieLinks?.instagram_id}")
                                        )
                                        context.startActivity(intent)
                                    }
                                ),
                            contentScale = ContentScale.Crop
                        )
                        VerticalDivider(modifier = Modifier)
                        Image(
                            painter = painterResource(id = R.drawable.web),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(5.dp)
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .padding(3.dp)
                                .clickable(
                                    onClick = {
                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://www.google.com/search?q=${seriesDetail.name}")
                                        )
                                        context.startActivity(intent)
                                    }
                                ),
                            contentScale = ContentScale.Crop
                        )
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, top = 10.dp),
                    ) {
                        Text("Status", color = Color(0xFFE0E0E0), modifier = Modifier.weight(0.5f))
                        Text(
                            seriesDetail.status,
                            color = Color(0xFFE0E0E0),
                            modifier = Modifier.weight(0.5f)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, top = 10.dp),
                    ) {
                        Text(
                            "Original\nLanguage",
                            color = Color(0xFFE0E0E0),
                            modifier = Modifier.weight(0.5f)
                        )
                        Text(
                            seriesDetail.original_language,
                            color = Color(0xFFE0E0E0),
                            modifier = Modifier.weight(0.5f)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, top = 10.dp),
                    ) {
                        Text("Type", color = Color(0xFFE0E0E0), modifier = Modifier.weight(0.5f))
                        Text(
                            "$" + seriesDetail.type,
                            color = Color(0xFFE0E0E0),
                            modifier = Modifier.weight(0.5f)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, top = 10.dp, bottom = 12.dp),
                    ) {
                        Text("Number of\nSeasons", color = Color(0xFFE0E0E0), modifier = Modifier.weight(0.5f))
                        Text(
                            "$" + seriesDetail.number_of_seasons.toString(),
                            color = Color(0xFFE0E0E0),
                            modifier = Modifier.weight(0.5f)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, top = 10.dp, bottom = 12.dp),
                    ) {
                        Text("Number of\nEpisodes", color = Color(0xFFE0E0E0), modifier = Modifier.weight(0.5f))
                        Text(
                            "$" + seriesDetail.number_of_episodes.toString(),
                            color = Color(0xFFE0E0E0),
                            modifier = Modifier.weight(0.5f)
                        )
                    }
                }

                //al videos
                if (seriesVideos?.results?.size !=0) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF2e2d2d))

                ) {
                        Text(
                            "Videos",
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(start = 10.dp, top = 5.dp)
                                .background(Color(0xFF232323), shape = RoundedCornerShape(10.dp))
                                .padding(8.dp),
                            color = Color.White,
                        )
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            items(seriesVideos?.results?.take(5) ?: emptyList()) {
                                Column(modifier = Modifier.width(240.dp)) {
                                    YouTubePlayerVideos(it.key)
                                    Text(
                                        it.name, modifier = Modifier
                                            .padding(start = 10.dp, top = 5.dp)
                                            .height(50.dp), color = Color(0xFFE0E0E0),
                                        fontFamily = FontFamily(Font(R.font.interbold))
                                    )
                                    Text(
                                        formatTimestamp(it.published_at), modifier = Modifier
                                            .padding(start = 10.dp, bottom = 5.dp)
                                            .height(20.dp), color = Color(0xFF5077B3)
                                    )
                                }
                            }
                        }
                        Text(
                            "All Videos",
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(start = 10.dp, top = 5.dp, bottom = 10.dp)
                                .border(
                                    width = 2.dp,
                                    color = Color.White,
                                    shape = RoundedCornerShape(30.dp)
                                )
                                .background(Color(0xFF1f1f1f), shape = RoundedCornerShape(30.dp))
                                .padding(10.dp)
                                .clickable(
                                    onClick = {
                                        //show all videos
                                        goToAllVideosScreen(id)
                                    }
                                ),
                            color = Color.White,
                            textAlign = TextAlign.Center,
                        )

                    }
                }
                //cast
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF2e2d2d))

                ) {
                    if (seriesCredits?.cast?.size !=0) {
                        Text(
                            "Top Billed Cast",
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(start = 10.dp, top = 5.dp)
                                .background(Color(0xFF232323), shape = RoundedCornerShape(10.dp))
                                .padding(8.dp),
                            color = Color.White,
                        )
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            items(seriesCredits?.cast?.take(10) ?: emptyList()) {
                                Column(modifier = Modifier
                                    .padding(horizontal = 10.dp).width(100.dp)
                                    .clickable(
                                        onClick = {
                                            //cast
                                        }
                                    )) {
                                    Image(
                                        rememberAsyncImagePainter("https://image.tmdb.org/t/p/w500${it.profile_path}"),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .height(160.dp)
                                            .clip(RoundedCornerShape(20.dp))
                                    )
                                    Text(
                                        it.name,
                                        modifier = Modifier
                                            .padding(start = 10.dp, top = 5.dp)
                                            .height(50.dp), color = Color(0xFFE0E0E0),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        fontFamily = FontFamily(Font(R.font.interbold)),

                                        )
                                    Text(
                                        it.character,
                                        modifier = Modifier
                                            .padding(start = 5.dp, bottom = 5.dp)
                                            .height(30.dp),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        color = Color(0xFF5077B3)
                                    )
                                }
                            }
                        }
                    }
                }
                //more data can be added
            }
        }
    }
}