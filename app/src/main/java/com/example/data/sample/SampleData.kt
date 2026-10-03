package com.example.data.sample

import com.example.data.local.EpisodeEntity
import com.example.data.local.MediaItemEntity

object SampleData {

    const val VIDEO_URL_1 = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    const val VIDEO_URL_2 = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
    const val VIDEO_URL_3 = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
    const val VIDEO_URL_4 = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"
    const val VIDEO_URL_5 = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
    const val VIDEO_URL_6 = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"

    val initialMediaItems = listOf(
        MediaItemEntity(
            id = 1,
            title = "Brahmastra: Part One - Shiva",
            description = "A DJ named Shiva discovers his strange connection to the element of fire, and holds the power to awaken the Brahmastra, a supernatural weapon of celestial powers that could destroy the world.",
            type = "MOVIE",
            genre = "Action / Mythological",
            language = "Hindi • Telugu • Tamil",
            releaseYear = 2023,
            durationMinutes = 167,
            ageRating = "U/A 13+",
            rating = 4.8,
            posterUrl = "hero_brahmastra",
            bannerUrl = "hero_brahmastra",
            videoUri = VIDEO_URL_3,
            isTrending = true,
            isFeatured = true,
            isOriginal = true
        ),
        MediaItemEntity(
            id = 2,
            title = "Special Ops: The Mirage",
            description = "Himmat Singh and his elite undercover task force track down an elusive mastermind responsible for synchronized financial warfare and cyber espionage across South Asia.",
            type = "SERIES",
            genre = "Spy Thriller",
            language = "Hindi • English",
            releaseYear = 2024,
            durationMinutes = 48,
            ageRating = "U/A 16+",
            rating = 4.9,
            posterUrl = "hero_webseries",
            bannerUrl = "hero_webseries",
            videoUri = VIDEO_URL_1,
            isTrending = true,
            isFeatured = true,
            isOriginal = true,
            seasonsCount = 2
        ),
        MediaItemEntity(
            id = 3,
            title = "T20 Championship: India vs Australia",
            description = "Live high-voltage ICC T20 Championship Final with multi-cam 4K streaming, ultra-low latency, and live Hindi & English commentary.",
            type = "SPORTS",
            genre = "Cricket",
            language = "Live Hindi • English",
            releaseYear = 2025,
            durationMinutes = 210,
            ageRating = "U",
            rating = 5.0,
            posterUrl = "https://images.unsplash.com/photo-1540747913346-19e32dc3e97e?w=800",
            bannerUrl = "https://images.unsplash.com/photo-1540747913346-19e32dc3e97e?w=1200",
            videoUri = VIDEO_URL_5,
            isTrending = true,
            isFeatured = true,
            isOriginal = false
        ),
        MediaItemEntity(
            id = 4,
            title = "The Night Manager",
            description = "An ex-soldier turned night manager of a luxury Dhaka hotel is recruited by an intelligence officer to infiltrate the inner circle of a ruthless arms dealer masquerading as a philanthropist.",
            type = "SERIES",
            genre = "Suspense Thriller",
            language = "Hindi",
            releaseYear = 2023,
            durationMinutes = 52,
            ageRating = "A 18+",
            rating = 4.8,
            posterUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=800",
            bannerUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1200",
            videoUri = VIDEO_URL_4,
            isTrending = true,
            isFeatured = false,
            isOriginal = true,
            seasonsCount = 2
        ),
        MediaItemEntity(
            id = 5,
            title = "12th Fail: Beyond Limits",
            description = "Inspired by millions of true aspirants, Manoj Kumar Sharma battles extreme poverty and setbacks in Delhi's Chambal to emerge as an iconic IPS officer.",
            type = "MOVIE",
            genre = "Drama / Biography",
            language = "Hindi",
            releaseYear = 2024,
            durationMinutes = 147,
            ageRating = "U/A 13+",
            rating = 4.9,
            posterUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=800",
            bannerUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=1200",
            videoUri = VIDEO_URL_2,
            isTrending = true,
            isFeatured = false,
            isOriginal = false
        ),
        MediaItemEntity(
            id = 6,
            title = "Criminal Justice: Season 3",
            description = "Madhav Mishra returns with his quick wit and unassuming demeanor to defend an unconvincing prime suspect accused of killing a celebrated child celebrity.",
            type = "SERIES",
            genre = "Legal Drama",
            language = "Hindi",
            releaseYear = 2023,
            durationMinutes = 44,
            ageRating = "U/A 16+",
            rating = 4.7,
            posterUrl = "https://images.unsplash.com/photo-1589829545856-d10d557cf95f?w=800",
            bannerUrl = "https://images.unsplash.com/photo-1589829545856-d10d557cf95f?w=1200",
            videoUri = VIDEO_URL_1,
            isTrending = false,
            isFeatured = false,
            isOriginal = true,
            seasonsCount = 3
        ),
        MediaItemEntity(
            id = 7,
            title = "Jawan: Final Strike",
            description = "A high-octane emotional journey of a prison warden who sets out to rectify societal wrongs with a team of fearless women operatives.",
            type = "MOVIE",
            genre = "Action Thriller",
            language = "Hindi • Tamil • Telugu",
            releaseYear = 2023,
            durationMinutes = 170,
            ageRating = "U/A 16+",
            rating = 4.8,
            posterUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=800",
            bannerUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=1200",
            videoUri = VIDEO_URL_6,
            isTrending = true,
            isFeatured = false,
            isOriginal = false
        ),
        MediaItemEntity(
            id = 8,
            title = "Deadpool & Wolverine",
            description = "Wade Wilson's peaceful life is shattered when the TVA pulls him into an existential mission, requiring an alliance with a reluctant Wolverine.",
            type = "MOVIE",
            genre = "Superhero / Comedy",
            language = "English • Hindi",
            releaseYear = 2024,
            durationMinutes = 128,
            ageRating = "A 18+",
            rating = 4.7,
            posterUrl = "https://images.unsplash.com/photo-1568832359672-e36cf5d74f54?w=800",
            bannerUrl = "https://images.unsplash.com/photo-1568832359672-e36cf5d74f54?w=1200",
            videoUri = VIDEO_URL_3,
            isTrending = true,
            isFeatured = false,
            isOriginal = false
        )
    )

