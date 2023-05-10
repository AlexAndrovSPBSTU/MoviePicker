package com.example.moviepicker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.moviepicker.data.model.top.movie.TopItem
import com.example.moviepicker.data.api.*
import com.example.moviepicker.data.repository.MovieDataSource
import com.example.moviepicker.data.repository.MovieDataSourceFactory
import com.example.moviepicker.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository(private val apiService: KPApiService) {

    lateinit var filmPagedList: LiveData<PagedList<TopItem>>
    lateinit var movieDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<TopItem>> {
        movieDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        filmPagedList = LivePagedListBuilder(movieDataSourceFactory, config).build()
        return filmPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            movieDataSourceFactory.filmLiveDataSource, MovieDataSource::networkState
        )
    }
}