package com.example.imovie.ui.favorite.tvshow

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imovie.databinding.FragmentFavoriteTvShowBinding
import com.example.imovie.ui.tvshow.detail.TvShowDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteTvShowFragment : Fragment(), FavoriteTvShowAdapter.TvShowItemListener {
    lateinit var binding: FragmentFavoriteTvShowBinding
    private lateinit var adapter: FavoriteTvShowAdapter
    private val viewModel: FavoriteTvShowViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteTvShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        getFavoriteTvShow()
    }

    private fun setupRecyclerView() {
        adapter = FavoriteTvShowAdapter(this)
        with(binding) {
            rvTvShow.layoutManager = LinearLayoutManager(requireContext())
            rvTvShow.adapter = adapter
        }
    }

    private fun getFavoriteTvShow() {
        viewModel.getFavoriteTvShow().observe(viewLifecycleOwner, { tvShow ->
            binding.progressBar.visibility = View.GONE
            val item = tvShow.filter { it.category == "TVSHOW" }
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


    override fun onClicked(tvShowId: Int?) {
        val intent = Intent(requireContext(), TvShowDetailActivity::class.java)
        intent.putExtra(TvShowDetailActivity.ID, tvShowId)
        startActivity(intent)
    }
}