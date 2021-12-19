package test.linkaja.testapp.detailmovie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rv_item_genre.view.*
import test.linkaja.testapp.R
import test.linkaja.testapp.detailmovie.model.Genre

class DetailGenreAdapter(val listener: GenreListener) : RecyclerView.Adapter<DetailGenreAdapter.BaseViewHolder<*>>(){

    var list : List<Genre> = arrayListOf()

    fun updateList(list: List<Genre>){
        this.list = list
        notifyDataSetChanged()
    }

    abstract class BaseViewHolder<T> (itemView: View): RecyclerView.ViewHolder(itemView){
        abstract fun bind(item: Genre, pos: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_item_genre_detail, parent, false)
        return ListHolder(view)
    }

    inner class ListHolder(itemView: View): BaseViewHolder<Genre>(itemView){
        override fun bind(item: Genre, pos: Int) {
            with(itemView){
                tvGenre.text = item.name
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

    interface GenreListener{
        fun onClick(genre:Genre, pos:Int)
    }

}