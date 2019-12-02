package com.example.recyclingreminder

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

import java.util.ArrayList

class HomeOwnerDashboard : AppCompatActivity() {

    internal lateinit var listViewViolations: ListView
    internal lateinit var violations: MutableList<Violation>
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homeowner_dashboard)

        listViewViolations= findViewById<View>(R.id.listViewAuthors) as ListView
        violations = ArrayList()
    }

    override fun onStart() {
        super.onStart()
        val email = intent.getStringExtra("email")
        db.collection("homeowners").document("email").get()
            .addOnSuccessListener{dataSnapshot ->
                //clearing the previous list
                violations.clear()
                
                //creating adapter using AuthorList
                val violationAdapter = ViolationList(this@HomeOwnerDashboard, violations)
                //attaching adapter to the listview
                listViewViolations.adapter = violationAdapter
            }
    }

    companion object {
        val USER_ID = "com.example.tesla.myhomelibrary.userid"
    }
}



