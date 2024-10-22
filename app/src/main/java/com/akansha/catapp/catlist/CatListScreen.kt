package com.akansha.catapp.catlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.akansha.catapp.R
import com.akansha.catapp.data.source.local.Breed
import com.akansha.catapp.data.source.local.Cat
import com.akansha.catapp.data.source.local.CatWithBreed
import com.akansha.catapp.util.CatListTopAppBar
import com.akansha.catapp.util.EmptyView
import com.akansha.catapp.util.LoadingView


@Composable
fun CatListScreen(
    viewModel: CatListViewModel = hiltViewModel<CatListViewModel>(),
    onItemClick: (String) -> Unit,
    onOpenFavouritesScreen: () -> Unit,
) {
    Scaffold(
        topBar = {
            CatListTopAppBar(
                onTapClearLocalStorage = { viewModel.clearLocalStorage() },
                onTapFavouriteMenuItems = { onOpenFavouritesScreen() },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            val listState = rememberLazyListState()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val favouriteCats by viewModel.favoriteCats.collectAsStateWithLifecycle()
            val isLoadingMore by viewModel.isLoadingNextPage.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.fetchNextPage()
            }

            when (uiState) {
                is CatListUIState.Loading -> LoadingView()

                is CatListUIState.Empty -> EmptyView(message = stringResource(R.string.empty_screen_message))

                is CatListUIState.Success -> {
                    CatList(
                        listState = listState,
                        isLoadingMore = isLoadingMore,
                        onItemClick = onItemClick,
                        onFavouriteToggle = { viewModel.toggleFavoriteCat(it) },
                        scrolledToEnd = { viewModel.fetchNextPage() },
                        cats = (uiState as CatListUIState.Success).cats,
                        favouriteCats = favouriteCats
                    )
                }
            }
        }
    }
}

@Composable
private fun CatList(
    listState: LazyListState,
    isLoadingMore: Boolean,
    onItemClick: (String) -> Unit,
    onFavouriteToggle: (String) -> Unit,
    scrolledToEnd: () -> Unit,
    cats: List<CatWithBreed>,
    favouriteCats: Set<String>,
) {
    val isEndOfListReached by remember {
        derivedStateOf {
            val lastVisibleItemIndex =
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            lastVisibleItemIndex == listState.layoutInfo.totalItemsCount - 1
        }
    }
    val loadingMoreDescription = stringResource(R.string.loading_more)

    LaunchedEffect(isEndOfListReached) {
        if (isEndOfListReached) {
            scrolledToEnd()
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .padding(all = dimensionResource(id = R.dimen.screen_content_padding))
            .fillMaxSize()
            .testTag(stringResource(R.string.list_tag))
    ) {

        items(cats) { catWithBreed ->
            CatItem(
                imageUrl = catWithBreed.cat.imageUrl,
                isFavourite = favouriteCats.contains(catWithBreed.cat.id),
                breedName = catWithBreed.breed?.name ?: "",
                onItemClick = { onItemClick(catWithBreed.cat.id) },
                onFavouriteToggle = { onFavouriteToggle(catWithBreed.cat.id) }
            )
        }
        if (isLoadingMore) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .semantics { contentDescription = loadingMoreDescription },
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCatList() {
    val mockCatList = List(10) { it ->
        CatWithBreed(
            cat = Cat(
                id = "$it",
                imageUrl = "",
                breedId = "breed${it}"
            ),
            breed = Breed(
                breedId = "breed${it}",
                name = "Breed $it",
                temperament = "",
                description = ""
            )
        )
    }
    val mockFavIds = setOf("1", "3")
    val listState = rememberLazyListState()

    CatList(
        listState = listState,
        isLoadingMore = false,
        onItemClick = {},
        onFavouriteToggle = {},
        scrolledToEnd = {},
        cats = mockCatList,
        favouriteCats = mockFavIds,
    )
}
