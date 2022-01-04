package com.example.imovie.ui.favorite.tvshow

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imovie.BuildConfig.BASE_IMAGE_URL
import com.example.imovie.data.entity.favorite.FavoriteEntity
import com.example.imovie.databinding.ItemRowMovieTvBinding

class FavoriteTvShowAdapter(private val listener: TvShowItemListener) :
    PagedListAdapter<FavoriteEntity, FavoriteTvShowAdapter.TvShowViewHolder>(DIFF_CALLBACK) {

    interface TvShowItemListener {
        fun onClicked(tvShowId: Int?)
    }

    private val items = ArrayList<FavoriteEntity>()

    fun setItems(items: ArrayList<FavoriteEntity>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowViewHolder {
        val binding: ItemRowMovieTvBinding =
            ItemRowMovieTvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TvShowViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) = holder.bind(items[position])

    inner class TvShowViewHolder(
        private val itemBinding: ItemRowMovieTvBinding,
        private val listener: TvShowItemListener
    ) : RecyclerView.ViewHolder(itemBinding.root),
        View.OnClickListener {

        private var movie: FavoriteEntity? = null

        init {
            itemBinding.root.setOnClickListener(this)
        }

        fun bind(item: FavoriteEntity) {
            this.movie = item
            itemBinding.itemTitle.text = item.title
            itemBinding.itemDate.text = item.releaseDate
            itemBinding.itemMovieRating.text = item.voteAverage.toString()
            Glide.with(itemBinding.root)
                .load(BASE_IMAGE_URL + item.posterPath)
                .into(itemBinding.itemPoster)
        }

        override fun onClick(v: View?) = listener.onClicked(movie?.id)

    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<FavoriteEntity> =
            object : DiffUtil.ItemCallback<FavoriteEntity>() {
                override fun areItemsTheSame(
                    oldFav: FavoriteEntity,
                    newFav: FavoriteEntity
                ): Boolean {
                    return oldFav.title == newFav.title && oldFav.releaseDate == newFav.releaseDate
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldFav: FavoriteEntity,
                    newFav: FavoriteEntity
                ): Boolean {
                    return oldFav == newFav
                }
            }
    }

}
