package com.example.tmdb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tmdb.api.toTmdbImg
import com.example.tmdb.databinding.FragmentDetailBinding
import com.example.tmdb.model.MovieDetail
import com.facebook.drawee.view.SimpleDraweeView

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var posterView: SimpleDraweeView
    private lateinit var backDropView: SimpleDraweeView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        posterView = view.findViewById(R.id.img_detail_poster)
        backDropView = view.findViewById(R.id.img_detail_backdrop)
    }

    fun setItem(md: MovieDetail) {
        if (this::binding.isInitialized) {
            binding.movieDetail = md

            val backdrop: String = md.backdrop_path?.toTmdbImg(400) ?: ""
            backDropView.setImageURI(backdrop)
            val poster: String = md.poster_path?.toTmdbImg(400) ?: ""
            posterView.setImageURI(poster)
        }
    }
}