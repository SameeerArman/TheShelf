package com.sam.theshelf

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sam.theshelf.databinding.RowBooksBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with

class AdapterBook :RecyclerView.Adapter<AdapterBook.HolderBook>{

    private val context : Context
    private val bookArrayList : ArrayList<ModelBook>

    private lateinit var binding: RowBooksBinding

    constructor(context: Context, bookArrayList: ArrayList<ModelBook>) {
        this.context= context
        this.bookArrayList = bookArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderBook {

        binding = RowBooksBinding.inflate(LayoutInflater.from(context),parent,false)
        return HolderBook(binding.root)
    }
    override fun onBindViewHolder(holder: HolderBook, position: Int) {

        val model = bookArrayList[position]
        val id = model.id
        val title = model.title
        val score = model.score
        val popularity = model.popularity
        val image = model.image
        val publishedChapterDate = model.publishedChapterDate
        val uid = model.uid
        val desc = model.desc

        holder.bookNameTv.text = title
        holder.bookPop2Tv.text = score
Glide.with(context).load(image).into(binding.bookCoverIv)

        holder.itemView.setOnClickListener {
            val intent= Intent(context,BookDetailActivity::class.java)
            intent.putExtra("id",id)
            context.startActivity(intent)
        }


    }
    override fun getItemCount(): Int {
        return bookArrayList.size
    }



    inner class HolderBook(itemView: View): RecyclerView.ViewHolder(itemView){

        var bookNameTv:TextView = binding.bookNameTv
        var bookPop2Tv:TextView = binding.bookPop2Tv
        var bookCoverIv:ImageView = binding.bookCoverIv
    }



}