package com.example.moviepicker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import api.model.top.movie.TopItem
import api.service.repository.NetworkState
import com.bumptech.glide.Glide
import com.example.moviepicker.R

class PopularMoviesPagedListAdapter(val fragment: Fragment) :
    PagedListAdapter<TopItem, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val DATA_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position), holder.itemView.context)
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
        if (viewType == DATA_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.vertical_movie_card, parent, false)
            return MovieItemViewHolder(view, fragment)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            DATA_VIEW_TYPE
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<TopItem>() {
        override fun areItemsTheSame(oldItem: TopItem, newItem: TopItem): Boolean {
            return oldItem.kinopoiskId == newItem.kinopoiskId
        }

        override fun areContentsTheSame(oldItem: TopItem, newItem: TopItem): Boolean {
            return oldItem == newItem
        }

    }

    class MovieItemViewHolder(view: View, val fragment: Fragment) : RecyclerView.ViewHolder(view) {

        fun bind(movie: TopItem?, context: Context) {
            val title : TextView = itemView.findViewById(R.id.movieTitle)
            val year : TextView = itemView.findViewById(R.id.movieYear)
            val poster : ImageView = itemView.findViewById(R.id.moviePoster)

            title.text = movie?.nameRu
            year.text = movie?.year
            Glide.with(fragment).load(movie?.posterUrl).into(poster)

            itemView.setOnClickListener {
                // todo navigate to description fragment + passing movie id
                fragment.findNavController().navigate(R.id.descriptionFragment)

            }
        }
    }

    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
//                itemView.progressBar.visibility = View.VISIBLE
            } else {
                //                itemView.progressBar.visibility = View.VISIBLE
            }
            // todo show message when ENDOFLIST, ERROR;
        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }
}