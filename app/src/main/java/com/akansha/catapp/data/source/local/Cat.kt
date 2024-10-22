package com.akansha.catapp.data.source.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "cats",
    foreignKeys = [
        ForeignKey(
            entity = Breed::class,
            parentColumns = ["breedId"],
            childColumns = ["breedId"],
        )
    ],
)
data class Cat(
    @PrimaryKey val id: String,
    val imageUrl: String,
    val breedId: String?,
)


@Entity(
    tableName = "breeds"
)
data class Breed(
    @PrimaryKey val breedId: String,
    val name: String,
    val temperament: String?,
    val description: String?,
)


data class CatWithBreed(
    @Embedded val cat: Cat,
    @Relation(
        parentColumn = "breedId",
        entityColumn = "breedId"
    )
    val breed: Breed?
)


@Entity(
    tableName = "favourite_cats",
    foreignKeys = [
        ForeignKey(
            entity = Cat::class,
            parentColumns = ["id"],
            childColumns = ["id"],
        )
    ],
)
data class FavouriteCat(
    @PrimaryKey val id: String,
)
