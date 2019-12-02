package com.example.recyclingreminder

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeownerRegistrationActivity : AppCompatActivity() {

    private var emailET: EditText? = null
    private var passwordET: EditText? = null
    private var addressET: EditText? = null
    private var phoneNumberET: EditText? = null
    private var regBtn: Button? = null
    private var mAuth: FirebaseAuth? = null
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homeowner_registration)

        mAuth = FirebaseAuth.getInstance()

        initializeUI()

        regBtn!!.setOnClickListener { registerNewUser() }
    }

    private fun registerNewUser() {

        val email: String = emailET!!.text.toString()
        val password: String = passwordET!!.text.toString()
        val address: String  = addressET!!.text.toString() //TODO: address not stored anywhere
        val phoneNumber: String = phoneNumberET!!.text.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter an email ID...", Toast.LENGTH_LONG)
                .show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter a password...", Toast.LENGTH_LONG)
                .show()
            return
        }
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(applicationContext, "Please enter an address...", Toast.LENGTH_LONG)
                .show()
            return
        }
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(applicationContext, "Please enter a phone Number...", Toast.LENGTH_LONG)
                .show()
        }

        val user = hashMapOf(
            "phoneNumber" to phoneNumber,
            "address" to address,
            "violations" to ""
        )
        db.collection("homeowners").document(email).set(user)


        //TODO: change to phone number later
        mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "Registration successful!", Toast.LENGTH_LONG)
                    .show()

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
            }
        }
    }

    private fun initializeUI() {
        emailET = findViewById(R.id.homeowner_email_edittext)
        passwordET = findViewById(R.id.homeowner_password_edittext)
        addressET = findViewById(R.id.homeowner_address_edittext)
        phoneNumberET = findViewById(R.id.homeowner_phone_number_edittext)
        regBtn = findViewById(R.id.homeowner_register_button)
    }
}