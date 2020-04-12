package com.mustafa.movieapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.mustafa.movieapp.api.ApiResponse
import com.mustafa.movieapp.api.PeopleService
import com.mustafa.movieapp.mappers.MoviePersonPagingChecker
import com.mustafa.movieapp.mappers.TvPersonPagingChecker
import com.mustafa.movieapp.models.Resource
import com.mustafa.movieapp.models.entity.MoviePerson
import com.mustafa.movieapp.models.entity.MoviePersonResult
import com.mustafa.movieapp.models.entity.TvPerson
import com.mustafa.movieapp.models.entity.TvPersonResult
import com.mustafa.movieapp.models.network.MoviePersonResponse
import com.mustafa.movieapp.models.network.TvPersonResponse
import com.mustafa.movieapp.room.PeopleDao
import com.mustafa.movieapp.utils.AbsentLiveData
import com.mustafa.movieapp.view.ui.common.AppExecutors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonWorksRepository @Inject constructor(
    private val peopleService: PeopleService,
    private val peopleDao: PeopleDao,
    private val appExecutors: AppExecutors
) {

    fun loadMoviesForPerson(personId: Int): LiveData<Resource<List<MoviePerson>>> {
        return object :
            NetworkBoundResource<List<MoviePerson>, MoviePersonResponse, MoviePersonPagingChecker>(
                appExecutors
            ) {
            override fun saveCallResult(items: MoviePersonResponse) {

                val movieIds: List<Int> = items.cast.map { it.id }

                peopleDao.insertMovieForPerson(movies = items.cast)
                val moviePersonResult = MoviePersonResult(
                    moviesIds = movieIds,
                    personId = personId
                )
                peopleDao.insertMoviePersonResult(moviePersonResult)
            }

            override fun shouldFetch(data: List<MoviePerson>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<MoviePerson>> {
                return Transformations.switchMap(peopleDao.getMoviePersonResultByPersonIdLiveData(personId)) { searchData ->
                    if (searchData == null) {
                        AbsentLiveData.create()
                    } else {
                        peopleDao.loadMoviesForPerson(searchData.moviesIds)
                    }
                }
            }

            override fun pageChecker(): MoviePersonPagingChecker {
                return MoviePersonPagingChecker()
            }

            override fun createCall(): LiveData<ApiResponse<MoviePersonResponse>> {
                return peopleService.fetchPersonMovies(id = personId)
            }
        }.asLiveData()
    }

    fun loadTvsForPerson(personId: Int): LiveData<Resource<List<TvPerson>>> {
        return object :
            NetworkBoundResource<List<TvPerson>, TvPersonResponse, TvPersonPagingChecker>(
                appExecutors
            ) {
            override fun saveCallResult(items: TvPersonResponse) {

                val movieIds: List<Int> = items.cast.map { it.id }

                peopleDao.insertTvForPerson(tvs = items.cast)
                val moviePersonResult = TvPersonResult(
                    tvsIds = movieIds,
                    personId = personId
                )
                peopleDao.insertTvPersonResult(moviePersonResult)
            }

            override fun shouldFetch(data: List<TvPerson>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<TvPerson>> {
                return Transformations.switchMap(peopleDao.getTvPersonResultByPersonIdLiveData(personId)) { searchData ->
                    if (searchData == null) {
                        AbsentLiveData.create()
                    } else {
                        peopleDao.loadTvsForPerson(searchData.tvsIds)
                    }
                }
            }

            override fun pageChecker(): TvPersonPagingChecker {
                return TvPersonPagingChecker()
            }

            override fun createCall(): LiveData<ApiResponse<TvPersonResponse>> {
                return peopleService.fetchPersonTvs(id = personId)
            }
        }.asLiveData()
    }


}