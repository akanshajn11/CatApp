package com.akansha.catapp.catlist

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.akansha.catapp.R
import com.akansha.catapp.data.source.local.Breed
import com.akansha.catapp.data.source.local.Cat
import com.akansha.catapp.data.source.local.CatWithBreed
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Example to show UI Testing.
 * All test cases and other classes not covered due to time constraints.
 */

@RunWith(AndroidJUnit4::class)
class CatListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel: CatListViewModel = mockk(relaxed = true)
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun whenUiStateIsLoading_displaysLoadingView() {
        // ARRANGE
        every { mockViewModel.uiState } returns MutableStateFlow(CatListUIState.Loading)
        every { mockViewModel.favoriteCats } returns MutableStateFlow(emptySet())
        every { mockViewModel.isLoadingNextPage } returns MutableStateFlow(false)

        // ACT
        composeTestRule.setContent {
            CatListScreen(
                viewModel = mockViewModel,
                onItemClick = {},
                onOpenFavouritesScreen = {},
            )
        }

        // ASSERT
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.loading_description))
            .assertIsDisplayed()
    }

    @Test
    fun whenUiStateIsEmpty_displaysEmptyView() {
        // ARRANGE
        every { mockViewModel.uiState } returns MutableStateFlow(CatListUIState.Empty)
        every { mockViewModel.favoriteCats } returns MutableStateFlow(emptySet())
        every { mockViewModel.isLoadingNextPage } returns MutableStateFlow(false)

        // ACT
        composeTestRule.setContent {
            CatListScreen(
                viewModel = mockViewModel,
                onItemClick = {},
                onOpenFavouritesScreen = {}
            )
        }

        // ASSERT
        composeTestRule.onNodeWithText(context.getString(R.string.empty_screen_message))
            .assertIsDisplayed()
    }

    @Test
    fun whenUiStateIsSuccess_displaysCatList() {
        // ARRANGE
        val catList = mockCatList
        every { mockViewModel.uiState } returns MutableStateFlow(CatListUIState.Success(catList))
        every { mockViewModel.favoriteCats } returns MutableStateFlow(setOf("1"))
        every { mockViewModel.isLoadingNextPage } returns MutableStateFlow(false)

        // ACT
        composeTestRule.setContent {
            CatListScreen(
                viewModel = mockViewModel,
                onItemClick = {},
                onOpenFavouritesScreen = {}
            )
        }

        // ASSERT
        composeTestRule.onNodeWithTag(context.getString(R.string.list_tag)).assertIsDisplayed()
    }

    @Test
    fun scrollsToEndAndShowsLoadingMoreIndicator() {
        // ARRANGE
        val catList = mockCatList
        every { mockViewModel.uiState } returns MutableStateFlow(CatListUIState.Success(catList))
        every { mockViewModel.favoriteCats } returns MutableStateFlow(emptySet())
        every { mockViewModel.isLoadingNextPage } returns MutableStateFlow(true)

        // ACT
        composeTestRule.setContent {
            CatListScreen(
                viewModel = mockViewModel,
                onItemClick = {},
                onOpenFavouritesScreen = {}
            )
        }
        composeTestRule.onNodeWithTag(context.getString(R.string.list_tag))
            .performScrollToIndex(catList.size)

        // ASSERT
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.loading_more))
            .assertIsDisplayed()
    }

    @Test
    fun clickOnCatItem_triggersOnItemClickCallback() {
        // ARRANGE
        val mockSelectedCat = mockCatList.first()
        var clickedCatId: String? = null
        every { mockViewModel.uiState } returns MutableStateFlow(CatListUIState.Success(mockCatList))
        every { mockViewModel.favoriteCats } returns MutableStateFlow(emptySet())
        every { mockViewModel.isLoadingNextPage } returns MutableStateFlow(false)

        // ACT
        composeTestRule.setContent {
            CatListScreen(
                viewModel = mockViewModel,
                onItemClick = { clickedCatId = it },
                onOpenFavouritesScreen = {}
            )
        }
        composeTestRule.onNodeWithText(mockCatList.first().breed?.name.toString()).performClick()

        // ASSERT
        assertEquals(mockSelectedCat.cat.id, clickedCatId)
    }
}

private val mockCatList = List(20) { it ->
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