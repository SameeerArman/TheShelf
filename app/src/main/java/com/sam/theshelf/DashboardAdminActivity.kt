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
import com.sam.theshelf.databinding.ActivityDashboardAdminBinding

class DashboardAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardAdminBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var bookArrayList: ArrayList<ModelBook>
    private lateinit var adapterBook: AdapterBook

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        loadBooks()

        binding.logoutBtn.setOnClickListener{
            firebaseAuth.signOut()
            checkUser()
        }
        binding.refreshBtn.setOnClickListener {
            startActivity(Intent(this,DashboardAdminActivity::class.java))
            Toast.makeText(this,"Refreshed",Toast.LENGTH_SHORT).show()
        }

        binding.addCategoryBtn.setOnClickListener {
            startActivity(Intent(this,CategoryAddActivity::class.java))
        }

    }

    private fun loadBooks() {
        bookArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("BookShelf")
        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children){
                    val model = ds.getValue(ModelBook::class.java)
                    bookArrayList.add(model!!)
                }
                adapterBook = AdapterBook(this@DashboardAdminActivity,bookArrayList)
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
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        else {
            val email = firebaseUser.email
            binding.subTitleTv.text = email
        }

    }
}