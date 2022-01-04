package com.example.imovie.ui.favorite.movie

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imovie.databinding.FragmentFavoriteMovieBinding
import com.example.imovie.ui.movie.detail.MovieDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteMovieFragment : Fragment(), FavoriteMovieAdapter.MovieItemListener {
    lateinit var binding: FragmentFavoriteMovieBinding
    private lateinit var adapter: FavoriteMovieAdapter
    private val viewModel: FavoriteMovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        getFavoriteMovies()
    }

    private fun setupRecyclerView() {
        adapter = FavoriteMovieAdapter(this)
        with(binding) {
            rvMovie.layoutManager = LinearLayoutManager(requireContext())
            rvMovie.adapter = adapter
        }
    }

    private fun getFavoriteMovies() {
        viewModel.getFavoriteMovie().observe(viewLifecycleOwner, { movies ->
            binding.progressBar.visibility = View.GONE
            val item = movies.filter { it.category == "MOVIE" }
            if (item.isNotEmpty()) {
                binding.imgEmpty.visibility = View.INVISIBLE
                binding.txtNoFavorite.visibility = View.INVISIBLE
                adapter.setItems(ArrayList(item))
            } else {
                binding.imgEmpty.visibility = View.VISIBLE
                binding.txtNoFavorite.visibility = View.VISIBLE
            }

        })
    }


    override fun onClicked(movieId: Int?) {
        val intent = Intent(requireContext(), MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.ID, movieId)
        startActivity(intent)
    }
}