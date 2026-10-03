package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.MediaItemEntity
import com.example.ui.components.MediaCard
import com.example.ui.components.MediaImage
import com.example.ui.theme.HotstarBlue
import com.example.ui.theme.HotstarGold
import com.example.ui.theme.HotstarNavyDark
import com.example.ui.theme.HotstarSurfaceBorder
import com.example.ui.theme.HotstarSurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.MediaViewModel

@Composable
fun DetailScreen(
    mediaId: Long,
    viewModel: MediaViewModel,
    onBackClick: () -> Unit,
    onPlayClick: (Long, Long?) -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allMedia by viewModel.allMedia.collectAsStateWithLifecycle()
    val mediaItem = allMedia.find { it.id == mediaId }

    val isInWatchlist by viewModel.isInWatchlist(mediaId).collectAsStateWithLifecycle()
    val episodes by viewModel.getEpisodesForSeries(mediaId).collectAsStateWithLifecycle()

    var isDownloaded by remember { mutableStateOf(false) }
    var selectedSeason by remember { mutableIntStateOf(1) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Episodes/Info, 1: More Like This

    if (mediaItem == null) {
        Box(
            modifier = modifier.fillMaxSize().background(HotstarNavyDark),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading...", color = TextWhite)
        }
        return
    }

    val isSeries = mediaItem.type.equals("SERIES", ignoreCase = true)
    val seasons = (1..(mediaItem.seasonsCount.coerceAtLeast(1))).toList()
    val currentSeasonEpisodes = episodes.filter { it.seasonNumber == selectedSeason }
    val moreLikeThis = allMedia.filter { it.id != mediaId && (it.genre.contains(mediaItem.genre.split("/").first().trim(), true) || it.type == mediaItem.type) }.take(6)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(HotstarNavyDark)
            .testTag("detail_screen_column"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Hero Backdrop Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp)
            ) {
                MediaImage(
                    urlOrName = if (mediaItem.bannerUrl.isNotBlank()) mediaItem.bannerUrl else mediaItem.posterUrl,
                    contentDescription = mediaItem.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Top & Bottom Gradients
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.7f),
                                    Color.Transparent,
                                    HotstarNavyDark.copy(alpha = 0.85f),
                                    HotstarNavyDark
                                )
                            )
                        )
                )

                // Top Back Button
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .padding(top = 12.dp, start = 12.dp)
                        .align(Alignment.TopStart)
                        .testTag("detail_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                // Bottom Content Title over banner
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    if (mediaItem.isOriginal) {
                        Text(
                            text = "HOTSTAR SPECIALS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = HotstarGold
                        )
                    }
                    Text(
                        text = mediaItem.title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextWhite
                        )
                    )
                }
            }
        }

        // Meta tags: Year, Duration, Age rating, Languages, 4K HDR
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${mediaItem.releaseYear}",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                    Text("•", color = TextMuted)
                    Text(
                        text = if (isSeries) "${mediaItem.seasonsCount} Seasons" else "${mediaItem.durationMinutes}m",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                    Text("•", color = TextMuted)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color.White.copy(alpha = 0.15f))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = mediaItem.ageRating,
                            color = TextWhite,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text("•", color = TextMuted)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(HotstarBlue.copy(alpha = 0.2f))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "4K HDR",
                            color = HotstarBlue,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${mediaItem.genre} • ${mediaItem.language}",
                    color = TextMuted,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = mediaItem.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextWhite.copy(alpha = 0.9f),
                        lineHeight = 20.sp
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Primary Watch Button
                Button(
                    onClick = {
                        val firstEpId = if (isSeries) episodes.firstOrNull()?.id else null
                        onPlayClick(mediaId, firstEpId)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("detail_play_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Watch",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isSeries) "Watch S1 E1" else "Watch Movie",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action Buttons Row: Watchlist, Download, Share
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // Watchlist Action
                    ActionItemButton(
                        icon = if (isInWatchlist) Icons.Default.Check else Icons.Default.Add,
                        label = if (isInWatchlist) "In Watchlist" else "Watchlist",
                        iconTint = if (isInWatchlist) HotstarGold else Color.White,
                        onClick = { viewModel.toggleWatchlist(mediaId) },
                        testTag = "detail_watchlist_action"
                    )

                    // Download Action
                    ActionItemButton(
                        icon = if (isDownloaded) Icons.Default.Check else Icons.Default.Download,
                        label = if (isDownloaded) "Downloaded" else "Download",
                        iconTint = if (isDownloaded) HotstarBlue else Color.White,
                        onClick = {
                            isDownloaded = !isDownloaded
                            Toast.makeText(
                                context,
                                if (isDownloaded) "Downloaded for offline watching!" else "Download removed",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        testTag = "detail_download_action"
                    )

                    // Share Action
                    ActionItemButton(
                        icon = Icons.Default.Share,
                        label = "Share",
                        iconTint = Color.White,
                        onClick = {
                            Toast.makeText(context, "Link copied to clipboard!", Toast.LENGTH_SHORT).show()
                        },
                        testTag = "detail_share_action"
                    )
                }
            }
        }

        // Section Tabs: "Episodes" (if series) & "More Like This"
        item {
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = HotstarSurfaceBorder)

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
                divider = {}
            ) {
                if (isSeries) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                text = "Episodes",
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTab == 0) HotstarBlue else TextMuted
                            )
                        }
                    )
                }
                Tab(
                    selected = selectedTab == if (isSeries) 1 else 0,
                    onClick = { selectedTab = if (isSeries) 1 else 0 },
                    text = {
                        Text(
                            text = "More Like This",
                            fontWeight = if (selectedTab == if (isSeries) 1 else 0) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTab == if (isSeries) 1 else 0) HotstarBlue else TextMuted
                        )
                    }
                )
            }
        }

        // If Series and Tab == 0: Seasons selector + Episode list
        if (isSeries && selectedTab == 0) {
            // Season Selector Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    seasons.forEach { seasonNum ->
                        val isSeasonSelected = selectedSeason == seasonNum
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSeasonSelected) HotstarBlue else HotstarSurfaceElevated)
                                .clickable { selectedSeason = seasonNum }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Season $seasonNum",
                                color = if (isSeasonSelected) TextWhite else TextMuted,
                                fontWeight = if (isSeasonSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Episode cards
            items(currentSeasonEpisodes, key = { it.id }) { ep ->
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = HotstarSurfaceElevated),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clickable { onPlayClick(mediaId, ep.id) }
                        .testTag("episode_item_${ep.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(90.dp)
                                .height(56.dp)
                                .clip(RoundedCornerShape(6.dp))
                        ) {
                            MediaImage(
                                urlOrName = ep.thumbnailUrl.ifBlank { mediaItem.posterUrl },
                                contentDescription = ep.title,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.6f))
                                    .align(Alignment.Center),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = ep.title,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${ep.durationMinutes}m",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                            if (ep.description.isNotBlank()) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = ep.description,
                                    color = TextMuted,
                                    fontSize = 11.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            if (currentSeasonEpisodes.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No episodes added for this season yet.", color = TextMuted)
                    }
                }
            }
        }

        // More Like This Recommendations
        if ((!isSeries && selectedTab == 0) || (isSeries && selectedTab == 1)) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(moreLikeThis) { rec ->
                        MediaCard(
                            item = rec,
                            onClick = { onNavigateToDetail(rec.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionItemButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    iconTint: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
            .testTag(testTag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = TextMuted,
            fontWeight = FontWeight.Medium
        )
    }
}
