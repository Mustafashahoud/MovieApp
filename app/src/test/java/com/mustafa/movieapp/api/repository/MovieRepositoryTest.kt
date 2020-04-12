package com.mustafa.movieapp.api.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.mustafa.movieapp.api.MovieService
import com.mustafa.movieapp.api.util.ApiUtil.successCall
import com.mustafa.movieapp.api.util.InstantAppExecutors
import com.mustafa.movieapp.models.Keyword
import com.mustafa.movieapp.models.Resource
import com.mustafa.movieapp.models.Review
import com.mustafa.movieapp.models.Video
import com.mustafa.movieapp.models.network.KeywordListResponse
import com.mustafa.movieapp.models.network.ReviewListResponse
import com.mustafa.movieapp.models.network.VideoListResponse
import com.mustafa.movieapp.repository.MovieRepository
import com.mustafa.movieapp.room.AppDatabase
import com.mustafa.movieapp.room.MovieDao
import com.mustafa.movieapp.utils.MockTestUtil.Companion.mockKeywordList
import com.mustafa.movieapp.utils.MockTestUtil.Companion.mockMovie
import com.mustafa.movieapp.utils.MockTestUtil.Companion.mockReviewList
import com.mustafa.movieapp.utils.MockTestUtil.Companion.mockVideoList
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@RunWith(JUnit4::class)
class MovieRepositoryTest {
    private lateinit var repository: MovieRepository
    private val movieDao = mock<MovieDao>()
    private val service = mock<MovieService>()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        val db = mock<AppDatabase>()
        `when`(db.movieDao()).thenReturn(movieDao)
        `when`(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()
        repository = MovieRepository(service, movieDao, InstantAppExecutors())
    }

    @Test
    fun loadKeywordListTest() {
        val loadFromDB = mockMovie()
        whenever(movieDao.getMovie(123)).thenReturn(loadFromDB)

        val mockResponse = KeywordListResponse(123, mockKeywordList())
        val call = successCall(mockResponse)
        whenever(service.fetchKeywords(123)).thenReturn(call)

        val data = repository.loadKeywordList(123)
        verify(movieDao).getMovie(123)
        verifyNoMoreInteractions(service)

        val observer = mock<Observer<Resource<List<Keyword>>>>()
        data.observeForever(observer)
        verify(observer).onChanged(Resource.success(mockKeywordList(), false))

        val updatedMovie = mockMovie()
        updatedMovie.keywords = mockKeywordList()
        verify(movieDao).updateMovie(updatedMovie)
    }

    @Test
    fun loadVideoListTest() {
        val loadFromDB = mockMovie()
        whenever(movieDao.getMovie(123)).thenReturn(loadFromDB)

        val mockResponse = VideoListResponse(123, mockVideoList())
        val call = successCall(mockResponse)
        whenever(service.fetchVideos(123)).thenReturn(call)

        val data = repository.loadVideoList(123)
        verify(movieDao).getMovie(123)
        verifyNoMoreInteractions(service)

        val observer = mock<Observer<Resource<List<Video>>>>()
        data.observeForever(observer)
        verify(observer).onChanged(Resource.success(mockVideoList(), false))

        val updatedMovie = mockMovie()
        updatedMovie.videos = mockVideoList()
        verify(movieDao).updateMovie(updatedMovie)
    }

    @Test
    fun loadReviewListTest() {
        val loadFromDB = mockMovie()
        whenever(movieDao.getMovie(123)).thenReturn(loadFromDB)

        val mockResponse = ReviewListResponse(123, 1, mockReviewList(), 100, 100)
        val call = successCall(mockResponse)
        whenever(service.fetchReviews(123)).thenReturn(call)

        val data = repository.loadReviewsList(123)
        verify(movieDao).getMovie(123)
        verifyNoMoreInteractions(service)

        val observer = mock<Observer<Resource<List<Review>>>>()
        data.observeForever(observer)
        verify(observer).onChanged(Resource.success(mockReviewList(), false))

        val updatedMovie = mockMovie()
        updatedMovie.reviews = mockReviewList()
        verify(movieDao).updateMovie(updatedMovie)
    }
}
