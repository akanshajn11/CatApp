package com.akansha.catapp.data

import com.akansha.catapp.data.source.local.Breed
import com.akansha.catapp.data.source.local.Cat
import com.akansha.catapp.data.source.local.CatWithBreed
import com.akansha.catapp.data.source.network.NetworkBreed
import com.akansha.catapp.data.source.network.NetworkCat


fun List<NetworkCat>.mapToCatWithBreed(): List<CatWithBreed> =
    this.map { networkCat ->
        CatWithBreed(
            cat = Cat(
                id = networkCat.id,
                imageUrl = networkCat.url,
                breedId = networkCat.breeds?.first()?.id
            ),
            breed = networkCat.breeds?.first()?.mapToBreed()
        )
    }


fun NetworkBreed.mapToBreed(): Breed =
    Breed(
        breedId = id,
        name = name,
        temperament = temperament,
        description = description,
    )