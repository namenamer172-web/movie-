package com.example.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.EpisodeEntity
import com.example.data.sample.SampleData
import com.example.ui.components.MediaImage
import com.example.ui.theme.HotstarAccentRed
import com.example.ui.theme.HotstarBlue
import com.example.ui.theme.HotstarGold
import com.example.ui.theme.HotstarNavyDark
import com.example.ui.theme.HotstarSurfaceBorder
import com.example.ui.theme.HotstarSurfaceElevated
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.MediaViewModel

@Composable
fun UploadStudioScreen(
    viewModel: MediaViewModel,
    onNavigateToMedia: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uploadEvent by viewModel.uploadEvent.collectAsStateWithLifecycle()
    val userUploads by viewModel.userUploads.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Upload Movie", "Upload Web Series", "My Uploads (${userUploads.size})")

    LaunchedEffect(uploadEvent) {
        uploadEvent?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearUploadEvent()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HotstarNavyDark)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(HotstarBlue.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = "Studio",
                        tint = HotstarBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "JioHotstar Creator Studio",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    )
                    Text(
                        text = "Upload & stream your own movies and web series",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                    )
                }
            }
        }

        // Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = HotstarNavyDark,
            contentColor = HotstarBlue,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = HotstarBlue
                )
            },
            divider = { HorizontalDivider(color = HotstarSurfaceBorder) }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTab == index) HotstarBlue else TextMuted,
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier.testTag("studio_tab_$index")
                )
            }
        }

        when (selectedTab) {
            0 -> MovieUploadForm(viewModel = viewModel)
            1 -> WebSeriesUploadForm(viewModel = viewModel)
            2 -> MyUploadsList(
                uploads = userUploads,
                onItemClick = onNavigateToMedia,
                onDeleteItem = { viewModel.deleteMedia(it) }
            )
        }
    }
}

