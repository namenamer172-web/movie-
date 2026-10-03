package com.example.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "media_items")
data class MediaItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val type: String, // "MOVIE", "SERIES", "SPORTS", "SHORT"
    val genre: String, // "Action", "Thriller", "Drama", "Cricket", "Comedy", "Sci-Fi"
    val language: String = "Hindi",
    val releaseYear: Int = 2024,
    val durationMinutes: Int = 120,
    val ageRating: String = "U/A 16+",
    val rating: Double = 4.7,
    val posterUrl: String = "",
    val bannerUrl: String = "",
    val videoUri: String = "",
    val isTrending: Boolean = false,
    val isFeatured: Boolean = false,
    val isOriginal: Boolean = false,
    val isUploadedByUser: Boolean = false,
    val seasonsCount: Int = 1,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "episodes",
    foreignKeys = [
        ForeignKey(
            entity = MediaItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["seriesId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["seriesId"])]
)
data class EpisodeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val seriesId: Long,
    val seasonNumber: Int = 1,
    val episodeNumber: Int,
    val title: String,
    val description: String = "",
    val durationMinutes: Int = 45,
    val videoUri: String = "",
    val thumbnailUrl: String = ""
)

@Entity(tableName = "watch_history")
data class WatchHistoryEntity(
    @PrimaryKey
    val mediaId: Long,
    val episodeId: Long? = null,
    val lastPositionMs: Long = 0L,
    val totalDurationMs: Long = 1L,
    val updatedAt: Long = System.currentTimeMillis()
) {
    val progressFraction: Float
        get() = if (totalDurationMs > 0) (lastPositionMs.toFloat() / totalDurationMs.toFloat()).coerceIn(0f, 1f) else 0f
}

@Entity(tableName = "watchlist")
data class WatchlistEntity(
    @PrimaryKey
    val mediaId: Long,
    val addedAt: Long = System.currentTimeMillis()
)
