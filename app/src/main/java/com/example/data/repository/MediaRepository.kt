package com.example.data.repository

import com.example.data.local.EpisodeEntity
import com.example.data.local.MediaDao
import com.example.data.local.MediaItemEntity
import com.example.data.local.WatchHistoryEntity
import com.example.data.local.WatchlistEntity
import com.example.data.sample.SampleData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class MediaRepository(private val mediaDao: MediaDao) {

    val allMedia: Flow<List<MediaItemEntity>> = mediaDao.getAllMedia()
    val featuredMedia: Flow<List<MediaItemEntity>> = mediaDao.getFeaturedMedia()
    val trendingMedia: Flow<List<MediaItemEntity>> = mediaDao.getTrendingMedia()
    val userUploadedMedia: Flow<List<MediaItemEntity>> = mediaDao.getUserUploadedMedia()
    val watchlistItems: Flow<List<MediaItemEntity>> = mediaDao.getWatchlistItems()
    val continueWatchingMedia: Flow<List<MediaItemEntity>> = mediaDao.getContinueWatchingMedia()
    val watchHistory: Flow<List<WatchHistoryEntity>> = mediaDao.getWatchHistory()

    fun getMediaByType(type: String): Flow<List<MediaItemEntity>> = mediaDao.getMediaByType(type)

    fun getMediaById(id: Long): Flow<MediaItemEntity?> = mediaDao.getMediaById(id)

    suspend fun getMediaByIdOnce(id: Long): MediaItemEntity? = mediaDao.getMediaByIdOnce(id)

    fun searchMedia(query: String): Flow<List<MediaItemEntity>> = mediaDao.searchMedia(query)

    fun getEpisodesForSeries(seriesId: Long): Flow<List<EpisodeEntity>> =
        mediaDao.getEpisodesForSeries(seriesId)

    fun getEpisodesBySeason(seriesId: Long, season: Int): Flow<List<EpisodeEntity>> =
        mediaDao.getEpisodesBySeason(seriesId, season)

    fun isInWatchlist(mediaId: Long): Flow<Boolean> = mediaDao.isInWatchlist(mediaId)

    fun getWatchProgress(mediaId: Long): Flow<WatchHistoryEntity?> = mediaDao.getWatchProgress(mediaId)

    suspend fun toggleWatchlist(mediaId: Long) {
        val inList = mediaDao.isInWatchlist(mediaId).firstOrNull() ?: false
        if (inList) {
            mediaDao.removeFromWatchlist(mediaId)
        } else {
            mediaDao.addToWatchlist(WatchlistEntity(mediaId = mediaId))
        }
    }

    suspend fun saveWatchProgress(mediaId: Long, episodeId: Long?, positionMs: Long, totalMs: Long) {
        if (totalMs > 0) {
            mediaDao.saveWatchProgress(
                WatchHistoryEntity(
                    mediaId = mediaId,
                    episodeId = episodeId,
                    lastPositionMs = positionMs,
                    totalDurationMs = totalMs,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun clearHistory(mediaId: Long) {
        mediaDao.clearHistoryForMedia(mediaId)
    }

    /**
     * Upload a new movie or single media item
     */
    suspend fun uploadMovie(item: MediaItemEntity): Long {
        return mediaDao.insertMedia(item.copy(isUploadedByUser = true, type = "MOVIE"))
    }

    /**
     * Upload a new web series with optional initial episodes
     */
    suspend fun uploadSeries(
        seriesItem: MediaItemEntity,
        episodes: List<EpisodeEntity>
    ): Long {
        val seriesId = mediaDao.insertMedia(
            seriesItem.copy(
                isUploadedByUser = true,
                type = "SERIES",
                seasonsCount = episodes.maxOfOrNull { it.seasonNumber } ?: 1
            )
        )
        if (episodes.isNotEmpty()) {
            val episodesWithSeriesId = episodes.map { it.copy(seriesId = seriesId) }
            mediaDao.insertEpisodes(episodesWithSeriesId)
        }
        return seriesId
    }

    /**
     * Add an episode to an existing series
     */
    suspend fun addEpisode(episode: EpisodeEntity): Long {
        return mediaDao.insertEpisode(episode)
    }

    suspend fun deleteMedia(mediaId: Long) {
        mediaDao.deleteMediaById(mediaId)
    }

    suspend fun deleteEpisode(episodeId: Long) {
        mediaDao.deleteEpisodeById(episodeId)
    }

    /**
     * Pre-populate initial sample catalog if database is empty on first launch
     */
    suspend fun ensureInitialData() {
        val count = mediaDao.countMedia()
        if (count == 0) {
            mediaDao.insertMediaList(SampleData.initialMediaItems)
            mediaDao.insertEpisodes(SampleData.sampleEpisodes)
            // Seed sample continue watching entry
            mediaDao.saveWatchProgress(
                WatchHistoryEntity(
                    mediaId = 1,
                    lastPositionMs = 45 * 60 * 1000L,
                    totalDurationMs = 167 * 60 * 1000L
                )
            )
            mediaDao.saveWatchProgress(
                WatchHistoryEntity(
                    mediaId = 2,
                    episodeId = 1,
                    lastPositionMs = 20 * 60 * 1000L,
                    totalDurationMs = 48 * 60 * 1000L
                )
            )
            // Seed sample watchlist items
            mediaDao.addToWatchlist(WatchlistEntity(mediaId = 1))
            mediaDao.addToWatchlist(WatchlistEntity(mediaId = 4))
        }
    }
}
