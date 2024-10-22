package com.akansha.catapp

import androidx.navigation.NavHostController
import com.akansha.catapp.CatAppDestinationArgs.CAT_ID_ARG
import com.akansha.catapp.CatAppScreens.CAT_DETAILS_SCREEN
import com.akansha.catapp.CatAppScreens.CAT_LIST_SCREEN
import com.akansha.catapp.CatAppScreens.CAT_FAVOURITES_SCREEN


private object CatAppScreens {
    const val CAT_LIST_SCREEN = "cats"
    const val CAT_DETAILS_SCREEN = "details"
    const val CAT_FAVOURITES_SCREEN = "favorites"
}

object CatAppDestinationArgs {
    const val CAT_ID_ARG = "catId"
}

object CatAppDestinations {
    const val LIST_ROUTE = CAT_LIST_SCREEN
    const val DETAILS_ROUTE = "$CAT_DETAILS_SCREEN/{$CAT_ID_ARG}"
    const val FAVOURITES_ROUTE = CAT_FAVOURITES_SCREEN
}

class CatAppNavigationActions(
    private val navController: NavHostController,
) {

    fun navigateToDetails(id: String) {
        navController.navigate("$CAT_DETAILS_SCREEN/$id")
    }

    fun navigateToFavouriteList() {
        navController.navigate(CAT_FAVOURITES_SCREEN)
    }
}