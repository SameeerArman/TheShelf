package com.sam.theshelf

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sam.theshelf.databinding.ActivityLogin2Binding

class Login2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityLogin2Binding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.noAccountTv.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.emailLoginTv.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.loginBtn.setOnClickListener {

            validateData()
        }

    }

    private var mobile = ""
    private var password = ""

    private fun validateData() {

        mobile = binding.mobileEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

//validation
        if (!Patterns.PHONE.matcher(mobile).matches()) {
            Toast.makeText(this,"Email doesn't seems right!!! ", Toast.LENGTH_SHORT).show()
        }
        else if (password.isEmpty()){
            Toast.makeText(this,"No password No Security!!! ", Toast.LENGTH_SHORT).show()
        }
        else {
            loginUser()
        }

    }
    //login
    private fun loginUser() {

        progressDialog.setMessage("Logging you in...")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(mobile,password)
            .addOnSuccessListener {
                checkUser()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this,"Login failed - ${e.message}!!! ", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        progressDialog.setMessage("Checking User...")

        val firebaseUser= firebaseAuth.currentUser!!
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child((firebaseUser.uid))
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    progressDialog.dismiss()
                    val userType =  snapshot.child("userType").value

                    if (userType == "user"){
                        startActivity(Intent(this@Login2Activity, DashboardUserActivity::class.java))
                        finish()
                    }
                    else if (userType == "admin"){
                        startActivity(Intent(this@Login2Activity, DashboardAdminActivity::class.java))
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

}

