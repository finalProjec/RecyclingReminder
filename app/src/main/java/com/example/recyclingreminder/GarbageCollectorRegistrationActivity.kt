package com.example.recyclingreminder

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GarbageCollectorRegistrationActivity : AppCompatActivity() {

    companion object {
        val emailDomain = "@recycling.gov"
        val unregisteredIds = "unregistered employee ids"
        val TAG = "final"
        val GARBAGECOLLECTORS = "garbagecollectors"
    }

    private var emailET: EditText? = null
    private var passwordET: EditText? = null
    private var employeeIdET: EditText? = null
    private var regBtn: Button? = null
    private var progressBar: ProgressBar? = null

    private var mAuth: FirebaseAuth? = null
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.garbage_collector_registration)

        mAuth = FirebaseAuth.getInstance()

        initializeUI()

        regBtn!!.setOnClickListener { validateInput() }
    }

    override fun onRestart() {
        super.onRestart()

        initializeUI()
        emailET!!.setText("")
        passwordET!!.setText("")
        employeeIdET!!.setText("")
    }


    private fun validateInput() {
        Log.i(TAG, "validateInput")

        progressBar!!.visibility = View.VISIBLE

        val email: String = emailET!!.text.toString()
        val password: String = passwordET!!.text.toString()
        val employeeID: String =
            employeeIdET!!.text.toString()
        //val phoneNumber: String = phoneNumberET!!.text.toString()

        if (TextUtils.isEmpty(email) || !email.endsWith(emailDomain)) {
            Toast.makeText(
                applicationContext, "Please enter your government issued email...",
                Toast.LENGTH_LONG
            ).show()
            progressBar!!.visibility = View.GONE
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter your password...", Toast.LENGTH_LONG)
                .show()
            progressBar!!.visibility = View.GONE
            return
        }
        if (TextUtils.isEmpty(employeeID)) {
            Toast.makeText(applicationContext, "Please enter your employee ID!", Toast.LENGTH_LONG)
                .show()
            progressBar!!.visibility = View.GONE
            return
        }

        // check if employee ID exists in the Firestore database
        val docRef = firestore.collection(unregisteredIds).document(employeeID)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document == null || !document.exists()) {
                    progressBar!!.visibility = View.GONE
                    Toast.makeText(applicationContext, "Invalid employee ID", Toast.LENGTH_LONG)
                        .show()
                } else {
                    docRef.delete()
                    registerNewUser(email, password, employeeID)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get document failed with ", exception)
            }


    }

    private fun registerNewUser(email: String, password: String, employeeID: String) {
        Log.i(TAG, "registerNewUser")

        mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "Registration successful!", Toast.LENGTH_LONG)
                    .show()
                progressBar!!.visibility = View.GONE

                //add user to Firestore
                val docData = hashMapOf("employee id" to employeeID)

                firestore.collection(GARBAGECOLLECTORS).document(email).set(docData)
                    .addOnSuccessListener { Log.d(TAG, "New user added successfully") }
                    .addOnFailureListener { Log.w(TAG, "Error adding new user") }

                val intent = Intent(
                    this@GarbageCollectorRegistrationActivity, LoginActivity::class.java
                ) //change LoginActivity
                startActivity(intent)
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
        emailET = findViewById(R.id.garbage_email_edittext)
        passwordET = findViewById(R.id.garbage_password_edittext)
        employeeIdET = findViewById(R.id.garbage_employee_id_edittext)
        //phoneNumberET = findViewById(R.id.garbage_phone_number_edittext)
        regBtn = findViewById(R.id.garbage_register_button)
        progressBar = findViewById(R.id.garbage_progress_bar)
    }
}
