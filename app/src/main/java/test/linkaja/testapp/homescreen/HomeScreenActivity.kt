package test.linkaja.testapp.homescreen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import test.linkaja.testapp.R
import test.linkaja.testapp.base.BaseActivity
import test.linkaja.testapp.homescreen.viewmodel.GenresViewModel
import udinsi.dev.progress_svg.ProgressSvg

@AndroidEntryPoint
class HomeScreenActivity : BaseActivity() {
    private val genreViewModel : GenresViewModel by viewModels()
    lateinit var progressSvg: ProgressSvg

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        progressSvg = ProgressSvg(this)
        progressSvg.setMessage("Sedang memuat")

        genreViewModel.getGenres()

        lifecycleScope.launchWhenCreated {
            genreViewModel.conversion.collect {
                when(it){
                    is GenresViewModel.GenreEvent.Success -> {
                        progressSvg.dissmis()
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
    }
}