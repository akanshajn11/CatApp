package com.akansha.catapp.favouritelist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.akansha.catapp.R
import com.akansha.catapp.catlist.CatItem
import com.akansha.catapp.data.source.local.CatWithBreed
import com.akansha.catapp.util.EmptyView
import com.akansha.catapp.util.FavouritesListTopAppBar


@Composable
fun FavouriteCatList(
    viewModel: FavouriteCatListViewModel = hiltViewModel<FavouriteCatListViewModel>(),
    onItemClick: (String) -> Unit,
    onBackPressed: () -> Unit,
) {
    Scaffold(topBar = { FavouritesListTopAppBar(onBack = onBackPressed) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            val cats: List<CatWithBreed> = viewModel.uiState.collectAsStateWithLifecycle().value

            if (cats.isEmpty()) {
                EmptyView(message = stringResource(R.string.no_favourites_title))
            } else {
                LazyColumn(modifier = Modifier.padding(dimensionResource(id = R.dimen.screen_content_padding))) {
                    items(cats) { catWithBreed ->
                        CatItem(
                            imageUrl = catWithBreed.cat.imageUrl,
                            isFavourite = true,
                            breedName = catWithBreed.breed?.name ?: "",
                            onItemClick = { onItemClick(catWithBreed.cat.id) },
                            onFavouriteToggle = {
                                viewModel.toggleFavoriteCat(catWithBreed.cat.id)
                            }
                        )
                    }
                }
            }
        }
    }
}