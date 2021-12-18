package test.linkaja.testapp.homescreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import test.linkaja.testapp.R
import test.linkaja.testapp.base.BaseActivity

class HomeScreenActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
    }
}