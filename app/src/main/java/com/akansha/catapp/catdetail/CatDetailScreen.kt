package com.akansha.catapp.catdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.akansha.catapp.R
import com.akansha.catapp.data.source.local.Breed
import com.akansha.catapp.data.source.local.Cat
import com.akansha.catapp.data.source.local.CatWithBreed
import com.akansha.catapp.util.CatDetailTopAppBar
import com.akansha.catapp.util.LoadingView


@Composable
fun CatDetailScreen(
    id: String,
    onBack: () -> Unit,
    viewModel: CatDetailViewModel = hiltViewModel<CatDetailViewModel>(),
) {
    Scaffold(topBar = { CatDetailTopAppBar(onBack = onBack) }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            val uiState: CatDetailUIState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.fetchCatDetails(id)
            }

            if (uiState.isLoading) {
                LoadingView()
            } else {
                DetailView(uiState.catDetail)
            }
        }
    }
}

@Composable
fun DetailView(catDetail: CatWithBreed?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.screen_content_padding)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            placeholder = painterResource(R.drawable.placeholder_cat),
            model = catDetail?.cat?.imageUrl,
            error = painterResource(R.drawable.placeholder_cat),
            contentDescription = stringResource(R.string.cat_image_description),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.vertical_margin)))
        Text(
            text = catDetail?.breed?.name ?: "",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag(stringResource(R.string.breed_name_tag))
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.vertical_margin)))
        Text(
            text = catDetail?.breed?.temperament ?: "",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag(stringResource(R.string.breed_temperament_tag))
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.vertical_margin)))
        Text(
            text = catDetail?.breed?.description ?: "",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.testTag(stringResource(R.string.breed_description_tag))
        )
    }
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.list_item_padding)))
}


@Preview(showBackground = true)
@Composable
fun PreviewDetailView() {
    val mockCatDetail = CatWithBreed(
        cat = Cat(
            id = "catId",
            imageUrl = "mockUrl",
            breedId = "breedId",
        ),
        breed = Breed(
            breedId = "breedId",
            name = "Siamese",
            temperament = "Playful, Social",
            description = "The Siamese is a strong, active, and playful cat."
        )
    )
    DetailView(catDetail = mockCatDetail)
}
