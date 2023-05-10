package com.example.moviepicker.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import api.model.Movie
import api.model.favouriteMovies
import api.model.movie.Film
import api.service.FilmDetailsRepository
import com.example.moviepicker.R
import com.example.moviepicker.data.model.Movie
import com.example.moviepicker.adapters.HorizontalMovieCardAdapter
import com.example.moviepicker.databinding.FragmentProfileBinding
import io.paperdb.Paper
import io.reactivex.disposables.CompositeDisposable


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val adapter = HorizontalMovieCardAdapter()
    private val imageIdList = listOf(
        R.drawable.the_grand_budapest_hotel
    )
    private var index = 0
    fun getFavouriteMovies() {
        var favouriteMoviesId = arrayListOf<Int>()
        try {
            favouriteMoviesId = Paper.book().read("favouriteMovies", favouriteMovies)!!
        } catch (e : Exception) {
            print(e)
        }
//        for (id in favouriteMoviesId) {
//
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater)
        _binding?.apply {
            moviesRecyclerView.layoutManager = LinearLayoutManager(activity)
            moviesRecyclerView.adapter = adapter
            for (i in 1..6) {
                val movie = Movie(imageIdList[index], "Отель «Гранд Будапешт»", "Уэс Андерсон")
                adapter.addItem(movie)
            }
        }
        val statistics = "Добавлено ${adapter.itemCount} фильмов"
        binding.textViewStatistics.setText(statistics)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchResultFragment()
    }
}