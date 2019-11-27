package com.example.recyclingreminder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class GarbageCollectorLoginActivity: AppCompatActivity() {
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var userEmail: EditText? = null
    private var userPassword: EditText? = null
    private var loginBtn: Button? = null
    private var progressBar: ProgressBar? = null

    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_garbagecollector)

//        mDatabase = FirebaseDatabase.getInstance()
//        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()

        initializeUI()

        loginBtn!!.setOnClickListener { loginUserAccount() }
    }

    private fun loginUserAccount() {
        progressBar!!.visibility = View.VISIBLE

        val email = userEmail?.text.toString()
        val password = userPassword?.text.toString()

        if (email == "") {
            Toast.makeText(applicationContext, "Please enter an email", Toast.LENGTH_LONG).show()
            progressBar!!.visibility = View.GONE
            return
        }

        if (password == "") {
            Toast.makeText(applicationContext, "Please enter a password", Toast.LENGTH_LONG).show()
            progressBar!!.visibility = View.GONE
            return
        }

        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                progressBar!!.visibility = View.GONE
                if (task.isSuccessful()) {
                    val uid = mAuth!!.currentUser?.uid
                    val intent = Intent(this, GarbageCollectorDashboardActivity:: class.java)
                    intent.putExtra("userid", uid)
                    intent.putExtra("useremail", email)
                    startActivity(intent)
                }
                else {
                    Toast.makeText(applicationContext, "Login failed! Please try again later", Toast.LENGTH_LONG).show()
                }
            }


    }

    private fun initializeUI() {
        userEmail = findViewById(R.id.garbageCollectorEmail)
        userPassword = findViewById(R.id.garbageCollectorPassword)

        loginBtn = findViewById(R.id.login2)
        progressBar = findViewById(R.id.progressBar2)
    }
}
