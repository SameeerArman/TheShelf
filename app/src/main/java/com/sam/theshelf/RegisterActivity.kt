package com.sam.theshelf

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sam.theshelf.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater )
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Wait...")
        progressDialog.setCanceledOnTouchOutside(false)


        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        binding.registerBtn.setOnClickListener {


            validateData()
        }


    }
        private var name = ""
        private var email = ""
        private var mobile = ""
        private var password = ""

    //Validation
    private fun validateData() {

        name = binding.nameEt.text.toString().trim()
        email = binding.emailEt.text.toString().trim()
        mobile = binding.mobileEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()
        val cPassword = binding.cPasswordEt.text.toString().trim()

        if (name.isEmpty()){
            Toast.makeText(this,"Enter your Name!!!", Toast.LENGTH_SHORT).show()
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this,"Email doesn't seem right!!! ", Toast.LENGTH_SHORT).show()
        }
        else if (password.isEmpty()){
            checkPassword()
            Toast.makeText(this,"Password is needed for your Security!!! ", Toast.LENGTH_SHORT).show()
        }
        else if (password.length<7){
            checkPassword()
            Toast.makeText(this,"Password is too short!!! ", Toast.LENGTH_SHORT).show()
        }
        else if (password.none { it.isDigit() }){
            checkPassword()
            Toast.makeText(this,"Password must contain a Number!!! ", Toast.LENGTH_SHORT).show()
        }
        else if (password.none { it.isUpperCase()}){
            checkPassword()
            Toast.makeText(this,"Password must have an Uppercase!!! ", Toast.LENGTH_SHORT).show()
        }
        else if (password.none { it.isLowerCase() }){
            checkPassword()
            Toast.makeText(this,"Password must have a Lowercase!!! ", Toast.LENGTH_SHORT).show()
        }
        else if (password.none { !it.isLetterOrDigit()}){
            checkPassword()
            Toast.makeText(this,"Password must have a Special Character!!! ", Toast.LENGTH_SHORT).show()
        }
        else if (cPassword.isEmpty()){
            Toast.makeText(this,"Confirm your Password!!! ", Toast.LENGTH_SHORT).show()
        }
        else if (password != cPassword){
                Toast.makeText(this,"Password doesn't match!!! ", Toast.LENGTH_SHORT).show()

        }
        else{
            createUserAccount()
        }

    }

    private fun checkPassword() {

    }

    //Account_Creation
    private fun createUserAccount() {

        progressDialog.setMessage("Account is being Created...")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                udpdateUserInfo()
            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(this,"We failed - ${e.message}!!! ", Toast.LENGTH_SHORT).show()
            }

    }
    //Save_to_Database
    private fun udpdateUserInfo() {
       progressDialog.setMessage("Your data is being saved...")
        val timestamp = System.currentTimeMillis()

        val  uid = firebaseAuth.uid

        val hashMap: HashMap<String,Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["email"] = email
        hashMap["name"] = name
        hashMap["mobile"] = mobile
        hashMap["profileImage"] = ""
        hashMap["userType"] = "user"
        hashMap["timestamp"] = timestamp

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this,"Account Created", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity, DashboardUserActivity::class.java))
                finish()
            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(this,"Save failed - ${e.message}!!! ", Toast.LENGTH_SHORT).show()
            }
    }
}