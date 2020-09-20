package com.github.henry232323.hackcuvirtual

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val msg = intent.getBooleanExtra(EXTRA_MESSAGE, false)

        val login_title: TextView = findViewById(R.id.login_title)
        val text:String = if(msg) "Already logged in" else "Not yet logged in";
        login_title.setText(text)

        //storing username and password from edit text boxes
        val bLogin = findViewById<Button>(R.id.btnLogin)

        val activity = this

        bLogin.setOnClickListener{
            val iUsername = findViewById<EditText>(R.id.etUsername).getText().toString()
            val iPassword = findViewById<EditText>(R.id.etPassword).getText().toString()

            Messenger.instance.getToken(iUsername, iPassword, object: Callback {
                override fun onFailure(call: Call, e: IOException){
                    val failText = TextView(activity)
                    failText.setText("Login Failed")
                }

                override fun onResponse(call: Call, response: Response) {
                    val successText = TextView(activity)
                    successText.setText("Login Successful!")
                    val intent = Intent(activity, MainActivity::class.java)
                    Messenger.instance.start(application)
                    startActivity(intent)
                }
            })
        }

    }
}