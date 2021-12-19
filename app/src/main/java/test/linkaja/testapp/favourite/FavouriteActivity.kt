package test.linkaja.testapp.favourite

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_favourite.*
import kotlinx.coroutines.flow.collect
import test.linkaja.testapp.R
import test.linkaja.testapp.base.BaseActivity
import test.linkaja.testapp.detailmovie.MovieDetailActivity
import test.linkaja.testapp.favourite.adapter.FavouriteMovieAdapter
import test.linkaja.testapp.favourite.viewmodel.FavouriteViewModel
import test.linkaja.testapp.homescreen.model.movie.Movie
import udinsi.dev.progress_svg.ProgressSvg

@AndroidEntryPoint
class FavouriteActivity : BaseActivity() {
    private val favouriteViewModel: FavouriteViewModel by viewModels()
    lateinit var progressSvg: ProgressSvg
    lateinit var favouriteMovieAdapter: FavouriteMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        progressSvg = ProgressSvg(this)
        progressSvg.setMessage("Sedang memuat")

        favouriteMovieAdapter = FavouriteMovieAdapter(object : FavouriteMovieAdapter.MovieListener{
            override fun onClick(movie: Movie, pos: Int) {
                val intent = Intent(this@FavouriteActivity, MovieDetailActivity::class.java)
                intent.putExtra("id", movie.id.toString())
                startActivity(intent)
            }

        })

        buttonBack.setOnClickListener {
            finish()
        }

        rvFavouriteMovie.adapter = favouriteMovieAdapter

        favouriteViewModel.getFavouriteMovie()

        lifecycleScope.launchWhenCreated {
            favouriteViewModel.conversion.collect {
                when(it){
                    is FavouriteViewModel.FavouriteEvent.Success ->{
                        progressSvg.dissmis()
                        favouriteMovieAdapter.updateList(it.movies)
                    }

                    is FavouriteViewModel.FavouriteEvent.Error -> {
                        progressSvg.dissmis()
                    }

                    is FavouriteViewModel.FavouriteEvent.Loading -> {
                        progressSvg.show()
                    }

                    else -> Unit
                }
            }
        }
    }
}