package com.akansha.catapp.catlist

import com.akansha.catapp.MainCoroutineRule
import com.akansha.catapp.data.CatRepository
import com.akansha.catapp.data.source.local.Breed
import com.akansha.catapp.data.source.local.Cat
import com.akansha.catapp.data.source.local.CatWithBreed
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Example to show Unit Testing.
 * All test cases and other classes not covered due to time constraints.
 */

class CatListViewModelTest {

    private lateinit var catListViewModel: CatListViewModel
    private lateinit var catRepository: CatRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        catRepository = mockk<CatRepository>()
        every { catRepository.observeAllFavoriteCatIds() } returns flowOf(emptySet())
        catListViewModel = CatListViewModel(catRepository)
    }

    @Test
    fun `initial state is loading and fetches first page`() = runTest {
        coEvery { catRepository.getCats(any(), any()) } returns mockCatList
        val uiState = catListViewModel.uiState.first()

        assertEquals(uiState, CatListUIState.Loading)

        catListViewModel.fetchNextPage()

        coVerify { catRepository.getCats(limit = 20, page = 1) }
        assert(catListViewModel.uiState.value is CatListUIState.Success)
        val successState = catListViewModel.uiState.value as CatListUIState.Success
        assertEquals(mockCatList, successState.cats)
    }

    @Test
    fun `toggleFavoriteCate should call repository's toggleCatFavourites`() = runTest {
        // ARRANGE
        val mockId = "id"
        coEvery { catRepository.toggleCatFavourites(any()) } just runs

        // ACT
        catListViewModel.toggleFavoriteCat(mockId)

        // ASSERT
        coVerify { catRepository.toggleCatFavourites(mockId) }
    }

    @Test
    fun `clearLocalStorage should reset and fetch new page`() = runTest {
        // ARRANGE
        coEvery { catRepository.getCats(any(), any()) } returns mockCatList
        coEvery { catRepository.deleteLocalStorage() } just runs

        // ACT
        catListViewModel.clearLocalStorage()

        // ASSERT
        coVerify {
            catRepository.deleteLocalStorage()
            catRepository.getCats(any(), any())
        }
    }

    @Test
    fun `fetchNextPage handles empty results and sets state to Empty`() {
        coEvery { catRepository.getCats(any(), any()) } returns emptyList()

        catListViewModel.fetchNextPage()

        coVerify { catRepository.getCats(any(), any()) }
        assert(catListViewModel.uiState.value is CatListUIState.Empty)
    }
}

private val mockCatList = listOf(
    CatWithBreed(
        cat = Cat(
            id = "id",
            imageUrl = "url",
            breedId = "breedId"
        ),
        breed = Breed(
            breedId = "breedId",
            name = "name",
            temperament = "",
            description = ""
        )
    )
)