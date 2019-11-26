package com.example.recyclingreminder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class LoginChoiceActivity : AppCompatActivity() {
    internal var homeownerBtn : Button? = null
    internal var garbageCollectorBtn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_choice)

        initializeViews()

        homeownerBtn!!.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        garbageCollectorBtn!!.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initializeViews() {
        homeownerBtn = findViewById(R.id.homeowner_login)
        garbageCollectorBtn = findViewById(R.id.garbagecollector_login)
    }

}