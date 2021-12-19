package test.linkaja.testapp.homescreen.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import test.linkaja.testapp.homescreen.model.movie.Movie
import android.view.LayoutInflater

import android.widget.ImageView
import com.squareup.picasso.Picasso
import test.linkaja.testapp.BuildConfig
import test.linkaja.testapp.R


class ViewPagerAdapter(private val context: Context): PagerAdapter() {
    private var list = arrayListOf<Movie>()

    fun updateList(list: ArrayList<Movie>){
        this.list = list
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val movie = list[position]
        val mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = mLayoutInflater.inflate(R.layout.image_slider_item, container, false)
        val imageView = view.findViewById<ImageView>(R.id.imageViewSlider)

        Picasso.get()
            .load(BuildConfig.IMAGE_BASE_URL.plus(movie.backdrop_path))
            .into(imageView)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}