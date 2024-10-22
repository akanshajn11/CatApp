package com.akansha.catapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.akansha.catapp.CatAppDestinationArgs.CAT_ID_ARG
import com.akansha.catapp.catdetail.CatDetailScreen
import com.akansha.catapp.catlist.CatListScreen
import com.akansha.catapp.favouritelist.FavouriteCatList


@Composable
fun CatAppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = CatAppDestinations.LIST_ROUTE,
    navActions: CatAppNavigationActions = remember(navController) {
        CatAppNavigationActions(navController)
    }
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(
            route = CatAppDestinations.LIST_ROUTE
        ) {
            CatListScreen(
                onItemClick = { id -> navActions.navigateToDetails(id) },
                onOpenFavouritesScreen = { navActions.navigateToFavouriteList() }
            )
        }
        composable(
            route = CatAppDestinations.DETAILS_ROUTE
        ) { entry ->
            val catId = entry.arguments?.getString(CAT_ID_ARG)
            CatDetailScreen(catId.toString(), onBack = { navController.popBackStack() })
        }
        composable(
            route = CatAppDestinations.FAVOURITES_ROUTE
        ) {
            FavouriteCatList(
                onItemClick = { id -> navActions.navigateToDetails(id) },
                onBackPressed = { navController.popBackStack() }
            )
        }
    }
}