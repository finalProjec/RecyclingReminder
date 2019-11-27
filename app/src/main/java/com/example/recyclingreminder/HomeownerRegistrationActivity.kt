package com.example.recyclingreminder

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class HomeownerRegistrationActivity : AppCompatActivity() {

    private var emailET: EditText? = null
    private var passwordET: EditText? = null
    private var addressET: EditText? = null
    private var phoneNumberET: EditText? = null
    private var regBtn: Button? = null
    private var progressBar: ProgressBar? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homeowner_registration)

        mAuth = FirebaseAuth.getInstance()

        initializeUI()

        regBtn!!.setOnClickListener { registerNewUser() }
    }

    private fun registerNewUser() {
        progressBar!!.visibility = View.VISIBLE

        val email: String
        val password: String
        val address: String //TODO: address not stored anywhere
        val phoneNumber: String

        email = emailET!!.text.toString()
        password = passwordET!!.text.toString()
        address = addressET!!.text.toString()
        phoneNumber = phoneNumberET!!.text.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter your name...", Toast.LENGTH_LONG)
                .show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter your password...", Toast.LENGTH_LONG)
                .show()
            return
        }
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(applicationContext, "Please enter your employee ID!", Toast.LENGTH_LONG)
                .show()
            return
        }
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(applicationContext, "Please enter your phone Number!", Toast.LENGTH_LONG).show()
            return
        }

        //TODO: change to phone number later
        mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "Registration successful!", Toast.LENGTH_LONG)
                    .show()
                progressBar!!.visibility = View.GONE

                val intent = Intent(
                    this@HomeownerRegistrationActivity,
                    HomeownerLoginActivity::class.java
                ) //change LoginActivity
                startActivity(intent)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Registration failed! Please try again later",
                    Toast.LENGTH_LONG
                ).show()
                progressBar!!.visibility = View.GONE
            }
        }
    }

    private fun initializeUI() {
        emailET = findViewById(R.id.homeowner_email_edittext)
        passwordET = findViewById(R.id.homeowner_password_edittext)
        addressET = findViewById(R.id.homeowner_address_edittext)
        phoneNumberET = findViewById(R.id.homeowner_phone_number_edittext)
        regBtn = findViewById(R.id.homeowner_register_button)
        progressBar = findViewById(R.id.progressBar)
    }
}