@Composable
private fun MovieUploadForm(viewModel: MediaViewModel) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("Action") }
    var language by remember { mutableStateOf("Hindi") }
    var releaseYear by remember { mutableStateOf("2025") }
    var durationMinutes by remember { mutableStateOf("135") }
    var ageRating by remember { mutableStateOf("U/A 16+") }
    var videoUriString by remember { mutableStateOf("") }
    var posterUriString by remember { mutableStateOf("") }

    // Video Picker Launcher (Android Photo/Video Picker - zero permissions needed)
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { videoUriString = it.toString() }
    }

    // Poster Picker Launcher
    val posterPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { posterUriString = it.toString() }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Movie Details",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            )
        }

        item {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Movie Title *") },
                placeholder = { Text("e.g. Fighter: Sky Patrol") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("movie_title_input"),
                colors = hotstarTextFieldColors(),
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Synopsis / Storyline *") },
                placeholder = { Text("Enter movie synopsis...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .testTag("movie_desc_input"),
                colors = hotstarTextFieldColors(),
                maxLines = 4
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = genre,
                    onValueChange = { genre = it },
                    label = { Text("Genre") },
                    placeholder = { Text("Action, Sci-Fi") },
                    modifier = Modifier.weight(1f).testTag("movie_genre_input"),
                    colors = hotstarTextFieldColors(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = language,
                    onValueChange = { language = it },
                    label = { Text("Language") },
                    placeholder = { Text("Hindi, English") },
                    modifier = Modifier.weight(1f).testTag("movie_lang_input"),
                    colors = hotstarTextFieldColors(),
                    singleLine = true
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = releaseYear,
                    onValueChange = { releaseYear = it },
                    label = { Text("Year") },
                    modifier = Modifier.weight(1f).testTag("movie_year_input"),
                    colors = hotstarTextFieldColors(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = durationMinutes,
                    onValueChange = { durationMinutes = it },
                    label = { Text("Duration (mins)") },
                    modifier = Modifier.weight(1f).testTag("movie_duration_input"),
                    colors = hotstarTextFieldColors(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = ageRating,
                    onValueChange = { ageRating = it },
                    label = { Text("Age Rating") },
                    modifier = Modifier.weight(1f).testTag("movie_age_input"),
                    colors = hotstarTextFieldColors(),
                    singleLine = true
                )
            }
        }

        // Video File Section
        item {
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = HotstarSurfaceElevated),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.VideoFile,
                            contentDescription = "Video",
                            tint = HotstarBlue,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Movie Video File *",
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (videoUriString.isNotBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(HotstarNavyDark)
                                .padding(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Ready",
                                tint = HotstarGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = videoUriString,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = TextWhite,
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { videoUriString = "" },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove",
                                    tint = HotstarAccentRed,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                videoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = HotstarBlue),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.weight(1f).testTag("pick_movie_video_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = "Pick Video",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Select Video File", fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = {
                                videoUriString = SampleData.VIDEO_URL_3
                            },
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.weight(1f).testTag("use_sample_video_button")
                        ) {
                            Text("Use Sample Stream", fontSize = 12.sp, color = TextWhite)
                        }
                    }
                }
            }
        }

        // Poster Image Section
        item {
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = HotstarSurfaceElevated),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Poster",
                            tint = HotstarGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Poster Artwork",
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (posterUriString.isNotBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(HotstarNavyDark)
                                .padding(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Ready",
                                tint = HotstarGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = posterUriString,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = TextWhite,
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                posterPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = HotstarSurfaceBorder),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.weight(1f).testTag("pick_movie_poster_button")
                        ) {
                            Text("Pick Cover Image", fontSize = 12.sp, color = TextWhite)
                        }

                        OutlinedButton(
                            onClick = {
                                posterUriString = "hero_brahmastra"
                            },
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.weight(1f).testTag("preset_poster_button")
                        ) {
                            Text("Use Studio Poster", fontSize = 12.sp, color = TextWhite)
                        }
                    }
                }
            }
        }

        // Publish Button
        item {
            Button(
                onClick = {
                    if (title.isBlank()) {
                        return@Button
                    }
                    val finalVideoUri = videoUriString.ifBlank { SampleData.VIDEO_URL_1 }
                    val finalPosterUri = posterUriString.ifBlank { "hero_brahmastra" }
                    viewModel.uploadMovie(
                        title = title,
                        description = description.ifBlank { "Exciting cinematic blockbuster movie streaming on JioHotstar." },
                        genre = genre,
                        language = language,
                        releaseYear = releaseYear.toIntOrNull() ?: 2025,
                        durationMinutes = durationMinutes.toIntOrNull() ?: 120,
                        ageRating = ageRating,
                        videoUri = finalVideoUri,
                        posterUri = finalPosterUri
                    )
                    title = ""
                    description = ""
                    videoUriString = ""
                    posterUriString = ""
                },
                enabled = title.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = HotstarBlue),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("publish_movie_button")
            ) {
                Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Publish Movie to JioHotstar",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
private fun WebSeriesUploadForm(viewModel: MediaViewModel) {
    var seriesTitle by remember { mutableStateOf("") }
    var seriesDescription by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("Spy Thriller") }
    var language by remember { mutableStateOf("Hindi • English") }
    var releaseYear by remember { mutableStateOf("2025") }
    var ageRating by remember { mutableStateOf("U/A 16+") }
    var posterUriString by remember { mutableStateOf("") }

    // List of episodes to be published with series
    val episodes = remember {
        mutableStateListOf(
            EpisodeEntity(
                seriesId = 0,
                seasonNumber = 1,
                episodeNumber = 1,
                title = "Episode 1: Pilot",
                description = "The thrilling opening episode introducing the protagonists and the high-stakes mission.",
                durationMinutes = 45,
                videoUri = SampleData.VIDEO_URL_1,
                thumbnailUrl = "hero_webseries"
            )
        )
    }

    var showAddEpisodeDialog by remember { mutableStateOf(false) }

    // Poster Picker
    val posterPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { posterUriString = it.toString() }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Web Series Details",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            )
        }

        item {
            OutlinedTextField(
                value = seriesTitle,
                onValueChange = { seriesTitle = it },
                label = { Text("Series Title *") },
                placeholder = { Text("e.g. Delhi Underworld") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("series_title_input"),
                colors = hotstarTextFieldColors(),
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = seriesDescription,
                onValueChange = { seriesDescription = it },
                label = { Text("Series Logline / Synopsis *") },
                placeholder = { Text("Enter web series description...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .testTag("series_desc_input"),
                colors = hotstarTextFieldColors(),
                maxLines = 3
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = genre,
                    onValueChange = { genre = it },
                    label = { Text("Genre") },
                    modifier = Modifier.weight(1f),
                    colors = hotstarTextFieldColors(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = language,
                    onValueChange = { language = it },
                    label = { Text("Language") },
                    modifier = Modifier.weight(1f),
                    colors = hotstarTextFieldColors(),
                    singleLine = true
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = releaseYear,
                    onValueChange = { releaseYear = it },
                    label = { Text("Release Year") },
                    modifier = Modifier.weight(1f),
                    colors = hotstarTextFieldColors(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = ageRating,
                    onValueChange = { ageRating = it },
                    label = { Text("Age Rating") },
                    modifier = Modifier.weight(1f),
                    colors = hotstarTextFieldColors(),
                    singleLine = true
                )
            }
        }

        // Poster section
        item {
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = HotstarSurfaceElevated),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Series Banner / Poster",
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            fontSize = 14.sp
                        )
                        Text(
                            text = if (posterUriString.isNotBlank()) "Cover artwork selected" else "Select poster image",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                            onClick = {
                                posterPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = HotstarBlue),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.testTag("pick_series_poster_button")
                        ) {
                            Text("Pick", fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = { posterUriString = "hero_webseries" },
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("Preset", fontSize = 12.sp, color = TextWhite)
                        }
                    }
                }
            }
        }

        // Episodes Header & Add Button
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Episodes (${episodes.size})",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                )

                Button(
                    onClick = { showAddEpisodeDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = HotstarGold),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.testTag("add_episode_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Episode",
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Add Episode",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Added Episodes List
        items(episodes) { ep ->
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = HotstarSurfaceElevated),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(HotstarBlue.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "E${ep.episodeNumber}",
                            fontWeight = FontWeight.Bold,
                            color = HotstarBlue,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = ep.title,
                            fontWeight = FontWeight.SemiBold,
                            color = TextWhite,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Season ${ep.seasonNumber} • ${ep.durationMinutes} mins",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }

                    IconButton(
                        onClick = { episodes.remove(ep) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Episode",
                            tint = HotstarAccentRed.copy(alpha = 0.8f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Add Episode Inline Form
        if (showAddEpisodeDialog) {
            item {
                AddEpisodeCard(
                    nextEpisodeNumber = episodes.size + 1,
                    onAdd = { newEp ->
                        episodes.add(newEp)
                        showAddEpisodeDialog = false
                    },
                    onCancel = { showAddEpisodeDialog = false }
                )
            }
        }

        // Publish Web Series Button
        item {
            Button(
                onClick = {
                    if (seriesTitle.isBlank()) return@Button
                    val finalPosterUri = posterUriString.ifBlank { "hero_webseries" }
                    viewModel.uploadWebSeries(
                        title = seriesTitle,
                        description = seriesDescription.ifBlank { "Binge-worthy original web series streaming on JioHotstar." },
                        genre = genre,
                        language = language,
                        releaseYear = releaseYear.toIntOrNull() ?: 2025,
                        ageRating = ageRating,
                        posterUri = finalPosterUri,
                        episodesList = episodes.toList()
                    )
                    seriesTitle = ""
                    seriesDescription = ""
                    posterUriString = ""
                },
                enabled = seriesTitle.isNotBlank() && episodes.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = HotstarBlue),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("publish_series_button")
            ) {
                Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Publish Web Series (${episodes.size} Episodes)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
private fun AddEpisodeCard(
    nextEpisodeNumber: Int,
    onAdd: (EpisodeEntity) -> Unit,
    onCancel: () -> Unit
) {
    var epTitle by remember { mutableStateOf("Episode $nextEpisodeNumber") }
    var epSeason by remember { mutableStateOf("1") }
    var epNumber by remember { mutableStateOf(nextEpisodeNumber.toString()) }
    var epDuration by remember { mutableStateOf("45") }
    var epVideoUri by remember { mutableStateOf("") }

    val videoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { epVideoUri = it.toString() }
    }

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = HotstarSurfaceElevated),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, HotstarGold.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
            .padding(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "New Episode Details",
                fontWeight = FontWeight.Bold,
                color = HotstarGold,
                fontSize = 14.sp
            )

            OutlinedTextField(
                value = epTitle,
                onValueChange = { epTitle = it },
                label = { Text("Episode Title") },
                modifier = Modifier.fillMaxWidth().testTag("new_ep_title_input"),
                colors = hotstarTextFieldColors(),
                singleLine = true
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = epSeason,
                    onValueChange = { epSeason = it },
                    label = { Text("Season #") },
                    modifier = Modifier.weight(1f),
                    colors = hotstarTextFieldColors(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = epNumber,
                    onValueChange = { epNumber = it },
                    label = { Text("Episode #") },
                    modifier = Modifier.weight(1f),
                    colors = hotstarTextFieldColors(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = epDuration,
                    onValueChange = { epDuration = it },
                    label = { Text("Mins") },
                    modifier = Modifier.weight(1f),
                    colors = hotstarTextFieldColors(),
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        videoPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HotstarBlue),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        if (epVideoUri.isNotBlank()) "Video Selected ✓" else "Pick Video",
                        fontSize = 11.sp
                    )
                }

                OutlinedButton(
                    onClick = { epVideoUri = SampleData.VIDEO_URL_2 },
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Sample Stream", fontSize = 11.sp, color = TextWhite)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(onClick = onCancel, shape = RoundedCornerShape(6.dp)) {
                    Text("Cancel", color = TextMuted)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        onAdd(
                            EpisodeEntity(
                                seriesId = 0,
                                seasonNumber = epSeason.toIntOrNull() ?: 1,
                                episodeNumber = epNumber.toIntOrNull() ?: nextEpisodeNumber,
                                title = epTitle.ifBlank { "Episode $nextEpisodeNumber" },
                                durationMinutes = epDuration.toIntOrNull() ?: 45,
                                videoUri = epVideoUri.ifBlank { SampleData.VIDEO_URL_1 },
                                thumbnailUrl = "hero_webseries"
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HotstarGold),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("Add to Series", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun MyUploadsList(
    uploads: List<com.example.data.local.MediaItemEntity>,
    onItemClick: (Long) -> Unit,
    onDeleteItem: (Long) -> Unit
) {
    if (uploads.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.CloudUpload,
                    contentDescription = null,
                    tint = TextDark,
                    modifier = Modifier.size(56.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "No Uploads Yet",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Switch to 'Upload Movie' or 'Upload Web Series' to publish your own content to JioHotstar!",
                    color = TextMuted,
                    fontSize = 13.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(uploads, key = { it.id }) { item ->
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = HotstarSurfaceElevated),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(item.id) }
                    .testTag("uploaded_item_${item.id}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .width(80.dp)
                            .height(55.dp)
                    ) {
                        MediaImage(
                            urlOrName = if (item.bannerUrl.isNotBlank()) item.bannerUrl else item.posterUrl,
                            contentDescription = item.title,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(HotstarBlue.copy(alpha = 0.2f))
                                    .padding(horizontal = 5.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = item.type,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = HotstarBlue
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${item.releaseYear} • ${item.genre}",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    IconButton(
                        onClick = { onItemClick(item.id) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = HotstarGold
                        )
                    }

                    IconButton(
                        onClick = { onDeleteItem(item.id) },
                        modifier = Modifier.size(36.dp).testTag("delete_upload_${item.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = HotstarAccentRed.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun hotstarTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = TextWhite,
    unfocusedTextColor = TextWhite,
    focusedContainerColor = HotstarSurfaceElevated,
    unfocusedContainerColor = HotstarSurfaceElevated,
    focusedBorderColor = HotstarBlue,
    unfocusedBorderColor = HotstarSurfaceBorder,
    focusedLabelColor = HotstarBlue,
    unfocusedLabelColor = TextMuted
)
