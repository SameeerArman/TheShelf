package com.sam.theshelf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sam.theshelf.databinding.ActivityBookDetailBinding

class BookDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookDetailBinding

    private var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this, DashboardAdminActivity::class.java))
        }

        id = intent.getStringExtra("id")!!

        loadBookDetail()



    }

    private fun loadBookDetail() {
        val ref = FirebaseDatabase.getInstance().getReference("BookShelf")
        ref.child(id).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val title = "${snapshot.child("title").value}"
                val publishedChapterDate = "${snapshot.child("publishedChapterDate").value}"
                val score = "${snapshot.child("score").value}"
                val popularity = "${snapshot.child("popularity").value}"
                val image = "${snapshot.child("image").value}"


                binding.bookNameTv.text = title
                binding.bookPop2Tv.text = score
                binding.bookYear2.text = publishedChapterDate
                binding.bookScore2.text = popularity
                Glide.with(this@BookDetailActivity).load(image).into(binding.bookCoverIv)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}