package com.example.recyclingreminder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    companion object {
        val TAG = "final"
        val HOMEOWNERS = "homeowners"
        val GARBAGECOLLECTORS = "garbagecollectors"
    }

    private var userEmail: EditText? = null
    private var userPassword: EditText? = null
    private var loginBtn: Button? = null
    private var progressBar: ProgressBar? = null

    private val firestore = FirebaseFirestore.getInstance()
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        initializeUI()

        loginBtn!!.setOnClickListener { validateInput() }
    }

    private fun validateInput() {
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

        signInUser(email, password)
    }

    private fun signInUser(email: String, password: String) {

        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                progressBar!!.visibility = View.GONE
                if (task.isSuccessful) {
                    // check if employee ID exists in the Firestore database
                    checkFirestore(userEmail?.text.toString())
                } else {
                    Toast.makeText(
                        applicationContext, "Login failed! Please try again later",
                        Toast.LENGTH_LONG
                    ).show()
                    progressBar!!.visibility = View.GONE
                }
            }
    }

    private fun checkFirestore(email: String) {
        val homeownerDocRef = firestore.collection(HOMEOWNERS).document(email)
        val garbageDocRef = firestore.collection(GARBAGECOLLECTORS).document(email)

        homeownerDocRef.get()
            .addOnSuccessListener { document ->
                if (document == null || !document.exists()) {
                    // not a registered homeowner
                    // check garbage collector Firestore
                    garbageDocRef.get()
                        .addOnSuccessListener { document ->
                            if (document == null || !document.exists()) {
                                // not a registered homeowner or garbage collector
                                Toast.makeText(
                                    applicationContext, "Invalid login", Toast.LENGTH_LONG
                                ).show()
                                return@addOnSuccessListener
                            } else {
                                // is a registered garbage collector
                                Toast.makeText(
                                    applicationContext, "Successfully logged in", Toast.LENGTH_LONG
                                ).show()
                                val intent =
                                    Intent(this, GarbageCollectorDashboardActivity::class.java)
                                startActivity(intent)
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d(TAG, "get document failed with ", exception)
                        }
                    return@addOnSuccessListener
                } else {
                    // is a registered homeowner
                    Toast.makeText(applicationContext, "Successfully logged in", Toast.LENGTH_LONG)
                        .show()

                    val intent = Intent(this, HomeOwnerDashboard::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get document failed with ", exception)
            }
    }

    private fun initializeUI() {
        userEmail = findViewById(R.id.login_email)
        userPassword = findViewById(R.id.login_password)

        loginBtn = findViewById(R.id.login_button)
        progressBar = findViewById(R.id.login_progress_bar)
    }
}
