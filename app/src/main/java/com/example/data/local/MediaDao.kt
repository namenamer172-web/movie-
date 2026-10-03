package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {

    @Query("SELECT * FROM media_items ORDER BY createdAt DESC")
    fun getAllMedia(): Flow<List<MediaItemEntity>>

    @Query("SELECT * FROM media_items WHERE type = :type ORDER BY createdAt DESC")
    fun getMediaByType(type: String): Flow<List<MediaItemEntity>>

    @Query("SELECT * FROM media_items WHERE isFeatured = 1 ORDER BY createdAt DESC")
    fun getFeaturedMedia(): Flow<List<MediaItemEntity>>

    @Query("SELECT * FROM media_items WHERE isTrending = 1 ORDER BY rating DESC")
    fun getTrendingMedia(): Flow<List<MediaItemEntity>>

    @Query("SELECT * FROM media_items WHERE isUploadedByUser = 1 ORDER BY createdAt DESC")
    fun getUserUploadedMedia(): Flow<List<MediaItemEntity>>

    @Query("SELECT * FROM media_items WHERE id = :id LIMIT 1")
    fun getMediaById(id: Long): Flow<MediaItemEntity?>

    @Query("SELECT * FROM media_items WHERE id = :id LIMIT 1")
    suspend fun getMediaByIdOnce(id: Long): MediaItemEntity?

    @Query("SELECT * FROM media_items WHERE title LIKE '%' || :query || '%' OR genre LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchMedia(query: String): Flow<List<MediaItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedia(item: MediaItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMediaList(items: List<MediaItemEntity>)

    @Update
    suspend fun updateMedia(item: MediaItemEntity)

    @Query("DELETE FROM media_items WHERE id = :id")
    suspend fun deleteMediaById(id: Long)

    // Episodes
    @Query("SELECT * FROM episodes WHERE seriesId = :seriesId ORDER BY seasonNumber ASC, episodeNumber ASC")
    fun getEpisodesForSeries(seriesId: Long): Flow<List<EpisodeEntity>>

    @Query("SELECT * FROM episodes WHERE seriesId = :seriesId AND seasonNumber = :season ORDER BY episodeNumber ASC")
    fun getEpisodesBySeason(seriesId: Long, season: Int): Flow<List<EpisodeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(episode: EpisodeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisodes(episodes: List<EpisodeEntity>)

    @Query("DELETE FROM episodes WHERE id = :episodeId")
    suspend fun deleteEpisodeById(episodeId: Long)

    // Watchlist
    @Query("SELECT * FROM watchlist ORDER BY addedAt DESC")
    fun getWatchlist(): Flow<List<WatchlistEntity>>

    @Query("SELECT m.* FROM media_items m INNER JOIN watchlist w ON m.id = w.mediaId ORDER BY w.addedAt DESC")
    fun getWatchlistItems(): Flow<List<MediaItemEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist WHERE mediaId = :mediaId)")
    fun isInWatchlist(mediaId: Long): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWatchlist(item: WatchlistEntity)

    @Query("DELETE FROM watchlist WHERE mediaId = :mediaId")
    suspend fun removeFromWatchlist(mediaId: Long)

    // Watch History
    @Query("SELECT * FROM watch_history ORDER BY updatedAt DESC")
    fun getWatchHistory(): Flow<List<WatchHistoryEntity>>

    @Query("SELECT m.* FROM media_items m INNER JOIN watch_history h ON m.id = h.mediaId ORDER BY h.updatedAt DESC")
    fun getContinueWatchingMedia(): Flow<List<MediaItemEntity>>

    @Query("SELECT * FROM watch_history WHERE mediaId = :mediaId LIMIT 1")
    fun getWatchProgress(mediaId: Long): Flow<WatchHistoryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWatchProgress(history: WatchHistoryEntity)

    @Query("DELETE FROM watch_history WHERE mediaId = :mediaId")
    suspend fun clearHistoryForMedia(mediaId: Long)

    @Query("SELECT COUNT(*) FROM media_items")
    suspend fun countMedia(): Int
}
