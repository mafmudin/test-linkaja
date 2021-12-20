package test.linkaja.testapp.detailmovie

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import test.linkaja.testapp.BuildConfig
import test.linkaja.testapp.R
import test.linkaja.testapp.base.BaseActivity
import test.linkaja.testapp.detailmovie.adapter.DetailGenreAdapter
import test.linkaja.testapp.detailmovie.model.Genre
import test.linkaja.testapp.detailmovie.model.MovieDetailResponse
import test.linkaja.testapp.detailmovie.viewmodel.MovieDetailViewModel
import test.linkaja.testapp.favourite.FavouriteActivity
import test.linkaja.testapp.favourite.viewmodel.FavouriteViewModel
import udinsi.dev.progress_svg.ProgressSvg

@AndroidEntryPoint
class MovieDetailActivity : BaseActivity() {
    private val movieDetailViewModel: MovieDetailViewModel by viewModels()
    private val favouriteViewModel: FavouriteViewModel by viewModels()
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

        buttonBack.setOnClickListener {
            finish()
        }

        buttonFavorite.setOnClickListener {
            favouriteViewModel.setAsFavourite(id.toInt())
        }

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

        lifecycleScope.launch {
            favouriteViewModel.conversionSet.collect{
                when(it){
                    is FavouriteViewModel.SetFavouriteEvent.Success ->{
                        progressSvg.dissmis()
                        Toast.makeText(this@MovieDetailActivity, it.response.statusMessage, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@MovieDetailActivity, FavouriteActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    is FavouriteViewModel.SetFavouriteEvent.Error -> {
                        progressSvg.dissmis()
                    }
                    is FavouriteViewModel.SetFavouriteEvent.Loading ->{
                        progressSvg.show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun bindData(movieDetailResponse: MovieDetailResponse){
        Picasso.get()
            .load(BuildConfig.IMAGE_BASE_URL.plus(movieDetailResponse.backdropPath))
            .into(ivDetailImage)

        tvDetailTitle.text = movieDetailResponse.title
        tvRuntime.text = String.format("%s Minutes",movieDetailResponse.runtime)
        tvReleaseDate.text = movieDetailResponse.releaseDate
        tvRating.text = movieDetailResponse.voteAverage.toString()
        rvGenre.adapter = detailGenreAdapter
        tvOverview.text = movieDetailResponse.overview

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