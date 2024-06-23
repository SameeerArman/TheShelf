package com.sam.theshelf

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.color.utilities.Score
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sam.theshelf.databinding.ActivityCategoryAddBinding
import com.sam.theshelf.databinding.ActivityLoginBinding
import kotlinx.coroutines.delay

class CategoryAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryAddBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCategoryAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this, DashboardAdminActivity::class.java))
        }

        binding.submitBtn.setOnClickListener {
            validateData()
            binding.bNameEt.setText("")
            binding.bScoreEt.setText("")
            binding.bPopEt.setText("")
            binding.bImgEt.setText("")
            binding.bDateEt.setText("")
            binding.bDescET.setText("")
        }
    }

    private var bookName = ""
    private var bookScore = ""
    private var bookPop = ""
    private var bookCoverUrl = ""
    private var bookDate = ""
    private var bookDesc = ""



    private fun validateData() {

        bookName = binding.bNameEt.text.toString().trim()
        bookScore = binding.bScoreEt.text.toString().trim()
        bookPop = binding.bPopEt.text.toString().trim()
        bookCoverUrl = binding.bImgEt.text.toString().trim()
        bookDate = binding.bDateEt.text.toString().trim()
        bookDesc = binding.bDescET.text.toString().trim()

        if (bookName.isEmpty()){
            Toast.makeText(this,"Enter Title...",Toast.LENGTH_LONG).show()
        }
        if (bookScore.isEmpty()){
            Toast.makeText(this,"Enter Score...",Toast.LENGTH_LONG).show()
        }
        if (bookPop.isEmpty()){
            Toast.makeText(this,"Enter Popularity...",Toast.LENGTH_LONG).show()
        }
        if (bookCoverUrl.isEmpty()){
            Toast.makeText(this,"Enter Cover URL...",Toast.LENGTH_LONG).show()
        }
        if (bookDate.isEmpty()){
            Toast.makeText(this,"Enter Published Date...",Toast.LENGTH_LONG).show()
            }
        if (bookDesc.isEmpty()){
            Toast.makeText(this,"Enter Book Description...",Toast.LENGTH_LONG).show()
        }
        else{
            addCategoryFirebase()
        }

    }

    private fun addCategoryFirebase() {
        progressDialog.show()
        val timestamp = System.currentTimeMillis()
        val hashMap = HashMap<String,Any>()
        hashMap["id"] = "$timestamp"
        hashMap["title"] = bookName
        hashMap["score"] = bookScore
        hashMap["popularity"] = bookPop
        hashMap["image"] = bookCoverUrl
        hashMap["publishedChapterDate"] = bookDate
        hashMap["uid"] = "${firebaseAuth.uid}"
        hashMap["desc"] = bookDesc

        val ref = FirebaseDatabase.getInstance().getReference("BookShelf")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this,"ADDED!!",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this,"Failed - ${e.message}",Toast.LENGTH_SHORT).show()
            }



    }
}