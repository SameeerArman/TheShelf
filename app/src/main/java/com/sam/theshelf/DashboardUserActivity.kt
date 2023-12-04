package com.sam.theshelf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sam.theshelf.databinding.ActivityDashboardUserBinding

class DashboardUserActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDashboardUserBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var bookArrayList: ArrayList<ModelBook>
    private lateinit var adapterBook: AdapterBook

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        loadBooks()

        binding.logoutBtn.setOnClickListener{
            firebaseAuth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.refreshBtn.setOnClickListener {
            startActivity(Intent(this,DashboardUserActivity::class.java))
            Toast.makeText(this,"Refreshed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadBooks() {
        bookArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("BookShelf")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children){
                    val model = ds.getValue(ModelBook::class.java)
                    bookArrayList.add(model!!)
                }
                adapterBook = AdapterBook(this@DashboardUserActivity,bookArrayList)
                binding.booksRv.adapter = adapterBook


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }




    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null){

            binding.subTitleTv.text = "Not Logged In"
        }
        else {
            val email = firebaseUser.email
            binding.subTitleTv.text = email

        }
    }





}