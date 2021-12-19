package test.linkaja.testapp.homescreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home_screen.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import test.linkaja.testapp.R
import test.linkaja.testapp.base.BaseActivity
import test.linkaja.testapp.detailmovie.MovieDetailActivity
import test.linkaja.testapp.favourite.FavouriteActivity
import test.linkaja.testapp.homescreen.adapter.GenreAdapter
import test.linkaja.testapp.homescreen.adapter.MovieAdapter
import test.linkaja.testapp.homescreen.adapter.ViewPagerAdapter
import test.linkaja.testapp.homescreen.model.genres.Genre
import test.linkaja.testapp.homescreen.model.movie.Movie
import test.linkaja.testapp.homescreen.viewmodel.GenresViewModel
import udinsi.dev.progress_svg.ProgressSvg
import java.util.*

@AndroidEntryPoint
class HomeScreenActivity : BaseActivity() {
    private val genreViewModel : GenresViewModel by viewModels()
    lateinit var progressSvg: ProgressSvg
    lateinit var movieAdapter: MovieAdapter
    lateinit var genreAdapter: GenreAdapter
    lateinit var viewPagerAdapter: ViewPagerAdapter

    var page = 1
    var currentPage = 0
    var currentQuery = ""
    var isAdd = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        progressSvg = ProgressSvg(this)
        progressSvg.setMessage("Sedang memuat")

        genreAdapter = GenreAdapter(object : GenreAdapter.GenreListener{
            override fun onClick(genre: Genre, pos: Int) {
                page = 1
                isAdd = false
                genreViewModel.getMovieByGenre(genre.id.toString(), page)
            }
        })

        movieAdapter = MovieAdapter(object : MovieAdapter.MovieListener{
            override fun onClick(movie: Movie, pos: Int) {
                val intent = Intent(this@HomeScreenActivity, MovieDetailActivity::class.java)
                intent.putExtra("id", movie.id.toString())
                startActivity(intent)
            }
        })

        viewPagerAdapter = ViewPagerAdapter(this)

        rvGenre.adapter = genreAdapter
        rvMovie.adapter = movieAdapter
        viewPager.adapter = viewPagerAdapter

        tvLoadMore.setOnClickListener {
            isAdd = true
            if (llSearchName.visibility == View.VISIBLE){
                genreViewModel.searchMovie(currentQuery, page++)
            }else{
                genreViewModel.getMovie(page++)
            }
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                currentPage = position
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

        val handler = Handler()
        val runnable = Runnable {
            if (currentPage == viewPagerAdapter.count-1){
                currentPage = 0
            }
            viewPager.setCurrentItem(currentPage++, true)
        }

        val timer = Timer()
        timer.schedule(object : TimerTask(){
            override fun run() {
                handler.post(runnable)
            }
        }, 500, 5000)

        buttonSearch.setOnClickListener {
            buttonFavorite.visibility = View.GONE
            val slideLeft = AnimationUtils.loadAnimation(this, R.anim.slide_left)

            if (llSearchName.visibility == View.GONE){
                llSearchName.startAnimation(slideLeft)
                llSearchName.visibility = View.VISIBLE
                viewPager.visibility = View.GONE
            }
        }

        buttonCloseSearch.setOnClickListener {
            etSearch.setText("")
            val slideRight = AnimationUtils.loadAnimation(this, R.anim.slide_right)
            llSearchName.visibility = View.GONE

            if (buttonFavorite.visibility == View.GONE){
                buttonFavorite.startAnimation(slideRight)
                buttonFavorite.visibility = View.VISIBLE
                viewPager.visibility = View.VISIBLE
            }
        }

        buttonFavorite.setOnClickListener {
            val intent = Intent(this@HomeScreenActivity, FavouriteActivity::class.java)
            startActivity(intent)
        }

        etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isAdd = false
                page = 1
                currentQuery = s.toString()
                genreViewModel.searchMovie(s.toString(), page)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        genreViewModel.getGenres()
        genreViewModel.getMovie(page)
        genreViewModel.getHighestRateMovie()

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
                        if (isAdd){
                            movieAdapter.addList(it.movieItem)
                        }else{
                            movieAdapter.updateList(it.movieItem)
                        }
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

        lifecycleScope.launchWhenCreated {
            genreViewModel.conversionHighestMovie.collect {
                when(it){
                    is GenresViewModel.HighestRateMovieEvent.Success -> {
                        progressSvg.dissmis()
                        viewPagerAdapter.updateList(it.movieItem)
                    }
                    is GenresViewModel.HighestRateMovieEvent.Error -> {
                        progressSvg.dissmis()
                    }
                    is GenresViewModel.HighestRateMovieEvent.Loading -> {
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

        lifecycleScope.launch {
            genreViewModel.conversionSearch.collect{
                when(it){
                    is GenresViewModel.SearchEvent.Success -> {
//                        progressSvg.dissmis()
                        Log.e("RESULT SEARCH", it.movieItem.toString())
                        if (isAdd){
                            movieAdapter.addList(it.movieItem)
                        }else{
                            movieAdapter.updateList(it.movieItem)
                        }
                    }
                    is GenresViewModel.SearchEvent.Error -> {
//                        progressSvg.dissmis()
                    }
                    is GenresViewModel.SearchEvent.Loading -> {
//                        progressSvg.show()
                    }

                    else -> Unit
                }
            }
        }
    }
}