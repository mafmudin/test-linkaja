package test.linkaja.testapp.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import test.linkaja.testapp.R
import test.linkaja.testapp.base.BaseActivity
import test.linkaja.testapp.homescreen.HomeScreenActivity
import test.linkaja.testapp.splashscreen.viewmodel.RequestTokenViewModel
import test.linkaja.testapp.util.PrefsContant
import udinsi.dev.progress_svg.ProgressSvg

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : BaseActivity() {
    private val viewModel : RequestTokenViewModel by viewModels()
    lateinit var progressSvg: ProgressSvg

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        progressSvg = ProgressSvg(this)

        if (Prefs.getString(PrefsContant.SESSION_ID, "").isEmpty()){
            viewModel.requestToken()
        }else{
            Handler().postDelayed({
                val intent = Intent(this@SplashScreenActivity, HomeScreenActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.conversion.collect{
                when(it){
                    is RequestTokenViewModel.RequestTokenEvent.Failure ->{
                        progressSvg.dissmis()
                        Toast.makeText(this@SplashScreenActivity, it.errorText, Toast.LENGTH_SHORT).show()
                    }

                    is RequestTokenViewModel.RequestTokenEvent.Success -> {
                        progressSvg.dissmis()
                        Prefs.putString(PrefsContant.REQUEST_TOKEN, it.requestTokenResponse.request_token)
                        val url = "https://www.themoviedb.org/authenticate/${it.requestTokenResponse.request_token}?redirect_to=myapp://testlinkaja"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                        finish()
                    }

                    is RequestTokenViewModel.RequestTokenEvent.Loading -> {
                        progressSvg.show()
                    }

                    else -> Unit
                }
            }
        }
    }
}