package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.MediaItemEntity
import com.example.ui.components.CategoryChips
import com.example.ui.components.ContinueWatchingRow
import com.example.ui.components.HeroCarousel
import com.example.ui.components.MediaCard
import com.example.ui.components.MediaImage
import com.example.ui.theme.HotstarAccentRed
import com.example.ui.theme.HotstarBlue
import com.example.ui.theme.HotstarGold
import com.example.ui.theme.HotstarNavyDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.MediaViewModel

@Composable
fun HomeScreen(
    viewModel: MediaViewModel,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToPlayer: (Long, Long?) -> Unit,
    onNavigateToSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val featuredList by viewModel.featuredMedia.collectAsStateWithLifecycle()
    val trendingList by viewModel.trendingMedia.collectAsStateWithLifecycle()
    val moviesList by viewModel.movies.collectAsStateWithLifecycle()
    val seriesList by viewModel.series.collectAsStateWithLifecycle()
    val sportsList by viewModel.sports.collectAsStateWithLifecycle()
    val continueWatchingList by viewModel.continueWatching.collectAsStateWithLifecycle()
    val userUploads by viewModel.userUploads.collectAsStateWithLifecycle()
    val watchlistItems by viewModel.watchlistItems.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedHomeCategory.collectAsStateWithLifecycle()

    val categories = listOf("All", "Movies", "Web Series", "Sports", "Originals", "User Uploads")

    // Filter items based on selected category chip
    val filteredMovies = when (selectedCategory) {
        "All", "Movies" -> moviesList
        "User Uploads" -> moviesList.filter { it.isUploadedByUser }
        "Originals" -> moviesList.filter { it.isOriginal }
        else -> emptyList()
    }

    val filteredSeries = when (selectedCategory) {
        "All", "Web Series" -> seriesList
        "User Uploads" -> seriesList.filter { it.isUploadedByUser }
        "Originals" -> seriesList.filter { it.isOriginal }
        else -> emptyList()
    }

    val filteredSports = when (selectedCategory) {
        "All", "Sports" -> sportsList
        else -> emptyList()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(HotstarNavyDark)
            .testTag("home_screen_lazy_column"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // App Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // JioHotstar Logo brand
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.testTag("home_brand_logo")
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0C111B))
                    ) {
                        MediaImage(
                            urlOrName = "ic_jiohotstar_logo",
                            contentDescription = "JioHotstar",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Jio",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = HotstarBlue,
                            letterSpacing = (-0.5).sp
                        )
                    )
                    Text(
                        text = "Hotstar",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            letterSpacing = (-0.5).sp
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(HotstarGold.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "PREMIUM",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = HotstarGold
                        )
                    }
                }

                // Action icons: Cast, Search, Notification
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { /* Cast simulation */ },
                        modifier = Modifier.size(36.dp).testTag("action_cast")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cast,
                            contentDescription = "Cast",
                            tint = Color.White.copy(alpha = 0.85f),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onNavigateToSearch,
                        modifier = Modifier.size(36.dp).testTag("action_search")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    IconButton(
                        onClick = { /* Notification */ },
                        modifier = Modifier.size(36.dp).testTag("action_notifications")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White.copy(alpha = 0.85f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            CategoryChips(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { viewModel.setHomeCategory(it) }
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        // Featured Hero Carousel
        if (selectedCategory == "All" || selectedCategory == "Originals") {
            item {
                HeroCarousel(
                    items = featuredList,
                    onItemClick = { onNavigateToDetail(it.id) },
                    onWatchClick = { onNavigateToPlayer(it.id, null) },
                    onToggleWatchlist = { viewModel.toggleWatchlist(it.id) },
                    isItemInWatchlist = { id -> watchlistItems.any { it.id == id } }
                )
            }
        }

        // Continue Watching Row
        if (continueWatchingList.isNotEmpty() && selectedCategory == "All") {
            item {
                ContinueWatchingRow(
                    items = continueWatchingList,
                    onItemClick = { cw ->
                        onNavigateToPlayer(cw.media.id, cw.history.episodeId)
                    },
                    onDismissItem = { id ->
                        viewModel.clearHistory(id)
                    }
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
        }

        // Top 10 in India Today
        if (trendingList.isNotEmpty() && selectedCategory == "All") {
            item {
                SectionHeader(title = "Top 10 in India Today")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(trendingList.take(10)) { index, item ->
                        MediaCard(
                            item = item,
                            onClick = { onNavigateToDetail(item.id) },
                            rankNumber = index + 1
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Live & Upcoming Sports
        if (filteredSports.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(HotstarAccentRed)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "LIVE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Sports & Live Tournaments",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    )
                }
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredSports) { item ->
                        MediaCard(
                            item = item,
                            onClick = { onNavigateToDetail(item.id) },
                            isWide = true
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // User Uploaded Section (Highlighted!)
        if (userUploads.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(HotstarBlue)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "STUDIO",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Your Uploaded Movies & Series",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    )
                }
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(userUploads) { item ->
                        MediaCard(
                            item = item,
                            onClick = { onNavigateToDetail(item.id) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Binge-worthy Web Series
        if (filteredSeries.isNotEmpty()) {
            item {
                SectionHeader(title = "Binge-worthy Web Series")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredSeries) { item ->
                        MediaCard(
                            item = item,
                            onClick = { onNavigateToDetail(item.id) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Blockbuster Movies
        if (filteredMovies.isNotEmpty()) {
            item {
                SectionHeader(title = "Blockbuster Movies")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredMovies) { item ->
                        MediaCard(
                            item = item,
                            onClick = { onNavigateToDetail(item.id) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = TextWhite
        ),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
