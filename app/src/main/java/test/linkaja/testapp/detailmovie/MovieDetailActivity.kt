package test.linkaja.testapp.detailmovie

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.coroutines.flow.collect
import test.linkaja.testapp.BuildConfig
import test.linkaja.testapp.R
import test.linkaja.testapp.base.BaseActivity
import test.linkaja.testapp.detailmovie.adapter.DetailGenreAdapter
import test.linkaja.testapp.detailmovie.model.Genre
import test.linkaja.testapp.detailmovie.model.MovieDetailResponse
import test.linkaja.testapp.detailmovie.viewmodel.MovieDetailViewModel
import udinsi.dev.progress_svg.ProgressSvg

@AndroidEntryPoint
class MovieDetailActivity : BaseActivity() {
    private val movieDetailViewModel: MovieDetailViewModel by viewModels()
    lateinit var progressSvg: ProgressSvg
    lateinit var detailGenreAdapter: DetailGenreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        progressSvg = ProgressSvg(this)
        progressSvg.setMessage("Sedang memuat")

        val id = intent.getStringExtra("id")

        movieDetailViewModel.getDetailMovie(id!!)

        detailGenreAdapter = DetailGenreAdapter(object : DetailGenreAdapter.GenreListener{
            override fun onClick(genre: Genre, pos: Int) {

            }
        })

        lifecycleScope.launchWhenCreated {
            movieDetailViewModel.conversion.collect {
                when(it){
                    is MovieDetailViewModel.DetailMoveEvent.Success -> {
                        progressSvg.dissmis()
                        bindData(it.movieDetail)
                    }
                    is MovieDetailViewModel.DetailMoveEvent.Error -> {
                        progressSvg.dissmis()
                    }

                    is MovieDetailViewModel.DetailMoveEvent.Loading -> {
                        progressSvg.show()
                    }

                    else -> Unit
                }
            }
        }
    }

    fun bindData(movieDetailResponse: MovieDetailResponse){
        Picasso.get()
            .load(BuildConfig.IMAGE_BASE_URL.plus(movieDetailResponse.backdropPath))
            .into(ivDetailImage)

        tvTitle.text = movieDetailResponse.title
        tvRuntime.text = String.format("%s Minutes",movieDetailResponse.runtime)
        tvReleaseDate.text = movieDetailResponse.releaseDate
        tvRating.text = movieDetailResponse.voteAverage.toString()
        rvGenre.adapter = detailGenreAdapter
        tvOverview.text = movieDetailResponse.overview

        buttonFavorite.setOnClickListener {

        }
        if (movieDetailResponse.video!!){
            buttonPlay.visibility = View.VISIBLE
            buttonPlay.setOnClickListener {

            }
        }else{
            buttonPlay.visibility = View.GONE
        }

        detailGenreAdapter.updateList(movieDetailResponse.genres!!)
    }
}