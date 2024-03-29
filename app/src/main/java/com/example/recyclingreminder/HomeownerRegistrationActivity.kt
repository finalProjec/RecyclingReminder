package com.example.recyclingreminder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class HomeownerRegistrationActivity : AppCompatActivity() {

    companion object {
        val TAG = "final"
        val HOMEOWNERS = "homeowners"
    }

    private var emailET: EditText? = null
    private var passwordET: EditText? = null
    private var addressET: EditText? = null
    private var phoneNumberET: EditText? = null
    private var regBtn: Button? = null
    private var mapButton: Button? = null
    private var progressBar: ProgressBar? = null

    private var mAuth: FirebaseAuth? = null
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homeowner_registration)

        mAuth = FirebaseAuth.getInstance()

        initializeUI()

        regBtn!!.setOnClickListener { validateInputs() }
    }

    fun onClick(v: View) {
        val intent = Intent(this, HomeownerRegistrationMap::class.java)
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            addressET!!.setText(data!!.getStringExtra("ADDRESS").toString())
        }
    }

    private fun validateInputs() {
        progressBar!!.visibility = View.VISIBLE

        val email: String = emailET!!.text.toString()
        val password: String = passwordET!!.text.toString()
        val address: String = addressET!!.text.toString()
        val phoneNumber: String = phoneNumberET!!.text.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter your email!", Toast.LENGTH_LONG)
                .show()
            progressBar!!.visibility = View.GONE
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(applicationContext, "Please enter a valid email!", Toast.LENGTH_LONG)
                .show()
            progressBar!!.visibility = View.GONE
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter your password!", Toast.LENGTH_LONG)
                .show()
            progressBar!!.visibility = View.GONE

        }
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(applicationContext, "Please enter your address!", Toast.LENGTH_LONG)
                .show()
            progressBar!!.visibility = View.GONE
        }
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(applicationContext, "Please enter your phone number!", Toast.LENGTH_LONG)
                .show()
            progressBar!!.visibility = View.GONE
        }
        else if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
            Toast.makeText(applicationContext, "Please enter a valid phone number!", Toast.LENGTH_LONG)
                .show()
            progressBar!!.visibility = View.GONE
        }
        else {
            registerNewUser(email, password, address, phoneNumber)
        }
        return
    }

    private fun registerNewUser(
        email: String,
        password: String,
        address: String,
        phoneNumber: String
    ) {

        mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "Registration successful!", Toast.LENGTH_LONG)
                    .show()
                progressBar!!.visibility = View.GONE

                // add homeowner to Firestore
                val email = email
                val docData = hashMapOf(
                    "phonenumber" to phoneNumber,
                    "address" to address
                )

                firestore.collection(HOMEOWNERS).document(email).set(docData)
                    .addOnSuccessListener {
                        Log.d(
                            "TAG", "New user added successfully"
                        )
                    }
                    .addOnFailureListener {
                        Log.w("TAG", "Error adding new user")
                    }

                val currentDate = LocalDateTime.now()

                val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                val formatted = currentDate.format(formatter).toString()

                firestore.collection(HOMEOWNERS).document(email).update("violations", FieldValue.arrayUnion(formatted))
                    .addOnSuccessListener {
                        Log.d(
                            "TAG", "Violation added successfully"
                        )
                    }
                    .addOnFailureListener {
                        Log.w("TAG", "Error adding violation")
                    }

                val intent = Intent(
                    this@HomeownerRegistrationActivity, LoginActivity::class.java
                ) //change LoginActivity
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    applicationContext, "Registration failed! Please try again later",
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
        mapButton = findViewById(R.id.homeowner_map_button)
        progressBar = findViewById(R.id.homeowner_progress_bar)
    }
}