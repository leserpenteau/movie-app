package com.katerina.morozova.core.models

data class MovieDescriptionModel(
    val filmId: Int,
    val nameRu: String,
    val posterUrl: String,
    val year: String,
    val description: String,
    val countries: List<CountryModel>,
    val genres: List<GenreModel>
)