package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.EpisodeEntity
import com.example.data.local.MediaItemEntity
import com.example.data.local.WatchHistoryEntity
import com.example.data.repository.MediaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ContinueWatchingItem(
    val media: MediaItemEntity,
    val history: WatchHistoryEntity
)

class MediaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MediaRepository

    init {
        val db = AppDatabase.getInstance(application)
        repository = MediaRepository(db.mediaDao())
        viewModelScope.launch {
            repository.ensureInitialData()
        }
    }

    val allMedia: StateFlow<List<MediaItemEntity>> = repository.allMedia
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val featuredMedia: StateFlow<List<MediaItemEntity>> = repository.featuredMedia
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trendingMedia: StateFlow<List<MediaItemEntity>> = repository.trendingMedia
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val movies: StateFlow<List<MediaItemEntity>> = repository.getMediaByType("MOVIE")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val series: StateFlow<List<MediaItemEntity>> = repository.getMediaByType("SERIES")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val sports: StateFlow<List<MediaItemEntity>> = repository.getMediaByType("SPORTS")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userUploads: StateFlow<List<MediaItemEntity>> = repository.userUploadedMedia
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val watchlistItems: StateFlow<List<MediaItemEntity>> = repository.watchlistItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Combine media items with watch history for accurate continue watching progress
    val continueWatching: StateFlow<List<ContinueWatchingItem>> = combine(
        repository.allMedia,
        repository.watchHistory
    ) { allItems, histories ->
        histories.mapNotNull { hist ->
            val media = allItems.find { it.id == hist.mediaId }
            if (media != null) ContinueWatchingItem(media, hist) else null
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search query state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults: StateFlow<List<MediaItemEntity>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) flowOf(emptyList())
            else repository.searchMedia(query.trim())
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Category filter for home page
    private val _selectedHomeCategory = MutableStateFlow("All")
    val selectedHomeCategory: StateFlow<String> = _selectedHomeCategory.asStateFlow()

    fun setHomeCategory(category: String) {
        _selectedHomeCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleWatchlist(mediaId: Long) {
        viewModelScope.launch {
            repository.toggleWatchlist(mediaId)
        }
    }

    fun isInWatchlist(mediaId: Long): StateFlow<Boolean> {
        return repository.isInWatchlist(mediaId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    }

    fun getEpisodesForSeries(seriesId: Long): StateFlow<List<EpisodeEntity>> {
        return repository.getEpisodesForSeries(seriesId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun saveWatchProgress(mediaId: Long, episodeId: Long?, positionMs: Long, totalMs: Long) {
        viewModelScope.launch {
            repository.saveWatchProgress(mediaId, episodeId, positionMs, totalMs)
        }
    }

    fun clearHistory(mediaId: Long) {
        viewModelScope.launch {
            repository.clearHistory(mediaId)
        }
    }

    fun deleteMedia(mediaId: Long) {
        viewModelScope.launch {
            repository.deleteMedia(mediaId)
        }
    }

    // Upload status message
    private val _uploadEvent = MutableStateFlow<String?>(null)
    val uploadEvent: StateFlow<String?> = _uploadEvent.asStateFlow()

    fun clearUploadEvent() {
        _uploadEvent.value = null
    }

    fun uploadMovie(
        title: String,
        description: String,
        genre: String,
        language: String,
        releaseYear: Int,
        durationMinutes: Int,
        ageRating: String,
        videoUri: String,
        posterUri: String
    ) {
        viewModelScope.launch {
            try {
                val movie = MediaItemEntity(
                    title = title.trim(),
                    description = description.trim(),
                    type = "MOVIE",
                    genre = genre.trim().ifEmpty { "Action" },
                    language = language.trim().ifEmpty { "Hindi" },
                    releaseYear = releaseYear,
                    durationMinutes = if (durationMinutes > 0) durationMinutes else 120,
                    ageRating = ageRating,
                    rating = 4.8,
                    posterUrl = posterUri.trim(),
                    bannerUrl = posterUri.trim(),
                    videoUri = videoUri.trim(),
                    isUploadedByUser = true,
                    isTrending = true
                )
                repository.uploadMovie(movie)
                _uploadEvent.value = "Movie \"$title\" uploaded successfully!"
            } catch (e: Exception) {
                _uploadEvent.value = "Error uploading movie: ${e.localizedMessage}"
            }
        }
    }

    fun uploadWebSeries(
        title: String,
        description: String,
        genre: String,
        language: String,
        releaseYear: Int,
        ageRating: String,
        posterUri: String,
        episodesList: List<EpisodeEntity>
    ) {
        viewModelScope.launch {
            try {
                val series = MediaItemEntity(
                    title = title.trim(),
                    description = description.trim(),
                    type = "SERIES",
                    genre = genre.trim().ifEmpty { "Drama" },
                    language = language.trim().ifEmpty { "Hindi" },
                    releaseYear = releaseYear,
                    durationMinutes = 45,
                    ageRating = ageRating,
                    rating = 4.9,
                    posterUrl = posterUri.trim(),
                    bannerUrl = posterUri.trim(),
                    videoUri = episodesList.firstOrNull()?.videoUri ?: "",
                    isUploadedByUser = true,
                    isTrending = true,
                    seasonsCount = episodesList.maxOfOrNull { it.seasonNumber } ?: 1
                )
                repository.uploadSeries(series, episodesList)
                _uploadEvent.value = "Web Series \"$title\" with ${episodesList.size} episodes published!"
            } catch (e: Exception) {
                _uploadEvent.value = "Error uploading web series: ${e.localizedMessage}"
            }
        }
    }

    fun addEpisodeToSeries(
        seriesId: Long,
        seasonNumber: Int,
        episodeNumber: Int,
        title: String,
        description: String,
        durationMinutes: Int,
        videoUri: String,
        thumbnailUrl: String
    ) {
        viewModelScope.launch {
            try {
                repository.addEpisode(
                    EpisodeEntity(
                        seriesId = seriesId,
                        seasonNumber = seasonNumber,
                        episodeNumber = episodeNumber,
                        title = title.trim(),
                        description = description.trim(),
                        durationMinutes = if (durationMinutes > 0) durationMinutes else 45,
                        videoUri = videoUri.trim(),
                        thumbnailUrl = thumbnailUrl.trim()
                    )
                )
                _uploadEvent.value = "Episode \"$title\" added to series!"
            } catch (e: Exception) {
                _uploadEvent.value = "Error adding episode: ${e.localizedMessage}"
            }
        }
    }
}
