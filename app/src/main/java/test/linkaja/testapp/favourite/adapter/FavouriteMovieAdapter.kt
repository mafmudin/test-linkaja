package test.linkaja.testapp.favourite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rv_item_movie_favourite.view.*
import test.linkaja.testapp.BuildConfig
import test.linkaja.testapp.R
import test.linkaja.testapp.homescreen.model.movie.Movie

class FavouriteMovieAdapter(val listener: MovieListener) : RecyclerView.Adapter<FavouriteMovieAdapter.BaseViewHolder<*>>(){

    var list : MutableList<Movie> = arrayListOf()

    fun updateList(list: MutableList<Movie>){
        this.list = list
        notifyDataSetChanged()
    }

    fun addList(list: MutableList<Movie>){
        this.list.addAll(list)
        notifyItemInserted(this.list.size - 1)
    }

    abstract class BaseViewHolder<T> (itemView: View): RecyclerView.ViewHolder(itemView){
        abstract fun bind(item: Movie, pos: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_item_movie_favourite, parent, false)
        return ListHolder(view)
    }

    inner class ListHolder(itemView: View): BaseViewHolder<Movie>(itemView){
        override fun bind(item: Movie, pos: Int) {
            with(itemView){
                tvFavTitle.text = item.title
                tvOverview.text = item.overview
                tvRating.text = item.vote_average.toString()
                Picasso.get()
                    .load(BuildConfig.IMAGE_BASE_URL.plus(item.backdrop_path))
                    .placeholder(R.drawable.movie)
                    .into(ivFavMovie)

                itemView.setOnClickListener {
                    listener.onClick(item, pos)
                }
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
        fun onClick(movie:Movie, pos:Int)
    }

}