package com.akansha.catapp.util

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.akansha.catapp.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatListTopAppBar(
    onTapClearLocalStorage: () -> Unit,
    onTapFavouriteMenuItems: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(text = stringResource(id = R.string.cat_list_title)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        modifier = Modifier.fillMaxWidth(),
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(id = R.string.menu_items)
                )
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.favourite_cats_menu_item)) },
                    onClick = {
                        expanded = false
                        onTapFavouriteMenuItems()
                    })
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.clear_cache_reload_menu_item)) },
                    onClick = {
                        expanded = false
                        onTapClearLocalStorage()
                    })
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatDetailTopAppBar(onBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.detail_screen_title))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_icon_description)
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesListTopAppBar(onBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.favourite_cat_list))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_icon_description)
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}