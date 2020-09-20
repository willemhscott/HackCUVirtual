package com.github.henry232323.hackcuvirtual

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class LoginActivity : AppCompatActivity() {


    fun loginFail(){
        val failText = TextView(this)
        failText.setText("Login Failed")
    }

    fun loginSuccess(){
        val successText = TextView(this)
        successText.setText("Login Successful!")
        val intent = Intent(this, MainActivity::class.java)
        Messenger.instance.start(application)
        startActivity(intent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val msg = intent.getBooleanExtra(EXTRA_MESSAGE, false)

        val login_title: TextView = findViewById(R.id.login_title)
        val text:String = if(msg) "Already logged in" else "Not yet logged in";
        login_title.setText(text)

        //storing username and password from edit text boxes
        val iUsername = findViewById<EditText>(R.id.etUsername).toString()
        val iPassword = findViewById<EditText>(R.id.etPassword).toString()
        val bLogin = findViewById<Button>(R.id.btnLogin)



        bLogin.setOnClickListener{
            Messenger.instance.getToken(iUsername, iPassword, loginSuccess, loginFail)
        }

    }
}