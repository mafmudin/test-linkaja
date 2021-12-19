package test.linkaja.testapp.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import dagger.hilt.android.AndroidEntryPoint
import test.linkaja.testapp.R
import test.linkaja.testapp.base.BaseActivity
import test.linkaja.testapp.homescreen.HomeScreenActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            val intent = Intent(this@SplashScreenActivity, HomeScreenActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}