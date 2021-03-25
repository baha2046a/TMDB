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


class MoviesAdapter(private val dataSet: MutableList<Movie>) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textTitle: TextView
        val textDate: TextView
        val imgPoster: SimpleDraweeView
        val butLike: LikeButton

        init {
            // Define click listener for the ViewHolder's View.
            textTitle = view.findViewById(R.id.textTitle)
            textDate = view.findViewById(R.id.textDate)
            imgPoster = view.findViewById(R.id.img_poster)
            imgPoster.hierarchy.setPlaceholderImage(Common.EMPTY_POSTER)
            butLike = view.findViewById(R.id.but_like)
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
