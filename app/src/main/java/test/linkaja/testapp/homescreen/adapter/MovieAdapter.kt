package test.linkaja.testapp.homescreen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rv_item_movie.view.*
import test.linkaja.testapp.BuildConfig
import test.linkaja.testapp.R
import test.linkaja.testapp.homescreen.model.movie.Movie
import test.linkaja.testapp.homescreen.model.movielist.MovieItem

class MovieAdapter(val listener: MovieListener) : RecyclerView.Adapter<MovieAdapter.BaseViewHolder<*>>(){

    var list : List<Movie> = arrayListOf()

    fun updateList(list: List<Movie>){
        this.list = list
        notifyDataSetChanged()
    }

    abstract class BaseViewHolder<T> (itemView: View): RecyclerView.ViewHolder(itemView){
        abstract fun bind(item: Movie, pos: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_item_movie, parent, false)
        return ListHolder(view)
    }

    inner class ListHolder(itemView: View): BaseViewHolder<Movie>(itemView){
        override fun bind(item: Movie, pos: Int) {
            with(itemView){
                tvTile.text = item.title
                Picasso.get()
                    .load(BuildConfig.IMAGE_BASE_URL.plus(item.poster_path))
                    .placeholder(R.drawable.movie)
                    .into(ivMovie)
            }
        }

    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val data = list[position]
        holder.bind(data, position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface MovieListener{
        fun onClick(genre:Movie, pos:Int)
    }

}