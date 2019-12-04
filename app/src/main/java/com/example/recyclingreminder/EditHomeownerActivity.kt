package com.example.recyclingreminder

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class EditHomeownerActivity : AppCompatActivity() {

    private var addressET: EditText? = null
    private var phoneNumberET: EditText? = null
    private var updateBtn: Button? = null
    private var progressBar: ProgressBar? = null
    private val db = FirebaseFirestore.getInstance()
    private lateinit var email : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_homeowner)
        initializeUI()
        email = intent.getStringExtra("email")
        db.collection("homeowners").document(email).get()
            .addOnSuccessListener { document ->
                var address = document.get("address").toString()
                var phoneNumber = document.get("phonenumber").toString()
                addressET?.setText(address)
                phoneNumberET?.setText(phoneNumber)
            }

        updateBtn!!.setOnClickListener { updateUser() }
    }

    override fun onRestart() {
        super.onRestart()
        initializeUI()
        addressET!!.setText("")
        phoneNumberET!!.setText("")
    }

    private fun updateUser() {
        Log.i("TAG", email.toString())
        val homeowner = db.collection("homeowners").document(email)
        val address = addressET!!.text.toString()
        val number = phoneNumberET!!.text.toString()

        if (address != "") {
            homeowner.update("address", address)
        }
        if (number != "") {
            homeowner.update("phonenumber", number)
        }

        Toast.makeText(applicationContext, "Profile updated", Toast.LENGTH_LONG)
            .show()
        progressBar!!.visibility = View.GONE
        finish()

    }

    private fun initializeUI() {
        addressET = findViewById(R.id.homeowner_edit_address_text)
        phoneNumberET = findViewById(R.id.homeowner_edit_number_text)
        updateBtn = findViewById(R.id.update_button)
        progressBar = findViewById(R.id.homeowner_progress_bar)
    }
}