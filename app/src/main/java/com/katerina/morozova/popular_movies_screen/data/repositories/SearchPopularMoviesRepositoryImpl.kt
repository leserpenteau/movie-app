package com.katerina.morozova.popular_movies_screen.data.repositories

import com.katerina.morozova.core.api.MovieApiService
import com.katerina.morozova.core.models.MovieModel
import com.katerina.morozova.core.room.daos.MovieDao
import com.katerina.morozova.core.utils.responses.StatusResponse
import com.katerina.morozova.popular_movies_screen.domain.repositories.SearchPopularMoviesRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchPopularMoviesRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService,
    private val movieDao: MovieDao
) : SearchPopularMoviesRepository {

    override suspend fun getSearchedMovies(keyword: String) = flow {
        emit(StatusResponse.Loading(true))
        val allMovies = mutableListOf<MovieModel>()
        val maxPage = 3

        for (i in 1..maxPage) {
            val response = apiService.getSearchedMovies(keyword, i)
            allMovies.addAll(response.films.onEach {
                it.isFavorite = isFavourite(it.filmId)
            })
        }
        emit(StatusResponse.Success(allMovies.toList()))
    }.catch { e ->
        emit(StatusResponse.Failure(e.message ?: "Unknown error has occurred"))
    }

    override suspend fun isFavourite(movieId: Int): Boolean {
        return movieDao.isFavourite(movieId)
    }

}