    val sampleEpisodes = listOf(
        // Series 2 (Special Ops)
        EpisodeEntity(
            id = 1,
            seriesId = 2,
            seasonNumber = 1,
            episodeNumber = 1,
            title = "Episode 1: The Target in Istanbul",
            description = "Himmat Singh spots an anomalous international fund transfer pointing to an operative thought to have been neutralized a decade ago.",
            durationMinutes = 45,
            videoUri = VIDEO_URL_1,
            thumbnailUrl = "hero_webseries"
        ),
        EpisodeEntity(
            id = 2,
            seriesId = 2,
            seasonNumber = 1,
            episodeNumber = 2,
            title = "Episode 2: Code Black in Dubai",
            description = "Faruq infiltrates a high-society luxury gala in Dubai to clone the encryption keys of a syndicate courier.",
            durationMinutes = 48,
            videoUri = VIDEO_URL_2,
            thumbnailUrl = "hero_webseries"
        ),
        EpisodeEntity(
            id = 3,
            seriesId = 2,
            seasonNumber = 1,
            episodeNumber = 3,
            title = "Episode 3: The Diplomat's Trap",
            description = "Ruhani risks her cover in Jordan to extract intelligence before the border crossing is sealed.",
            durationMinutes = 52,
            videoUri = VIDEO_URL_3,
            thumbnailUrl = "hero_webseries"
        ),
        EpisodeEntity(
            id = 4,
            seriesId = 2,
            seasonNumber = 1,
            episodeNumber = 4,
            title = "Episode 4: Final Extraction",
            description = "A synchronized multi-team assault unfolds across three safehouses as Himmat closes in on the mastermind.",
            durationMinutes = 55,
            videoUri = VIDEO_URL_4,
            thumbnailUrl = "hero_webseries"
        ),
        EpisodeEntity(
            id = 5,
            seriesId = 2,
            seasonNumber = 2,
            episodeNumber = 1,
            title = "Season 2 Premiere: The Baku Protocol",
            description = "A new threat emerges in the Caspian pipeline sector, pulling Himmat Singh back into the operational war room.",
            durationMinutes = 50,
            videoUri = VIDEO_URL_6,
            thumbnailUrl = "hero_webseries"
        ),

        // Series 4 (The Night Manager)
        EpisodeEntity(
            id = 6,
            seriesId = 4,
            seasonNumber = 1,
            episodeNumber = 1,
            title = "Episode 1: Midnight in Dhaka",
            description = "Shaan Sengupta, an unassuming night manager, receives a desperate plea for protection from an arms dealer's captive wife.",
            durationMinutes = 52,
            videoUri = VIDEO_URL_4,
            thumbnailUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=800"
        ),
        EpisodeEntity(
            id = 7,
            seriesId = 4,
            seasonNumber = 1,
            episodeNumber = 2,
            title = "Episode 2: The Island Sanctuary",
            description = "Shaan orchestrates a daring staged rescue of Shelly Rungta's son to earn his trust in Sri Lanka.",
            durationMinutes = 54,
            videoUri = VIDEO_URL_5,
            thumbnailUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=800"
        )
    )
}
