package com.example.letstravel.fragment.test.search

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.letstravel.R

class SearchRecyclerViewAdapter(var context: Context) :
    RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder>() {
    private var searchList: List<SearchItem> = ArrayList<SearchItem>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvSearch: TextView

        init {
            tvSearch = itemView.findViewById(R.id.tv_search)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (searchList != null && searchList!!.isNotEmpty()) searchList!!.size else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvSearch.text = searchList[position].place
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(searchList: ArrayList<SearchItem>) {
        notifyDataSetChanged()
        this.searchList = searchList
    }
}