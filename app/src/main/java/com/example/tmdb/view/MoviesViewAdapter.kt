package com.example.tmdb.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdb.Common
import com.example.tmdb.R
import com.example.tmdb.api.toTmdbImg
import com.example.tmdb.control.FavoriteController
import com.example.tmdb.model.Movie
import com.facebook.drawee.view.SimpleDraweeView
import com.like.LikeButton
import com.like.OnLikeListener

/**
 * Display data of [com.example.tmdb.model.Movie] to user via a [RecyclerView]
 */
class MoviesViewAdapter(private val dataSet: MutableList<Movie>) :
    RecyclerView.Adapter<MoviesViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textTitle: TextView = view.findViewById(R.id.text_movie_title)
        val textDate: TextView = view.findViewById(R.id.text_movie_date)
        val textVote: TextView = view.findViewById(R.id.text_movie_vote)
        val imgPoster: SimpleDraweeView = view.findViewById(R.id.img_movie_poster)
        val butLike: LikeButton = view.findViewById(R.id.but_movie_like)

        init {
            imgPoster.hierarchy.setPlaceholderImage(Common.EMPTY_POSTER)
            imgPoster.hierarchy.setFailureImage(Common.EMPTY_POSTER)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.movie_grid_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textTitle.text = dataSet[position].title
        viewHolder.textTitle.tag = dataSet[position].id
        viewHolder.textDate.text = dataSet[position].release_date
        viewHolder.textVote.text = dataSet[position].voteAsString
        val url: String = dataSet[position].poster_path?.toTmdbImg(400) ?: ""
        viewHolder.imgPoster.setImageURI(url)
        viewHolder.butLike.isLiked = FavoriteController.isExist(dataSet[position])
        viewHolder.butLike.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton) {
                FavoriteController.add(dataSet[position])
            }

            override fun unLiked(likeButton: LikeButton) {
                FavoriteController.remove(dataSet[position])
            }
        })
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}
