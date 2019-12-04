package com.example.recyclingreminder

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import android.widget.ArrayAdapter
import kotlin.collections.ArrayList


class HomeOwnerDashboard : AppCompatActivity() {

    internal lateinit var listViewViolations: ListView
    internal lateinit var violations: MutableList<String>
    private val db = FirebaseFirestore.getInstance()
    private var editButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homeowner_dashboard)

        listViewViolations= findViewById<View>(R.id.listViewViolations) as ListView
        violations = ArrayList()
        editButton = findViewById(R.id.editButton) as Button

    }

    override fun onStart() {
        super.onStart()
        //Logged in users email
        val email = intent.getStringExtra("email")

        editButton?.setOnClickListener {
            var intent = Intent(this, EditHomeownerActivity :: class.java)
            intent.putExtra("email", email)
            startActivity(intent)
        }
        //Logged in users information
        val homeowner = db.collection("homeowners").document(email)

        homeowner.get()
            .addOnSuccessListener{doc ->
                Log.i("TAG", "VIOLATION LIST BEING QUERIED")

                var violations = doc.get("violations") as List<String>

                Log.i("TAG", violations.toString())

                //creating adapter using ViolationList
                val itemsAdapter =
                    ArrayAdapter(this, android.R.layout.simple_list_item_1, violations)

                //attaching adapter to the listview
                listViewViolations.adapter = itemsAdapter
            }
    }

    companion object {
    }
}
