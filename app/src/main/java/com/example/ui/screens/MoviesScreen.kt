package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.CategoryChips
import com.example.ui.components.MediaCard
import com.example.ui.theme.HotstarNavyDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.MediaViewModel

@Composable
fun MoviesScreen(
    viewModel: MediaViewModel,
    onNavigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val movies by viewModel.movies.collectAsStateWithLifecycle()
    var selectedGenre by remember { mutableStateOf("All") }
    val genres = listOf("All", "Action", "Drama", "Comedy", "Thriller", "Sci-Fi", "Superhero")

    val filteredMovies = if (selectedGenre == "All") {
        movies
    } else {
        movies.filter { it.genre.contains(selectedGenre, ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HotstarNavyDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Movies",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            )
            Text(
                text = "Blockbusters in Hindi, English, Tamil & more",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
            )
        }

        CategoryChips(
            categories = genres,
            selectedCategory = selectedGenre,
            onCategorySelected = { selectedGenre = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 110.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredMovies, key = { it.id }) { movie ->
                MediaCard(
                    item = movie,
                    onClick = { onNavigateToDetail(movie.id) }
                )
            }
        }
    }
}
