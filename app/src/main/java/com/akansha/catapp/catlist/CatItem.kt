package com.akansha.catapp.catlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.akansha.catapp.R


@Composable
fun CatItem(
    imageUrl: String,
    isFavourite: Boolean,
    breedName: String?,
    onItemClick: () -> Unit,
    onFavouriteToggle: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.Companion.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.list_item_height))
            .padding(vertical = dimensionResource(id = R.dimen.spacing_small))
            .clickable { onItemClick() }
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        AsyncImage(
            placeholder = painterResource(R.drawable.placeholder_cat),
            model = imageUrl,
            error = painterResource(R.drawable.placeholder_cat),
            contentDescription = stringResource(R.string.cat_image_description),
            contentScale = ContentScale.Companion.FillBounds,
            modifier = Modifier.width(dimensionResource(id = R.dimen.cat_image_width))
        )
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.horizontal_margin)))
        Text(
            modifier = Modifier.Companion.weight(1f),
            text = breedName ?: stringResource(R.string.unknown_breed),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
        IconButton(
            onClick = { onFavouriteToggle() },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            if (isFavourite) {
                Image(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = stringResource(R.string.remove_favorite_description)
                )
            } else {
                Image(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = stringResource(R.string.add_favourite_description)
                )
            }
        }
    }
}