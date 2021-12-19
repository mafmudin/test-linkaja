package test.linkaja.testapp.homescreen

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home_screen.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import test.linkaja.testapp.R
import test.linkaja.testapp.base.BaseActivity
import test.linkaja.testapp.homescreen.adapter.GenreAdapter
import test.linkaja.testapp.homescreen.adapter.MovieAdapter
import test.linkaja.testapp.homescreen.model.genres.Genre
import test.linkaja.testapp.homescreen.model.movie.Movie
import test.linkaja.testapp.homescreen.model.movielist.MovieItem
import test.linkaja.testapp.homescreen.viewmodel.GenresViewModel
import udinsi.dev.progress_svg.ProgressSvg

@AndroidEntryPoint
class HomeScreenActivity : BaseActivity() {
    private val genreViewModel : GenresViewModel by viewModels()
    lateinit var progressSvg: ProgressSvg
    lateinit var movieAdapter: MovieAdapter
    lateinit var genreAdapter: GenreAdapter

    var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        progressSvg = ProgressSvg(this)
        progressSvg.setMessage("Sedang memuat")

        genreAdapter = GenreAdapter(object : GenreAdapter.GenreListener{
            override fun onClick(genre: Genre, pos: Int) {
                page = 1
                genreViewModel.getMovieByGenre(genre.id.toString(), page)
            }
        })

        movieAdapter = MovieAdapter(object : MovieAdapter.MovieListener{
            override fun onClick(genre: Movie, pos: Int) {

            }
        })

        rvGenre.adapter = genreAdapter
        rvMovie.adapter = movieAdapter

        genreViewModel.getGenres()
        genreViewModel.getMovie(page)

        lifecycleScope.launchWhenCreated {
            genreViewModel.conversion.collect {
                when(it){
                    is GenresViewModel.GenreEvent.Success -> {
                        progressSvg.dissmis()
                        genreAdapter.updateList(it.genresResponse)
                    }
                    is GenresViewModel.GenreEvent.Error -> {
                        progressSvg.dissmis()
                    }
                    is GenresViewModel.GenreEvent.Loading -> {
                        progressSvg.show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            genreViewModel.conversionPopularMovie.collect {
                when(it){
                    is GenresViewModel.PopularMovieEvent.Success -> {
                        progressSvg.dissmis()
                        movieAdapter.updateList(it.movieItem)
                    }
                    is GenresViewModel.PopularMovieEvent.Error -> {
                        progressSvg.dissmis()
                    }
                    is GenresViewModel.PopularMovieEvent.Loading -> {
                        progressSvg.show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            genreViewModel.conversionMovie.collect{
                when(it){
                    is GenresViewModel.MovieEvent.Success -> {
                        progressSvg.dissmis()
                        Log.e("RESULT", it.movieItem.toString())
                        movieAdapter.updateList(it.movieItem)
                    }
                    is GenresViewModel.MovieEvent.Error -> {
                        progressSvg.dissmis()
                    }
                    is GenresViewModel.MovieEvent.Loading -> {
                        progressSvg.show()
                    }

                    else -> Unit
                }
            }
        }
    }
}