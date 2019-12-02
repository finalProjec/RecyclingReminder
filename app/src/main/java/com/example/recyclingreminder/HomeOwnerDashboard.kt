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

import java.util.ArrayList

class HomeOwnerDashboard : AppCompatActivity() {

    internal lateinit var listViewViolations: ListView
    internal lateinit var violations: MutableList<Violation>
    internal lateinit var databaseHomeOwners: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homeowner_dashboard)

        databaseHomeOwners = FirebaseDatabase.getInstance().getReference("homeowners") //Add an ID for that homeowner
        listViewViolations= findViewById<View>(R.id.listViewAuthors) as ListView
        violations = ArrayList()
    }

    override fun onStart() {
        super.onStart()
        databaseHomeOwners.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //clearing the previous list
                violations.clear()

                // getting violations only for the Current User
                val userShot = dataSnapshot.child(intent.getStringExtra(USER_ID)).children

                //iterating through all the nodes
                for (v in userShot) {
                    //getting violations
                    val violation = v.getValue(Violation::class.java)
                    //adding violation to the list
                    violations.add(violation!!)
                }


                //creating adapter using AuthorList
                val violationAdapter = ViolationList(this@HomeOwnerDashboard, violations)
                //attaching adapter to the listview
                listViewViolations.adapter = violationAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    companion object {
        val USER_ID = "com.example.tesla.myhomelibrary.userid"
    }
}



