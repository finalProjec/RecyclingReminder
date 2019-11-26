package com.example.recyclingreminder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class RegistrationChoiceActivity : AppCompatActivity() {
    internal var homeownerBtn : Button? = null
    internal var garbageCollectorBtn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_choice)

        initializeViews()

        homeownerBtn!!.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
        garbageCollectorBtn!!.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initializeViews() {
        homeownerBtn = findViewById(R.id.homeowner_signup)
        garbageCollectorBtn = findViewById(R.id.garbagecollector_signup)
    }

}