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
import test.linkaja.testapp.homescreen.viewmodel.MovieViewModel
import udinsi.dev.progress_svg.ProgressSvg
import java.util.*

@AndroidEntryPoint
class HomeScreenActivity : BaseActivity() {
    private val movieViewModel : MovieViewModel by viewModels()
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
                movieViewModel.getMovieByGenre(genre.id.toString(), page)
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
            page++
            if (llSearchName.visibility == View.VISIBLE){
                movieViewModel.searchMovie(currentQuery, page)
            }else{
                movieViewModel.getMovie(page)
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
        }, 500, 3000)

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
                movieViewModel.searchMovie(s.toString(), page)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        movieViewModel.getHighestRateMovie()

        movieViewModel.getLocalMovie()
        movieViewModel.getLocalGenre()

        lifecycleScope.launchWhenCreated {
            movieViewModel.conversionPopularMovie.collect{
                when(it){
                    is MovieViewModel.PopularMovieEvent.Loading ->{
                        progressSvg.show()
                    }
                    is MovieViewModel.PopularMovieEvent.Success -> {
                        progressSvg.dissmis()
                        if (it.movieItem.isEmpty()){
                            movieViewModel.getMovie(page)
                        }else{
                            if (isAdd){
                                movieAdapter.addList(it.movieItem)
                            }else{
                                movieAdapter.updateList(it.movieItem)
                            }
                            viewPagerAdapter.updateList(it.movieItem)
                        }
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            movieViewModel.conversion.collect {
                when(it){
                    is MovieViewModel.GenreEvent.Success -> {
                        progressSvg.dissmis()
                        if (it.genresResponse.isEmpty()){
                            movieViewModel.getGenres()
                        }else{
                            genreAdapter.updateList(it.genresResponse)
                        }
                    }
                    is MovieViewModel.GenreEvent.Error -> {
                        progressSvg.dissmis()
                    }
                    is MovieViewModel.GenreEvent.Loading -> {
                        progressSvg.show()
                    }
                    else -> Unit
                }
            }
        }

//        lifecycleScope.launchWhenCreated {
//            movieViewModel.conversionPopularMovie.collect {
//                when(it){
//                    is MovieViewModel.PopularMovieEvent.Success -> {
//                        progressSvg.dissmis()
//                        if (isAdd){
//                            movieAdapter.addList(it.movieItem)
//                        }else{
//                            movieAdapter.updateList(it.movieItem)
//                        }
//                        viewPagerAdapter.updateList(it.movieItem)
//                    }
//                    is MovieViewModel.PopularMovieEvent.Error -> {
//                        progressSvg.dissmis()
//                    }
//                    is MovieViewModel.PopularMovieEvent.Loading -> {
//                        progressSvg.show()
//                    }
//                    else -> Unit
//                }
//            }
//        }

        lifecycleScope.launchWhenCreated {
            movieViewModel.conversionHighestMovie.collect {
                when(it){
                    is MovieViewModel.HighestRateMovieEvent.Success -> {
                        progressSvg.dissmis()
                        viewPagerAdapter.updateList(it.movieItem)
                    }
                    is MovieViewModel.HighestRateMovieEvent.Error -> {
                        progressSvg.dissmis()
                    }
                    is MovieViewModel.HighestRateMovieEvent.Loading -> {
                        progressSvg.show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            movieViewModel.conversionMovie.collect{
                when(it){
                    is MovieViewModel.MovieEvent.Success -> {
                        progressSvg.dissmis()
                        Log.e("RESULT", it.movieItem.toString())
                        movieAdapter.updateList(it.movieItem)
                    }
                    is MovieViewModel.MovieEvent.Error -> {
                        progressSvg.dissmis()
                    }
                    is MovieViewModel.MovieEvent.Loading -> {
                        progressSvg.show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            movieViewModel.conversionSearch.collect{
                when(it){
                    is MovieViewModel.SearchEvent.Success -> {
//                        progressSvg.dissmis()
                        Log.e("RESULT SEARCH", it.movieItem.toString())
                        if (isAdd){
                            movieAdapter.addList(it.movieItem)
                        }else{
                            movieAdapter.updateList(it.movieItem)
                        }
                    }
                    is MovieViewModel.SearchEvent.Error -> {
//                        progressSvg.dissmis()
                    }
                    is MovieViewModel.SearchEvent.Loading -> {
//                        progressSvg.show()
                    }

                    else -> Unit
                }
            }
        }
    }

    override fun onBackPressed() {
        etSearch.setText("")
        val slideRight = AnimationUtils.loadAnimation(this, R.anim.slide_right)
        llSearchName.visibility = View.GONE

        if (buttonFavorite.visibility == View.GONE){
            buttonFavorite.startAnimation(slideRight)
            buttonFavorite.visibility = View.VISIBLE
            viewPager.visibility = View.VISIBLE
        }else{
            super.onBackPressed()
        }
    }
}