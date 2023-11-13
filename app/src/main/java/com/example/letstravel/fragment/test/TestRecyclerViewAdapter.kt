package com.example.letstravel.fragment.test

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.letstravel.R

class TestRecyclerViewAdapter(
    var context: Context,
    private var itemLists: ArrayList<TestItem>?
) : RecyclerView.Adapter<TestRecyclerViewAdapter.ViewHolder>() {


    // 아이템 뷰를 위한 뷰 홀더 객체를 생성하여 리턴
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.favorites_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (itemLists != null && itemLists!!.isNotEmpty()) itemLists!!.size else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ivFavorites.setImageResource(itemLists!![position].iconDrawable)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(itemLists: ArrayList<TestItem>) {
        this.itemLists = itemLists
        notifyDataSetChanged()
    }

//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var imageView: ImageView
////        var textView: TextView
//
//        init {
//            imageView = itemView.findViewById(R.id.iv_favorites_icon)
////            textView = itemView.findViewById(R.id.tv_favorites)
//        }
//    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivFavorites: ImageView
        var tvFavorites: TextView

        init {
            ivFavorites = itemView.findViewById(R.id.iv_favorites_icon)
            tvFavorites = itemView.findViewById(R.id.tv_favorites)
        }
    }
}

